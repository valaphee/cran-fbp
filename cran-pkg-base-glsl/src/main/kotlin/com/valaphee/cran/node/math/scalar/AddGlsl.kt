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

package com.valaphee.cran.node.math.scalar

import com.valaphee.cran.Glsl
import com.valaphee.cran.node.Node
import com.valaphee.cran.node.NodeGlsl
import com.valaphee.cran.spec.NodeImpl

/**
 * @author Kevin Ludwig
 */
@NodeImpl("glsl")
object AddGlsl : NodeGlsl {
    override fun initialize(node: Node, glsl: Glsl) = if (node is Add) {
        val inA = glsl.getVariable(node.inA)
        val inB = glsl.getVariable(node.inB)

        glsl.defineVariable(node.out, "${inA.declare()} + ${inB.declare()}")

        true
    } else false
}
