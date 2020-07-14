package de.mtorials.dialbot.enitity

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Gif(
    val type: String,
    val id: String,
    val slug: String,
    val url: String,
    val bitly_url: String,
    val embed_url: String,
    val username: String?,
    val source: String,
    val rating: String,
    val content_url: String?,
    val title: String
)