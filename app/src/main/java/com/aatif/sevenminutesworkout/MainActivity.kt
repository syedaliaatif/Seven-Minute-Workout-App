package com.aatif.sevenminutesworkout

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.aatif.sevenminutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private var startTimer: ImageButton? = null
    private var startTimerFrameLayout: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startTimer = binding.ibStartTimer
        startTimerFrameLayout = binding.flStartTimer
        startTimer?.setOnClickListener {
            startTimer?.background = getDrawable(R.drawable.ui__start_timer_background_clicked)
            startActivity(Intent(this, ExerciseSelectionActivity::class.java))
        }
        binding.btnBmiCalculator.setOnClickListener {
            startActivity(Intent(this, BMIActivity::class.java))
        }
        binding.btnHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        startTimer?.background = getDrawable(R.drawable.ui__start_timer_background)
    }
}