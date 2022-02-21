package org.bopre.support.generator.core.processor.data

import java.io.Closeable
import java.io.IOException

interface CloseableIterable<T> : Iterable<T>, Closeable {
    companion object {
        fun <T> fromIterable(iterable: Iterable<T>): CloseableIterable<T> = object : CloseableIterable<T> {
            override fun iterator(): Iterator<T> =
                iterable.iterator()
        }
    }

    @Throws(IOException::class)
    override fun close() {}
}