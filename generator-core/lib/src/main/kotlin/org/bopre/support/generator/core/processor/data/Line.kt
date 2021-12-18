package org.bopre.support.generator.core.processor.data

interface Line : Iterable<String> {
    fun getCell(name: String): String

    companion object {
        fun fromMap(mm: Map<String, String>): Line = object : Line {
            override fun getCell(name: String): String = mm.getOrDefault(name, "")
            override fun iterator(): Iterator<String> {
                return mm.keys.sorted()
                    .map { getCell(it) }
                    .iterator()
            }
        }
    }
}