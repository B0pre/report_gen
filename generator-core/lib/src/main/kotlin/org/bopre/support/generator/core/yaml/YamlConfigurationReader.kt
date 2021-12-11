package org.bopre.support.generator.core.yaml

import org.bopre.support.generator.core.yaml.data.Document

interface YamlConfigurationReader {
    fun readDocument(input: String): Document
}