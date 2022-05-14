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

package com.valaphee.flow

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.ObjectIdGenerator
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import com.fasterxml.jackson.annotation.SimpleObjectIdResolver

/**
 * @author Kevin Ludwig
 */
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.PropertyGenerator::class, resolver = ControlPath.IdResolver::class)
@JsonIdentityReference(alwaysAsId = true)
class ControlPath(
    override val id: Int
) : Path() {
    class IdResolver : SimpleObjectIdResolver() {
        override fun resolveId(id: ObjectIdGenerator.IdKey) = super.resolveId(id) ?: ControlPath(id.key as Int).also { bindItem(id, it) }

        override fun newForDeserialization(context: Any?) = IdResolver()
    }

    private var function: (suspend () -> Unit)? = null

    suspend operator fun invoke() {
        function?.invoke()
    }

    fun declare(function: suspend () -> Unit) {
        if (this.function != null) throw ControlPathException.AlreadySet

        this.function = function
    }
}
