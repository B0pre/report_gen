package org.bopre.support.generator.core.processor.content

interface TableContent : Content {

    fun getColumns(): List<TableColumn>
    fun getSourceId(): String
}