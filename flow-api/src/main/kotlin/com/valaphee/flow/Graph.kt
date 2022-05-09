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

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.UUID
import java.util.concurrent.Executors

/**
 * @author Kevin Ludwig
 */
class Graph(
    @get:JsonProperty("id") val id: UUID = UUID.randomUUID(),
    @get:JsonProperty("graph") val graph: List<Node>
) : CoroutineScope {
    @get:JsonIgnore override val coroutineContext get() = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    init {
        graph.forEach { it.run(this) }
    }
}
