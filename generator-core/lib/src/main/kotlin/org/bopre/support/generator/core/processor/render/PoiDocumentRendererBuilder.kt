package org.bopre.support.generator.core.processor.render

import org.bopre.support.generator.core.processor.data.LineSource
import org.bopre.support.generator.core.processor.content.Sheet
import java.util.*

class PoiDocumentRendererBuilder(
    private val registeredContentHandlers: List<ContentHandler> = getDefaultHandlers()
) {

    private val registeredSources = HashMap<String, LineSource>()
    private val registeredSheets = LinkedList<Sheet>()

    fun externalSource(sourceId: String, someSource: LineSource): PoiDocumentRendererBuilder = with(this) {
        registeredSources[sourceId] = someSource
        return this
    }

    fun appendSheet(sheet: Sheet): PoiDocumentRendererBuilder = with(this) {
        registeredSheets.add(sheet)
        return this
    }

    fun build(): PoiDocumentRenderer {
        val sheets = LinkedList(registeredSheets)
        val handlers = LinkedList(registeredContentHandlers)
        val settings = createSettings()

        return PoiDocumentRenderer(
            sheets = sheets,
            handlers = handlers,
            settings = settings
        )
    }

    private fun createSettings(): DocumentSettings {
        val externalSources = HashMap(registeredSources)
        return object : DocumentSettings {
            override fun getSource(sourceId: String): LineSource =
                externalSources[sourceId] ?: LineSource.static(emptyList())
        }
    }

    companion object {
        private fun getDefaultHandlers(): List<ContentHandler> {
            return listOf(
                SeparatorHandler(), TableHandler()
            )
        }
    }

}
