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

package com.valaphee.flow.hid

import com.fasterxml.jackson.annotation.JsonProperty
import com.valaphee.flow.ControlPath
import com.valaphee.flow.DataPath
import com.valaphee.flow.StatefulNode
import com.valaphee.flow.spec.DataType
import com.valaphee.flow.spec.In
import com.valaphee.flow.spec.Node
import com.valaphee.flow.spec.Out

/**
 * @author Kevin Ludwig
 */
@Node("HID/Mouse Button")
class MouseButton(
    @get:In (""     , ""          , "") @get:JsonProperty("in"       ) override val `in`    : ControlPath,
    @get:In (""     , DataType.Num, "") @get:JsonProperty("in_button")          val inButton: DataPath   ,
    @get:In ("State", DataType.Bin, "") @get:JsonProperty("in_state" )          val inState : DataPath   ,
    @get:Out(""     , ""              ) @get:JsonProperty("out"      )          val out     : ControlPath
) : StatefulNode() {
    override fun initialize() {
        `in`.declare {
            val button = inButton.getOrThrow<Int>("in_button")
            if (inState.getOrThrow("in_state")) Mouse.mouse.mousePress(button) else Mouse.mouse.mouseRelease(button)
            out()
        }
    }
}
