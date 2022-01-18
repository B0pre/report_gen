package org.bopre.support.generator.core.utils

import java.util.*

interface ScopedStack<Scope, Value> {

    companion object {
        fun <Scope, Value> create(): ScopedStack<Scope, Value> {
            return ScopedStackBasic()
        }
    }

    /**
     * push current value for scope
     */
    fun push(scope: Scope, value: Value)

    /**
     * get current value for scope
     */
    fun top(scope: Scope): Value?

    /**
     * retrieve current value for scope
     */
    fun pop(scope: Scope): Value?

    class ScopedStackBasic<Scope, Value> : ScopedStack<Scope, Value> {

        private val currentDefaultStyleStack = HashMap<Scope, Stack<Value>>()

        override fun push(scope: Scope, value: Value) {
            setUpScope(scope)
            currentDefaultStyleStack[scope]?.push(value)
        }

        override fun top(scope: Scope): Value? {
            setUpScope(scope)
            if (currentDefaultStyleStack[scope].isNullOrEmpty())
                return null
            return currentDefaultStyleStack[scope]?.peek()
        }

        override fun pop(scope: Scope): Value? {
            setUpScope(scope)
            if (currentDefaultStyleStack[scope].isNullOrEmpty())
                return null
            return currentDefaultStyleStack[scope]?.pop()
        }

        /**
         * create stack for required scope if not exists
         */
        private fun setUpScope(scope: Scope) {
            if (!currentDefaultStyleStack.containsKey(scope)) {
                currentDefaultStyleStack[scope] = Stack()
            }
        }
    }

}