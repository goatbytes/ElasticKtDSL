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
  "MatchingDeclarationName",
  "FunctionNaming",
)

package io.goatbytes.elasticsearch.query.fulltext

import org.elasticsearch.common.unit.Fuzziness
import org.elasticsearch.index.query.MultiMatchQueryBuilder
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.search.MatchQuery

data class MultiMatchData(
  var query: Any? = null,
  var fields: List<String>? = null,
  var type: String? = null,
  var operator: String? = null,
  var analyzer: String? = null,
  var boost: Float? = null,
  var slop: Int? = null,
  var fuzziness: Fuzziness? = null,
  var prefix_length: Int? = null,
  var max_expansions: Int? = null,
  var minimum_should_match: String? = null,
  var fuzzy_rewrite: String? = null,
  var tie_breaker: Float? = null,
  var lenient: Boolean? = null,
  var cutoff_frequency: Float? = null,
  var zero_terms_query: MatchQuery.ZeroTermsQuery? = null
)

private fun String.splitFieldBoost(): Pair<String, Float> {
  val parts = this.split("^", limit = 2)
  return when (parts.size) {
    1 -> parts[0] to 1.0f
    else -> parts[0] to parts[1].toFloat()
  }
}

private fun MultiMatchData.boostedFields() =
  fields!!.map { it.splitFieldBoost() }.toMap()

@Suppress("CyclomaticComplexMethod")
fun multi_match(init: MultiMatchData.() -> Unit): MultiMatchQueryBuilder {
  val params = MultiMatchData().apply(init)
  return MultiMatchQueryBuilder(params.query).apply {
    fields(params.boostedFields())
    params.type?.let { type(it) }
    params.operator?.let { operator(Operator.fromString(it)) }
    params.analyzer?.let { analyzer(it) }
    params.boost?.let { boost(it) }
    params.slop?.let { slop(it) }
    params.fuzziness?.let { fuzziness(it) }
    params.prefix_length?.let { prefixLength(it) }
    params.max_expansions?.let { maxExpansions(it) }
    params.minimum_should_match?.let { minimumShouldMatch(it) }
    params.fuzzy_rewrite?.let { fuzzyRewrite(it) }
    params.tie_breaker?.let { tieBreaker(it) }
    params.lenient?.let { lenient(it) }
    params.cutoff_frequency?.let { cutoffFrequency(it) }
    params.zero_terms_query?.let { zeroTermsQuery(it) }
  }
}
