package com.blogspot.soyamr.moneymarketsimulation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.blogspot.soyamr.moneymarketsimulation.databinding.ActivityMainBinding
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val model: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    //chart vars
    val set = LineDataSet(ArrayList<Entry>(), "money rate").apply {
        valueFormatter = DefaultValueFormatter(2)
    }
    private val data = LineData(ArrayList<ILineDataSet>().apply { add(set) })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        binding.lifecycleOwner = this
        binding.viewModel = model

        setChart()

        setObservers()
    }

    private fun setObservers() {


        model.points.observe(this,
            {
                if (it.isNullOrEmpty()) {
                    removeEverything()
                } else {
                    addPoint(it.last())
                }
            })
    }


    private fun setChart() {
        chart.setTouchEnabled(true);
        chart.setPinchZoom(true);


        chart.axisRight.isEnabled = false
        chart.description.isEnabled = false


        val position = XAxisPosition.BOTTOM
        chart.xAxis.position = position



        chart.isAutoScaleMinMaxEnabled = true;
        chart.data = data
    }

    private fun addPoint(entry: Entry) {
        set.addEntry(entry)
        notifyChart()
    }

    private fun removeEverything() {
        set.clear()
        chart.fitScreen()
        notifyChart()
    }

    private fun notifyChart() {
        data.notifyDataChanged()
        chart.invalidate()
    }

}