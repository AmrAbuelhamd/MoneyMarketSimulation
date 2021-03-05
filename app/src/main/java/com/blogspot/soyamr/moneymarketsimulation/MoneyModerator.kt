package com.blogspot.soyamr.moneymarketsimulation

import kotlin.random.Random

class MoneyModerator {
    private val k = 0.02

    var price = 73.24
        private set

    var days = 0.0
        private set


    private val random = Random
    fun update(): Double {
        price *= (1.0 + k * (random.nextDouble() - 0.5));
        ++days
        return price
    }

    fun reset() {
        price = 73.24
        days = 0.0
    }

}