package org.bopre.support.generator.core.processor.content.impl

import org.bopre.support.generator.core.processor.content.TableColumn
import org.bopre.support.generator.core.processor.data.Line

class SimpleTableColumn(
    private val title: String,
    private val id: String,
    private val styleId: String = "",
    private val headerStyleId: String = "",
    private val width: Int? = null,
    private val height: Int? = null
) : TableColumn {

    override fun getTitle(): String {
        return title
    }

    override fun getStyleId(): String = styleId

    override fun getHeaderStyleId(): String = headerStyleId

    override fun getValue(source: Line): Any =
        source.getCell(id)

    override fun getWidth(): Int? = width

    override fun getHeight(): Int? = height

    class SimpleTableColumnBuilder(
        private var title: String,
        private var id: String,
        private var styleId: String = "",
        private var headerStyleId: String = ""
    ) {

        fun title(title: String): SimpleTableColumnBuilder {
            this.title = title
            return this
        }

        fun id(id: String): SimpleTableColumnBuilder {
            this.id = id
            return this
        }

        fun styleId(styleId: String): SimpleTableColumnBuilder {
            this.styleId = styleId
            return this
        }

        fun headerStyleId(headerStyleId: String): SimpleTableColumnBuilder {
            this.headerStyleId = headerStyleId
            return this
        }

        fun build(): SimpleTableColumn {
            return SimpleTableColumn(
                title = title,
                id = id,
                styleId = styleId,
                headerStyleId = headerStyleId
            )
        }
    }

}
