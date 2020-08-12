package de.mtorials.dialbot.commands

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import de.mtorials.dialbot.enitity.Gif
import de.mtorials.dialphone.dialevents.MessageReceivedEvent
import de.mtorials.dialphone.dialevents.answer
import de.mtorials.dialphone.listener.CommandAdapter
import org.http4k.client.JavaHttpClient
import org.http4k.client.OkHttp
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request

class GifCommand(
    private val apiKey: String,
    private val client: HttpHandler = OkHttp()
) : CommandAdapter("gif") {
    override suspend fun execute(event: MessageReceivedEvent, parameters: Array<String>) {
        event answer search(parameters[0])[0].url
    }

    private fun search(q: String, limit: Int = 25) : Array<Gif> {
        val req = Request(Method.GET, "https://$searchUrl")
            .query("api_key", apiKey)
            .query("q", q)
            .query("limit", limit.toString())
        println(req)
        return jacksonObjectMapper().readValue<APIAnswer<Array<Gif>>>(client(req).bodyString()).data
    }

    companion object {
        const val searchUrl = "api.giphy.com/v1/gifs/search"
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    class APIAnswer<T>(
        val data: T
    )
}