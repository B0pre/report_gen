package org.bopre.support.generator.core.processor.render

interface ConfigurableTemplate<T> {

    interface Result<T> {
        data class Success<T>(val value: T) : Result<T>
        data class Fail<T>(val reason: String) : Result<T>
    }

    fun instance(params: Map<String, Any>): Result<T>
    fun instance(): Result<T> = instance(emptyMap())

}