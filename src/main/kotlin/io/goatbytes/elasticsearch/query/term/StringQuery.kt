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
import org.elasticsearch.index.query.Operator
import org.elasticsearch.index.query.QueryStringQueryBuilder

class StringBlock {
  class StringData(
    var searchText: String,
    var field: String,
    var defaultOperator: Operator = Operator.AND
  ) : QueryData()

  infix fun String.startsWith(searchText: String): StringData = StringData("$searchText*", this)
  infix fun String.endsWith(searchText: String): StringData = StringData("*$searchText", this)
  infix fun String.equalsTo(searchText: String): StringData = StringData(searchText, this)
  infix fun String.contains(searchText: String): StringData = StringData("*$searchText*", this)
}

fun string(init: StringBlock.() -> StringBlock.StringData): QueryStringQueryBuilder {
  val params = StringBlock().init()
  return QueryStringQueryBuilder(params.searchText).field(params.field).analyzeWildcard(true)
    .apply {
      defaultOperator(params.defaultOperator)
      initQuery(params)
    }
}
