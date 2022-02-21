package org.bopre.support.generator.core.utils

import java.io.Closeable

class CloseableWrap<T>(
    val value: T,
    private val closeAction: (T) -> Unit,
) : Closeable {
    override fun close() = closeAction.invoke(value)
}