package eu.codlab.lorcana.api.backend.routing

import eu.codlab.files.RootPath
import eu.codlab.files.VirtualFile
import eu.codlab.google.maps.Session
import eu.codlab.lorcana.api.utils.trySentry
import eu.codlab.lorcana.config.Config
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.routeMap() {
    val cacheFolder = VirtualFile(RootPath, ".map/tiles")
    val session = Session(Config.apiLorcanaGoogleMaps)

    get("/map/tile/{zoomLevel}/{col}/{row}") {
        trySentry(
            onError = {
                call.respond(HttpStatusCode.NotFound)
            }
        ) {
            if (!cacheFolder.exists()) {
                cacheFolder.mkdirs()
            }

            val zoomLevel = this.call.parameters["zoomLevel"]!!.toInt()
            val col = this.call.parameters["col"]!!.toInt()
            val row = this.call.parameters["row"]!!.toInt()

            val file = VirtualFile(cacheFolder, "${row}_${col}_$zoomLevel.jpeg")

            if (file.exists()) {
                val content = file.read()
                if (content.isNotEmpty()) {
                    call.respondBytes(content, ContentType.Image.JPEG)
                    return@trySentry
                }
            }

            val tile = session.getTile(
                zoomLevel = zoomLevel,
                col = col,
                row = row
            )

            file.write(tile)

            call.respondBytes(tile, ContentType.Image.JPEG)
        }
    }
}
