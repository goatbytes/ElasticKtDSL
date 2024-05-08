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

package io.goatbytes.elasticsearch.query.fulltext

import org.elasticsearch.common.unit.Fuzziness
import org.elasticsearch.index.query.MatchQueryBuilder
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.search.MatchQuery

/**
 * match
 */
class MatchBlock {

  @Deprecated(message = "Use invoke operator instead.", replaceWith = ReplaceWith("invoke(init)"))
  infix fun String.to(init: MatchData.() -> Unit) = this.invoke(init)

  operator fun String.invoke(init: MatchData.() -> Unit) = MatchData(name = this).apply(init)

  infix fun String.to(query: Any) = MatchData(name = this, query = query)

  data class MatchData(
    var name: String,
    var query: Any? = null,
    var operator: String? = null,
    var analyzer: String? = null,
    var boost: Float? = null,
    var fuzziness: Fuzziness? = null,
    var prefix_length: Int? = null,
    var max_expansions: Int? = null,
    var minimum_should_match: String? = null,
    var fuzzy_rewrite: String? = null,
    var lenient: Boolean? = null,
    var fuzzy_transpositions: Boolean? = null,
    var zero_terms_query: String? = null,
    var cutoff_frequency: Float? = null
  )
}

fun match(init: MatchBlock.() -> MatchBlock.MatchData): MatchQueryBuilder {
  val params = MatchBlock().init()
  return MatchQueryBuilder(params.name, params.query).apply {
    params.analyzer?.let { analyzer(it) }
    params.boost?.let { boost(it) }
    params.cutoff_frequency?.let { cutoffFrequency(it) }
    params.fuzziness?.let { fuzziness(it) }
    params.fuzzy_rewrite?.let { fuzzyRewrite(it) }
    params.fuzzy_transpositions?.let { fuzzyTranspositions(it) }
    params.lenient?.let { lenient(it) }
    params.max_expansions?.let { maxExpansions(it) }
    params.minimum_should_match?.let { minimumShouldMatch(it) }
    params.operator?.let { operator(Operator.fromString(it)) }
    params.prefix_length?.let { prefixLength(it) }
    params.zero_terms_query?.let { zeroTermsQuery(MatchQuery.ZeroTermsQuery.valueOf(it.toUpperCase())) }
  }
}
