package com.aatif.sevenminutesworkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aatif.sevenminutesworkout.adapter.ExerciseSelectionAdapter
import com.aatif.sevenminutesworkout.adapter.listener.ItemClickListener
import com.aatif.sevenminutesworkout.constants.Constants
import com.aatif.sevenminutesworkout.databinding.ActivityExerciseSelectionBinding
import com.aatif.sevenminutesworkout.model.Exercise

class ExerciseSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseSelectionBinding
    private val adapter: ExerciseSelectionAdapter by lazy {
        ExerciseSelectionAdapter(object: ItemClickListener{
            override fun onItemClick(view: View, position:Int, data:MutableList<Exercise>) {
                val radioButton = view.findViewById<RadioButton>(R.id.rb_select_exercise_toggle)
                radioButton.isChecked = !radioButton.isChecked
                data[position]= data[position].copy(isSelected = radioButton.isChecked)
            }
        })
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tlbExerciseSelectionPage)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.tlbExerciseSelectionPage.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.rvExerciseContent.adapter = adapter
        binding.rvExerciseContent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter.setContent(Constants.getDefaultExercises())
        binding.fabSubmit.setOnClickListener {
           val exerciseList =  adapter.getSelected()
            val intent = Intent(this, TimerActivity::class.java)
            intent.putParcelableArrayListExtra(Constants.EXERCISE_LIST_KEY, exerciseList as ArrayList<Exercise>)
            startActivity(intent)
        }
    }
}