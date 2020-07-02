package de.mtorials.dialbot.rss


import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import de.mtorials.dialbot.registerOnInterval
import de.mtorials.dialphone.DialPhone
import de.mtorials.dialphone.entities.entityfutures.RoomFuture
import de.mtorials.dialphone.sendHtmlMessage
import kotlinx.coroutines.runBlocking
import java.net.URL
import java.time.ZonedDateTime

class Rss(
    phone: DialPhone,
    private val subredditUrl: String,
    private val roomId: String,
    updateIntervalMillis: Long
) {

    private var feed: SyndFeed = SyndFeedInput().build(XmlReader(URL("$subredditUrl/new.rss")))
    private val room : RoomFuture
    private var lastTime: Long = ZonedDateTime.now().minusDays(1).toEpochSecond()

    init {
        val roomA : RoomFuture
        runBlocking {
            roomA = phone.getJoinedRoomFutureById(roomId) ?: error("Bot has not joined room with id $roomId yet!")
        }
        room = roomA
        phone.registerOnInterval(updateIntervalMillis) {
            runBlocking {
                feed = SyndFeedInput().build(XmlReader(URL("$subredditUrl/new.rss")))
                update()
            }
            true
        }
    }

    private suspend fun update() {
        val myFeed = feed.entries.filter {
            it.updatedDate.time > lastTime
        }
        if (myFeed.isNotEmpty()) {
            lastTime = myFeed[0].updatedDate.time
            sendPost(myFeed[0])
        }
    }

    private suspend fun sendPost(entry: SyndEntry) {
        room.sendHtmlMessage("<h1>${entry.title}</h1>\n")
        room.sendHtmlMessage("<i>${entry.link}</i>")
    }
}