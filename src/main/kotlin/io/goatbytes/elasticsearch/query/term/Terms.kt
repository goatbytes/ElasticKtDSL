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
import org.elasticsearch.index.query.TermsQueryBuilder
import org.elasticsearch.indices.TermsLookup

class TermsBlock {
  class TermsData(
    var name: String,
    var values: List<Any> = emptyList(),
    var termsLookup: TermsLookup? = null
  ) : QueryData()

  infix fun String.to(values: List<Any>): TermsData {
    return TermsData(name = this, values = values)
  }

  infix fun String.to(lookup: TermsLookup): TermsData {
    return TermsData(name = this, termsLookup = lookup)
  }

  class TermsLookupData(
    var index: String? = null,
    var type: String? = null,
    var id: String? = null,
    var path: String? = null
  )

  operator fun String.invoke(init: TermsLookupData.() -> Unit): TermsData {
    val lookup = TermsLookupData().run {
      init()
      TermsLookup(index, type, id, path)
    }
    return TermsData(name = this, termsLookup = lookup)
  }
}

fun terms(init: TermsBlock.() -> TermsBlock.TermsData): TermsQueryBuilder {
  val params = TermsBlock().init()
  return when {
    params.termsLookup is TermsLookup ->
      TermsQueryBuilder(params.name, params.termsLookup)

    else ->
      TermsQueryBuilder(params.name, params.values)
  }
    .apply {
      initQuery(params)
    }
}
