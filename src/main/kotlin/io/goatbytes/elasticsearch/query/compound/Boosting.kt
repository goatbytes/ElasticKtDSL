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
  "VariableNaming",
  "MatchingDeclarationName"
)

package io.goatbytes.elasticsearch.query.compound

import org.elasticsearch.index.query.BoostingQueryBuilder
import org.elasticsearch.index.query.QueryBuilder

data class BoostingData(
  var positive: QueryBuilder? = null,
  var negative: QueryBuilder? = null,
  var negative_boost: Float? = null
) {

  fun positive(f: () -> QueryBuilder) {
    positive = f()
  }

  fun negative(f: () -> QueryBuilder) {
    negative = f()
  }
}

fun boosting(init: BoostingData.() -> Unit): BoostingQueryBuilder {
  val params = BoostingData().apply(init)
  return BoostingQueryBuilder(params.positive, params.negative).apply {
    params.negative_boost?.let { negativeBoost(it) }
  }
}
