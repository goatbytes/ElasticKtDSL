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
  "FunctionNaming",
  "MatchingDeclarationName"
)

package io.goatbytes.elasticsearch.query.fulltext

import org.elasticsearch.index.query.CommonTermsQueryBuilder
import org.elasticsearch.index.query.Operator

/**
 * common
 */
class CommonBlock {

  @Deprecated(message = "Use invoke operator instead.", replaceWith = ReplaceWith("invoke(init)"))
  infix fun String.to(init: CommonData.() -> Unit): CommonData {
    return this.invoke(init)
  }

  operator fun String.invoke(init: CommonData.() -> Unit): CommonData {
    return CommonData(name = this).apply(init)
  }

  data class CommonData(
    var name: String,
    var query: Any? = null,
    var high_freq_operator: String? = null,
    var low_freq_operator: String? = null,
    var analyzer: String? = null,
    var boost: Float? = null,
    var cutoff_frequency: Float? = null,
    val minimum_should_match: MinimumShouldMatchData = MinimumShouldMatchData()
  ) {

    fun minimum_should_match(init: MinimumShouldMatchData.() -> Unit) {
      this.minimum_should_match.init()
    }

    infix fun MinimumShouldMatchData.to(numOrStr: Any) {
      this.low_freq = numOrStr
    }
  }

  data class MinimumShouldMatchData(var low_freq: Any? = null, var high_freq: Any? = null)
}

fun common(init: CommonBlock.() -> CommonBlock.CommonData): CommonTermsQueryBuilder {
  val params = CommonBlock().init()
  return CommonTermsQueryBuilder(params.name, params.query).apply {
    params.high_freq_operator?.let { highFreqOperator(Operator.fromString(it)) }
    params.low_freq_operator?.let { lowFreqOperator(Operator.fromString(it)) }
    params.analyzer?.let { analyzer(it) }
    params.boost?.let { boost(it) }
    params.cutoff_frequency?.let { cutoffFrequency(it) }
    params.minimum_should_match.low_freq?.toString()?.let { lowFreqMinimumShouldMatch(it) }
    params.minimum_should_match.high_freq?.toString()?.let { highFreqMinimumShouldMatch(it) }
  }
}
