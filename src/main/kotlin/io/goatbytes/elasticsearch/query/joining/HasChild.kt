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

package io.goatbytes.elasticsearch.query.joining

import org.apache.lucene.search.join.ScoreMode
import org.elasticsearch.index.query.InnerHitBuilder
import org.elasticsearch.index.query.QueryBuilder
import org.elasticsearch.join.query.HasChildQueryBuilder

data class HasChildData(
  var query: QueryBuilder? = null,
  var type: String? = null,
  var boost: Float? = null,
  var score_mode: ScoreMode = ScoreMode.None,
  var min_children: Int = HasChildQueryBuilder.DEFAULT_MIN_CHILDREN,
  var max_children: Int = HasChildQueryBuilder.DEFAULT_MAX_CHILDREN,
  var inner_hits: InnerHitBuilder? = null
) {

  fun query(f: () -> QueryBuilder) {
    query = f()
  }
}

fun has_child(init: HasChildData.() -> Unit): HasChildQueryBuilder {
  val params = HasChildData().apply(init)
  return HasChildQueryBuilder(params.type, params.query, params.score_mode).apply {
    params.boost?.let { boost(it) }
    minMaxChildren(params.min_children, params.max_children)
    params.inner_hits?.let { innerHit(it) }
  }
}
