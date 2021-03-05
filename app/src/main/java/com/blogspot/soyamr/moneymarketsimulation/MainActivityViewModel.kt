package com.blogspot.soyamr.moneymarketsimulation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val _points: MutableLiveData<ArrayList<Entry>> = MutableLiveData(ArrayList())
    val points: LiveData<ArrayList<Entry>> = _points

    private val moneyModerator = MoneyModerator()
    private val myAccount = MyAccount()

    val myMoneyInDollars: MutableLiveData<String> = MutableLiveData("1000")
    val exchangePrice: MutableLiveData<String> = MutableLiveData("${moneyModerator.price}")
    val myMoneyInRubles: MutableLiveData<String> = MutableLiveData("0")

    val buttonsEnabled: MutableLiveData<Boolean> = MutableLiveData(true)

    val dollarErrorText: MutableLiveData<String> = MutableLiveData("")
    val rublesErrorText: MutableLiveData<String> = MutableLiveData("")

    val days: MutableLiveData<Int> = MutableLiveData(0)


    init {
        updateChart()
    }

    fun buy() {
        if (checkUserBalance(Operation.BUY)) {
            clearErrors()
            viewModelScope.launch {
                buttonsEnabled.value = false

                myAccount.buy(exchangePrice.value!!.toDouble())

                getAccountInfo()


                buttonsEnabled.value = true
            }
        }
    }

    private fun getAccountInfo() {
        myMoneyInDollars.value = "${myAccount.myMoneyInDollars}"
        myMoneyInRubles.value = "${myAccount.myMoneyInRubles}"
    }

    private val moneyPerOperation = 1.0
    fun sell() {
        if (checkUserBalance(Operation.SELL)) {
            clearErrors()
            viewModelScope.launch {
                buttonsEnabled.value = false

                myAccount.sell(exchangePrice.value!!.toDouble())

                getAccountInfo()

                buttonsEnabled.value = true
            }
        }
    }

    fun nextDay() {
        clearErrors()
        viewModelScope.launch {
            buttonsEnabled.value = false

            moneyModerator.update()
            getBankInfo()
            updateChart()

            buttonsEnabled.value = true
        }

    }

    private fun getBankInfo() {
        days.value = moneyModerator.days.toInt()
        exchangePrice.value = "${moneyModerator.price}"
    }

    private fun updateChart() {
        _points.value?.add(Entry(days.value!!.toFloat(), exchangePrice.value!!.toFloat()))
        _points.value = _points.value
    }

    private fun clearErrors() {
        rublesErrorText.value = ""
        dollarErrorText.value = ""
    }

    fun reset() {
        moneyModerator.reset()
        myAccount.reset()
        getAccountInfo()
        getBankInfo()
        _points.value?.clear()
        _points.value = _points.value
        updateChart()
    }

    private fun checkUserBalance(operation: Operation): Boolean {
        when (operation) {
            Operation.BUY -> {
                return if (myMoneyInDollars.value!!.toDouble() < moneyPerOperation) {
                    dollarErrorText.value = "not enough money"
                    false
                } else {
                    dollarErrorText.value = ""
                    true
                }
            }
            Operation.SELL -> {
                return if (
                    myMoneyInRubles.value!!.toDouble() < (moneyPerOperation * exchangePrice.value!!.toDouble())) {
                    rublesErrorText.value = "not enough money"
                    false
                } else {
                    rublesErrorText.value = ""
                    true
                }
            }
        }
    }
}