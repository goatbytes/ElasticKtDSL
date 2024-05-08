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

import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilder

data class BoolData(
  var must: MutableList<QueryBuilder> = mutableListOf(),
  var filter: MutableList<QueryBuilder> = mutableListOf(),
  var must_not: MutableList<QueryBuilder> = mutableListOf(),
  var should: MutableList<QueryBuilder> = mutableListOf(),
  var minimum_should_match: Int? = null,
  var boost: Float? = null
) {

  fun must(f: () -> QueryBuilder) {
    must.add(f())
  }

  fun must_not(f: () -> QueryBuilder) {
    must_not.add(f())
  }

  fun filter(f: () -> QueryBuilder) {
    filter.add(f())
  }

  fun should(f: () -> QueryBuilder) {
    should.add(f())
  }
}

fun bool(init: BoolData.() -> Unit): BoolQueryBuilder {
  val params = BoolData().apply(init)
  return BoolQueryBuilder().apply {
    params.must.forEach { must(it) }
    params.filter.forEach { filter(it) }
    params.must_not.forEach { mustNot(it) }
    params.should.forEach { should(it) }
    params.minimum_should_match?.let { minimumShouldMatch(it) }
    params.boost?.let { boost(it) }
  }
}
