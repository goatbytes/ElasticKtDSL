package io.goatbytes.elasticsearch

import org.elasticsearch.common.Strings
import org.elasticsearch.index.query.QueryBuilder

/**
 * Convert the query into the json representation.
 *
 * @receiver The ElasticSearch Query Builder object
 * @return The query as a JSON string.
 */
fun QueryBuilder.json() = Strings.toString(this)
