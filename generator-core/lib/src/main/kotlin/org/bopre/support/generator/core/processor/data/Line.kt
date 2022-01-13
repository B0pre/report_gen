package org.bopre.support.generator.core.processor.data

interface Line : Iterable<Any> {
    fun getCell(name: String): Any

    companion object {
        fun fromMap(mm: Map<String, Any>): Line = object : Line {
            override fun getCell(name: String): Any = mm.getOrDefault(name, "")
            override fun iterator(): Iterator<Any> {
                return mm.keys.sorted()
                    .map { getCell(it) }
                    .iterator()
            }
        }
    }
}