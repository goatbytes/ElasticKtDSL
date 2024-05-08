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

package io.goatbytes.elasticsearch.query.fulltext

import io.goatbytes.elasticsearch.query.fulltext.MatchPhrasePrefixBlock.MatchPhrasePrefixData
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder

/**
 * match_phrase_prefix
 */
class MatchPhrasePrefixBlock {

  @Deprecated(
    message = "Use invoke operator instead.",
    replaceWith = ReplaceWith("invoke(init)")
  )
  infix fun String.to(init: MatchPhrasePrefixData.() -> Unit) = this.invoke(init)

  operator fun String.invoke(init: MatchPhrasePrefixData.() -> Unit) =
    MatchPhrasePrefixData(name = this).apply(init)

  infix fun String.to(query: Any) = MatchPhrasePrefixData(name = this, query = query)

  data class MatchPhrasePrefixData(
    var name: String,
    var query: Any? = null,
    var analyzer: String? = null,
    var slop: Int? = null,
    var max_expansions: Int? = null
  )
}

fun match_phrase_prefix(
  init: MatchPhrasePrefixBlock.() -> MatchPhrasePrefixData
): MatchPhrasePrefixQueryBuilder {
  val params = MatchPhrasePrefixBlock().init()
  return MatchPhrasePrefixQueryBuilder(params.name, params.query).apply {
    params.analyzer?.let { analyzer(it) }
    params.slop?.let { slop(it) }
    params.max_expansions?.let { maxExpansions(it) }
  }
}
