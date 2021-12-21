package org.bopre.support.generator.core.processor.content

interface Sheet {
    fun getTitle(): String
    fun getContents(): List<Content>
}