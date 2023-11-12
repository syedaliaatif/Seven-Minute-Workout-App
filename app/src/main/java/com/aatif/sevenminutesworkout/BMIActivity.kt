package com.aatif.sevenminutesworkout

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.underline
import com.aatif.sevenminutesworkout.databinding.ActivityBmiBinding

class BMIActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBmiBinding

    private var curState = RadioState.STANDARD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tlbBmiPage)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.tlbBmiPage.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.btnCalculate.setOnClickListener {
            calculateAndPresentBMI()
        }

        binding.btnActive.setOnClickListener {
            curState = RadioState.getOpposite(curState)
            binding.btnNotActive.text = binding.btnActive.text
            binding.btnActive.apply {
                text = curState.value
            }
            updateEditTextHints()
        }

    }

    private fun calculateAndPresentBMI(){
        if (binding.editTextHeight.text.isNullOrBlank() or binding.editTextWeight.text.isNullOrBlank()) {
            Toast.makeText(this, "Enter valid height and weight.", Toast.LENGTH_SHORT).show()
            return
        }

        val weight = curState.getWeight(binding.editTextWeight.text.toString().toDouble())
        val height = curState.getHeight(binding.editTextHeight.text.toString().toDouble())
        val bmi = weight / (height * height)

        showBMIResult(bmi)
    }

    private fun showBMIResult(bmi: Double){
        val roundedBmi = Math.round(bmi*100.0)/ 100.0
        val bmiResultText = buildSpannedString {
            append(getString(R.string.bmi_result_prefix))
            append(" ")
            underline {  bold{ append(roundedBmi.toString())}}
        }

        binding.tvBmiResult.text = bmiResultText
        binding.tvBmiResult.visibility = View.VISIBLE
    }

    private fun updateEditTextHints(){
        when(curState){
            RadioState.STANDARD ->{
                binding.tilHeight.setHint(R.string.height_hint_in_meters)
                binding.tilWeight.setHint(R.string.weight_hint_in_kgs)
            }
            RadioState.US -> {
                binding.tilHeight.setHint(R.string.height_hint_in_feets)
                binding.tilWeight.setHint(R.string.weight_hint_in_pounds)
            }
        }
    }

    private enum class RadioState(val value:String){
        STANDARD("Std. Metrics")
        {
            override fun getWeight(weight:Double):Double = weight
            override fun getHeight(height:Double):Double = height
        },
        US("US Metrics"){
            override fun getWeight(weight: Double):Double = weight * POUND_TO_KG_MULTIPLIER
            override fun getHeight(height: Double):Double = height * FEET_TO_METER_MULTIPLIER
        };

        abstract fun getWeight(weight:Double):Double
        abstract fun getHeight(height: Double):Double

       companion object {
           fun getOpposite(curState: RadioState): RadioState {
               return when (curState) {
                   STANDARD -> US
                   US -> STANDARD
               }
           }

           private const val POUND_TO_KG_MULTIPLIER = 0.453592
           private const val FEET_TO_METER_MULTIPLIER = 0.3048
       }
    }
}
