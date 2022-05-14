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

package com.valaphee.flow.control

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
@Node("Control/For")
class For(
    @get:In (""     , ""          , "" ) @get:JsonProperty("in"            ) override val `in`        : ControlPath,
    @get:In ("Start", DataType.Num, "0") @get:JsonProperty("in_range_start")          val inRangeStart: DataPath   ,
    @get:In ("End"  , DataType.Num, "9") @get:JsonProperty("in_range_end"  )          val inRangeEnd  : DataPath   ,
    @get:In ("Step" , DataType.Num, "1") @get:JsonProperty("in_step"       )          val inStep      : DataPath   ,
    @get:Out("Body" , ""               ) @get:JsonProperty("out_body"      )          val outBody     : ControlPath,
    @get:Out("Exit" , ""               ) @get:JsonProperty("out"           )          val out         : ControlPath,
    @get:Out("Index", DataType.Num     ) @get:JsonProperty("out_index"     )          val outIndex    : DataPath   ,
) : StatefulNode() {
    override fun initialize() {
        `in`.declare {
            IntProgression.fromClosedRange((inRangeStart.getOrThrow<Number>("in_range_start")).toInt(), (inRangeEnd.getOrThrow<Number>("in_range_end")).toInt(), (inStep.getOrThrow<Number>("in_step")).toInt()).forEach {
                outIndex.set(it)
                outBody()
            }
            out()
        }
    }
}
