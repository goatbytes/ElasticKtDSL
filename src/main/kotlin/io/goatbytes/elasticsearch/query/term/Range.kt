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
)

package io.goatbytes.elasticsearch.query.term

import io.goatbytes.elasticsearch.query.QueryData
import io.goatbytes.elasticsearch.query.initQuery
import org.elasticsearch.index.query.RangeQueryBuilder

class RangeBlock {
  class RangeData(
    var name: String,
    var from: Any? = null,
    var to: Any? = null,
    var include_upper: Boolean? = null,
    var include_lower: Boolean? = null,
    var format: String? = null,
    var time_zone: String? = null
  ) : QueryData() {

    var gte: Any?
      get() = this.from
      set(value) {
        from = value
        include_lower = true
      }

    var gt: Any?
      get() = this.from
      set(value) {
        from = value
        include_lower = false
      }

    var lte: Any?
      get() = this.to
      set(value) {
        to = value
        include_upper = true
      }

    var lt: Any?
      get() = this.to
      set(value) {
        to = value
        include_upper = false
      }
  }

  @Deprecated(message = "Use invoke operator instead.", replaceWith = ReplaceWith("invoke(init)"))
  infix fun String.to(init: RangeData.() -> Unit): RangeData {
    return this.invoke(init)
  }

  operator fun String.invoke(init: RangeData.() -> Unit): RangeData {
    return RangeData(name = this).apply(init)
  }
}

fun range(init: RangeBlock.() -> RangeBlock.RangeData): RangeQueryBuilder {
  val params = RangeBlock().init()
  return RangeQueryBuilder(params.name).apply {
    initQuery(params)
    params.from?.let { from(it) }
    params.to?.let { to(it) }
    params.include_lower?.let { includeLower(it) }
    params.include_upper?.let { includeUpper(it) }
    params.boost?.let { boost(it) }
    params.format?.let { format(it) }
    params.time_zone?.let { timeZone(it) }
  }
}
