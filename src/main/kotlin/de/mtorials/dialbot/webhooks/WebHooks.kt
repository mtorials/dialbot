package de.mtorials.dialbot.webhooks

import de.mtorials.dialbot.config
import de.mtorials.dialbot.logger
import de.mtorials.dialphone.DialPhone
import de.mtorials.dialphone.entities.entityfutures.RoomFuture
import de.mtorials.dialphone.sendHtmlMessage
import de.mtorials.dialphone.sendTextMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.http4k.core.*
import org.http4k.lens.Query
import org.http4k.lens.string
import org.http4k.routing.bind
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer

fun startWebhooks(phone: DialPhone) {

    val bodyLens = Body.string(ContentType.TEXT_PLAIN).toLens()
    val queryLens = Query.optional("roomId")
    val authLens = Query.optional("token")

    val auth = Filter { next ->
        block@{ request ->
            if (authLens(request) != config.webhooks.token) return@block Response(Status.UNAUTHORIZED)
            next(request)
        }
    }

    val handlerText : HttpHandler = block@{ request ->
        logger.info { "Text Webhook ${bodyLens(request)}" }
        val roomId: String = queryLens(request) ?: return@block Response(Status.BAD_REQUEST).body("no room id")
        val mayRoom: RoomFuture?
        runBlocking { mayRoom = phone.getJoinedRoomFutureById(roomId) }
        val room: RoomFuture = mayRoom ?: return@block Response(Status.NOT_FOUND).body("cant find room")
        runBlocking { room.sendTextMessage(bodyLens(request)) }
        Response(Status.OK)
    }

    val handlerHtml : HttpHandler = block@{ request ->
        logger.info { "Html Webhook ${bodyLens(request)}" }
        val roomId: String = queryLens(request) ?: return@block Response(Status.BAD_REQUEST).body("no room id")
        val mayRoom: RoomFuture?
        runBlocking { mayRoom = phone.getJoinedRoomFutureById(roomId) }
        val room: RoomFuture = mayRoom ?: return@block Response(Status.NOT_FOUND).body("cant find room")
        GlobalScope.launch { room.sendHtmlMessage(bodyLens(request)) }
        Response(Status.OK)
    }

    val app = auth.then(routes(
        "sendText" bind Method.POST to handlerText,
        "sendHtml" bind Method.POST to handlerHtml,
        "ping" bind Method.GET to {
            Response(Status.OK).body("pong!")
        }
    ))

    logger.info("starting server...")
    app.asServer(Jetty(config.port)).start()
}