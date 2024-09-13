package eu.codlab.lorcana.api.assets

import eu.codlab.files.VirtualFile

/**
 * Helper to extract assets from the binary, currently not used but will be soon
 */
object Assets {
    val files: List<String> = listOf(
        /**
         * whenever it's required
         */
    )

    suspend fun load() {
        files.forEach {
            val bytes = getResourceByteArray("/$it")
            val assets = VirtualFile(VirtualFile.Root, "assets")
            assets.mkdirs()
            VirtualFile(assets, it).write(bytes)
        }
    }

    private fun getResourceByteArray(filename: String) =
        this::class.java.getResourceAsStream(filename).use { it?.readBytes() } ?: ByteArray(0)
}
