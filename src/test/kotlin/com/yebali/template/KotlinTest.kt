package com.yebali.template

import org.junit.jupiter.api.Test

class KotlinTest {
    @Test
    fun equalsList() {
        val A = listOf(
            1 to "a",
            2 to "b",
            3 to "c",
        )

        val B = listOf(
            1 to "a",
            2 to "b",
            3 to "c",
        )

        val C = listOf(
            3 to "c",
            2 to "b",
            1 to "a",
        )

        println("A == B : ${A == B}")
        println("A == C : ${A == C}")
    }
}
