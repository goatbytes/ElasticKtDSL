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

package io.goatbytes.elasticsearch.query.compound

import org.elasticsearch.common.lucene.search.function.CombineFunction
import org.elasticsearch.common.lucene.search.function.FunctionScoreQuery
import org.elasticsearch.index.query.MatchAllQueryBuilder
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder

data class FunctionScoreData(
  var query: QueryBuilder = MatchAllQueryBuilder(),
  var boost: Float? = null,
  var functions: List<Pair<QueryBuilder?, ScoreFunctionBuilder<*>>> = emptyList(),
  var field_value_factor: FieldValueFactorFunctionBuilder? = null,
  var max_boost: Float? = null,
  var score_mode: String? = null,
  var boost_mode: String? = null,
  var min_score: Float? = null
)

fun function_score(init: FunctionScoreData.() -> Unit): FunctionScoreQueryBuilder {
  val params = FunctionScoreData().apply(init)
  val factorWrapper =
    listOf(Pair(null as QueryBuilder?, params.field_value_factor as ScoreFunctionBuilder<*>?))
  val merged = factorWrapper + params.functions

  val filterFunctions = merged.filter { it.second != null }.map {
    if (it.first == null) {
      FunctionScoreQueryBuilder.FilterFunctionBuilder(it.second)
    } else {
      FunctionScoreQueryBuilder.FilterFunctionBuilder(it.first, it.second)
    }
  }

  val builder = FunctionScoreQueryBuilder(params.query, filterFunctions.toTypedArray())

  return builder.apply {
    params.boost?.let { boost(it) }
    params.max_boost?.let { maxBoost(it) }
    params.score_mode?.let { scoreMode(FunctionScoreQuery.ScoreMode.fromString(it)) }
    params.boost_mode?.let { boostMode(CombineFunction.fromString(it)) }
    params.min_score?.let { setMinScore(it) }
  }
}
