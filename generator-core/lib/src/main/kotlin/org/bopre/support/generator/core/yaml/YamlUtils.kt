package org.bopre.support.generator.core.yaml


fun isVariableParameter(value: String): Boolean {
    return Regex("^\\\$\\{.*}\$").matches(value)
}

fun removeSymbols(value: String): String {
    return value.removePrefix("\${").removeSuffix("}")
}
