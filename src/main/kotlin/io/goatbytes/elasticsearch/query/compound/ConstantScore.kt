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
  "FunctionNaming"
)

package io.goatbytes.elasticsearch.query.compound

import org.elasticsearch.index.query.ConstantScoreQueryBuilder
import org.elasticsearch.index.query.QueryBuilder

data class ConstantScoreData(
  var filter: QueryBuilder? = null,
  var boost: Float? = null
) {
  fun filter(f: () -> QueryBuilder) {
    filter = f()
  }
}

fun constant_score(init: ConstantScoreData.() -> Unit): ConstantScoreQueryBuilder {
  val params = ConstantScoreData().apply(init)
  return ConstantScoreQueryBuilder(params.filter).apply {
    params.boost?.let { boost(it) }
  }
}
