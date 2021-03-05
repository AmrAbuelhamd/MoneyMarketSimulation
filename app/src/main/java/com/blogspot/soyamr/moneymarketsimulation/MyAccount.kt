package com.blogspot.soyamr.moneymarketsimulation

class MyAccount {
    var myMoneyInDollars = 1000.0
        private set
    var myMoneyInRubles = 0.0
        private set

    private val moneyPerOperation = 1.0

    fun buy(exchangeRate: Double) {
        myMoneyInDollars -= moneyPerOperation
        myMoneyInRubles += moneyPerOperation * exchangeRate
    }

    fun sell(exchangeRate: Double) {
        myMoneyInDollars += moneyPerOperation
        myMoneyInRubles -= moneyPerOperation * exchangeRate
    }

    fun reset() {
        myMoneyInDollars = 1000.0
        myMoneyInRubles = 0.0
    }
}