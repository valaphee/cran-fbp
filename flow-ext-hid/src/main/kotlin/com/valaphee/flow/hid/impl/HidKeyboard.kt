/*
 * Copyright (c) 2022, Valaphee.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.valaphee.flow.hid.impl

import com.valaphee.flow.hid.Key
import com.valaphee.flow.hid.Keyboard
import org.hid4java.HidDevice
import org.hid4java.HidManager
import java.util.BitSet
import kotlin.concurrent.thread

/**
 * @author Kevin Ludwig
 */
class HidKeyboard : Keyboard() {
    override fun keyPress(key: Key) = if (!keys.contains(key) && keys.size <= 6) {
        keys += key
        write()
        true
    } else false

    override fun keyRelease(key: Key) = if (keys.contains(key)) {
        keys -= key
        write()
        true
    } else false

    companion object {
        private var hidDevice: HidDevice? = null
        private const val path = "\\\\?\\hid#variable_6&col04#1"

        private val keys = mutableSetOf<Key>()

        init {
            Runtime.getRuntime().addShutdownHook(thread(false) {
                if (keys.isNotEmpty()) {
                    keys.clear()
                    write()
                }
                hidDevice?.close()
            })

            hidDevice = HidManager.getHidServices().attachedHidDevices.find { it.path.startsWith(path) }?.also { it.open() }
        }

        private fun write() {
            hidDevice?.let {
                if (it.isOpen) {
                    val message = ByteArray(2 + keys.size)
                    val modifiers = BitSet().apply {
                        if (keys.contains(Key.LeftControl)) set(0)
                        if (keys.contains(Key.LeftShift)) set(1)
                        if (keys.contains(Key.LeftAlt)) set(2)
                        if (keys.contains(Key.LeftMeta)) set(3)
                        if (keys.contains(Key.RightControl)) set(4)
                        if (keys.contains(Key.RightShift)) set(5)
                        if (keys.contains(Key.RightAlt)) set(6)
                        if (keys.contains(Key.RightMeta)) set(7)
                    }
                    message[0] = if (modifiers.isEmpty) 0 else modifiers.toByteArray()[0]
                    message[1] = 0x00
                    keys.forEachIndexed { i, key -> message[2 + i] = key.scanCode.toByte() }
                    it.write(message, message.size, 0x04)
                }
            }
        }
    }
}
