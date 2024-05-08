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

package io.goatbytes.elasticsearch.aggregation

import org.elasticsearch.search.aggregations.AggregationBuilder
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder

data class NestedAggregationData(
  var name: String? = null,
  var path: String? = null,
  var sub_aggregation: List<AggregationBuilder>? = null
) {
  fun sub_aggregation(f: () -> AggregationBuilder) {
    sub_aggregation = listOf(f())
  }
}

fun nested_aggregation(init: NestedAggregationData.() -> Unit): NestedAggregationBuilder {
  val params = NestedAggregationData().apply(init)
  return NestedAggregationBuilder(params.name, params.path).apply {
    params.sub_aggregation?.forEach {
      subAggregation(it)
    }
  }
}
