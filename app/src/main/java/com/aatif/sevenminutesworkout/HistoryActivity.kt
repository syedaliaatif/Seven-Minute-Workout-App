package com.aatif.sevenminutesworkout


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.aatif.sevenminutesworkout.adapter.HistoryAdapter
import com.aatif.sevenminutesworkout.constants.Constants
import com.aatif.sevenminutesworkout.databinding.ActivityHistoryBinding
import com.aatif.sevenminutesworkout.model.Exercise
import com.aatif.sevenminutesworkout.room.database.HistoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity(), HistoryAdapter.HistoryAdapterListener {

    private lateinit var binding:ActivityHistoryBinding
    private var currentState = State.BY_EXERCISE
    private lateinit var db:HistoryDatabase
    private val adapter= HistoryAdapter(this)
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tlbHistoryPage)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.tlbHistoryPage.setNavigationOnClickListener {
            onBackPressed()
        }
        binding.tvByExercise.setOnClickListener{
            currentState = State.BY_EXERCISE
            setAnchor(currentState)
            binding.tvByExercise.setBackgroundColor(Color.BLACK)
            binding.tvBySet.setBackgroundColor(getColor(R.color.grey_history_page))
            binding.tvByExercise.setTextColor(Color.WHITE)
            binding.tvBySet.setTextColor(Color.BLACK)
            adapter.notifyDataSetChanged()
        }
        binding.tvBySet.setOnClickListener {
            currentState = State.BY_SET
            setAnchor(currentState)
            binding.tvBySet.setBackgroundColor(Color.BLACK)
            binding.tvByExercise.setBackgroundColor(getColor(R.color.grey_history_page))
            binding.tvBySet.setTextColor(Color.WHITE)
            binding.tvByExercise.setTextColor(Color.BLACK)
            adapter.notifyDataSetChanged()
        }

        binding.flHeader.doOnLayout {
            setAnchor(currentState)
        }
        db =
            Room.databaseBuilder(this,
                HistoryDatabase::class.java,
                "HistoryDatabase" ).build()
        setRecyclerViewContent()

    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }


    private fun setAnchor(state:State){
        binding.ivAnchor.visibility = View.INVISIBLE
        val pairOfXAndY = getTranslationsRequired(state)
        binding.ivAnchor.translationX = pairOfXAndY.first.toFloat()
        binding.ivAnchor.translationY = pairOfXAndY.second.toFloat()
        binding.ivAnchor.visibility = View.VISIBLE
    }

    private fun getTranslationsRequired(state: State): Pair<Double, Double>{

        val headerHeight = binding.llHeader.height
        val headerWidth = binding.llHeader.width
        val anchorHeight = binding.ivAnchor.height
        val anchorWidth = binding.ivAnchor.width
        var translationX:Double = 0.0
        var translationY:Double = 0.0
         when(state){
            State.BY_EXERCISE ->{
                translationX = headerWidth/4.0 - anchorWidth/2.0
                translationY = (headerHeight - anchorHeight).toDouble()
            }
             State.BY_SET -> {
                 translationX = headerWidth*(3.0/4.0) - anchorWidth/2.0
                 translationY = (headerHeight - anchorHeight).toDouble()
             }
        }
        return Pair(translationX, translationY)
    }

    private fun setRecyclerViewContent() {
        binding.rvHistory.adapter = adapter
        binding.rvHistory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        refreshData()
    }

    private fun refreshData(){
        lifecycleScope.launch{
             withContext(Dispatchers.IO) {
                 val history = db.historyDao().getCompleteHistory()
                 withContext(Dispatchers.Main){
                     adapter.setContent(history)
                 }
             }
        }
    }

    override fun onClick(data:List<Exercise>) {
        val intent = Intent(this, TimerActivity::class.java)
        intent.putParcelableArrayListExtra(
            Constants.EXERCISE_LIST_KEY,
            ArrayList(data)
        )
        startActivity(intent)
    }

    override fun getStatus(): State {
        return currentState
    }

    enum class State{
        BY_EXERCISE,
        BY_SET
    }
}