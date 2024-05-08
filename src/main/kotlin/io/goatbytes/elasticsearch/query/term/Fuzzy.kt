/*
 * Copyright 2024 GoatBytes.IO
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

@file:Suppress(
  "ConstructorParameterNaming",
  "UndocumentedPublicFunction",
  "UndocumentedPublicClass",
  "MatchingDeclarationName"
)

package io.goatbytes.elasticsearch.query.term

import io.goatbytes.elasticsearch.query.QueryData
import io.goatbytes.elasticsearch.query.initQuery
import org.elasticsearch.common.unit.Fuzziness
import org.elasticsearch.index.query.FuzzyQueryBuilder

class FuzzyBlock {
  class FuzzyData(
    val name: String,
    var value: Any? = null,
    var fuzziness: Fuzziness? = null,
    var prefix_length: Int? = null,
    var max_expansions: Int? = null,
    var transpositions: Boolean? = null
  ) : QueryData()

  infix fun String.to(value: Any) = FuzzyData(name = this, value = value)

  @Deprecated(message = "Use invoke operator instead.", replaceWith = ReplaceWith("invoke(init)"))
  infix fun String.to(init: FuzzyData.() -> Unit) = this.invoke(init)

  operator fun String.invoke(init: FuzzyData.() -> Unit) = FuzzyData(name = this).apply(init)
}

@Suppress("DEPRECATION")
fun fuzzy(init: FuzzyBlock.() -> FuzzyBlock.FuzzyData): FuzzyQueryBuilder {
  val params = FuzzyBlock().init()
  return FuzzyQueryBuilder(params.name, params.value).apply {
    initQuery(params)
    params.fuzziness?.let { fuzziness(it) }
    params.prefix_length?.let { prefixLength(it) }
    params.max_expansions?.let { maxExpansions(it) }
    transpositions(params.transpositions ?: false)
  }
}
