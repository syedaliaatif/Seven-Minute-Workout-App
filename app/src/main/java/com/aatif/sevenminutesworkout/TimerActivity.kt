package com.aatif.sevenminutesworkout


import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.aatif.sevenminutesworkout.adapter.PaginationAdapter
import com.aatif.sevenminutesworkout.constants.Constants
import com.aatif.sevenminutesworkout.databinding.ActivityTimerBinding
import com.aatif.sevenminutesworkout.model.Exercise
import com.aatif.sevenminutesworkout.model.ExerciseStatus
import com.aatif.sevenminutesworkout.room.database.HistoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID

class TimerActivity : AppCompatActivity() {
    lateinit var binding : ActivityTimerBinding
    private var currentTimer : CountDownTimer?=null
    private var numExerciseCompleted = 0
    private var exercises = Constants.getDefaultExercises().toMutableList()
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var adapter: PaginationAdapter
    private lateinit var db :HistoryDatabase
    private lateinit var sessionUUID: UUID


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.tlbTimerPage)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.tlbTimerPage.setNavigationOnClickListener {
            onBackPressed()
        }

       db = Room.databaseBuilder(applicationContext,
            HistoryDatabase::class.java,
            "HistoryDatabase" ).build()

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelableArrayList(Constants.EXERCISE_LIST_KEY, Exercise::class.java)
                ?.let{
                    exercises = it.toMutableList()
                }
        }
        else{
            intent.extras?.getParcelableArrayList<Exercise>(Constants.EXERCISE_LIST_KEY)?.let{
                exercises = it.toMutableList()
            }
        }
        sessionUUID = UUID.randomUUID()
        setUpPagination()
        showRestView()
    }

    private fun startTimer(duration: Int,
                           onFinish: () -> Unit){
        if(currentTimer!=null){
            currentTimer?.cancel()
            currentTimer = null
        }
        currentTimer = getTimer(
            duration,
            onTick = {
                binding.pbTimer.progress = (it/1000L).toInt()
                binding.tvTimeLeft.text = (it/1000L).toString()
            },
            onFinish = onFinish
        )
        currentTimer?.start()
    }

    private fun showExerciseView(){
        val exerciseName = getCurrentExercise().name
        showImageView()
        showTitleView(exerciseName)
        showProgressView(30)
        hideUpcomingExerciseView()
        startTimer(30) {
            exercises.updateStatus(numExerciseCompleted, ExerciseStatus.COMPLETED)
            numExerciseCompleted++
            showRestView()
        }
    }

    private fun showRestView(){
        exercises.updateStatus(numExerciseCompleted,ExerciseStatus.INPROGRESS)
        playSound()
        hideImageView()
        showTitleView(getString(R.string.rest_title_text))
        showProgressView(10)
        showUpcomingExerciseView()
        startTimer(10){showExerciseView()}
    }

    private fun playSound(){
        try{
            val soundPath = "${RESOURCE_PATH}${R.raw.press_start}"
            if(mediaPlayer == null){
                mediaPlayer = MediaPlayer.create(applicationContext, soundPath.toUri()).apply {
                   isLooping = false
                }
            }
            mediaPlayer?.let{
                it.start()
            }

        }catch (error:Exception){
            error.printStackTrace()
        }
    }

    private fun hideUpcomingExerciseView(){
        binding.tvUpcomingExercise.visibility = View.GONE
    }

    private fun showUpcomingExerciseView(){
        val isAvailable = isExerciseAvailable()
        if(isAvailable){
            val exercise = getCurrentExercise()
            binding.tvUpcomingExercise.apply {
                text = buildSpannedString {
                    append(getString(R.string.upcoming_exercise_prefix))
                    append(" ")
                    bold{
                        append(exercise.name)
                    }
                }
                visibility = View.VISIBLE
            }
        }
        else {

        }
    }

    private fun showTitleView(text : String) {
        binding.tvTimerTitle.apply {
            this.text = text
        }
    }

    private fun showProgressView(timer : Int){
        binding.pbTimer.apply {
            max = timer
            progress = timer
            visibility = View.VISIBLE
        }

        binding.tvTimeLeft.apply {
            text = timer.toString()
            visibility = View.VISIBLE
        }
    }

    private fun showImageView() {
        binding.ivExercise.apply {
            getCurrentExercise().imageResource?.let{
                this.setImageDrawable(getDrawable(it))
            }
            visibility = View.VISIBLE
        }
    }

    private fun hideImageView(){
        binding.ivExercise.visibility = View.GONE
    }

    private fun getCurrentExercise(): Exercise {
        return exercises[numExerciseCompleted]
    }

    private fun isExerciseAvailable(): Boolean{
        return numExerciseCompleted < exercises.size
    }

    private fun getTimer(
        durationInSec: Int,
        onTick: (Long)->Unit,
        onFinish: ()->Unit
    ):CountDownTimer{
        return object : CountDownTimer(durationInSec * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) = onTick.invoke(millisUntilFinished)

            override fun onFinish() = onFinish.invoke()
        }
    }

    override fun onDestroy() {
        currentTimer?.cancel()
        currentTimer = null
        mediaPlayer?.apply {
            stop()
        }
        mediaPlayer = null
        super.onDestroy()
    }

    private fun setUpPagination(){
        adapter = PaginationAdapter()
        binding.rvPagination.adapter = adapter
        binding.rvPagination.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter.setContent(exercises)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun MutableList<Exercise>.updateStatus(position: Int, status:ExerciseStatus ){
        if(position == exercises.size){
            return
        }
        exercises[position] = exercises[position].copy(
            exerciseStatus = status
        )
        val uuid = UUID.randomUUID()
        val datestr = OffsetDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        if(status == ExerciseStatus.COMPLETED) {
            lifecycleScope.launch{
                withContext(Dispatchers.IO) {
                    db.historyDao().updateHistory(
                        uuid = uuid.toString(),
                        date = datestr,
                        id = exercises[position].id,
                        name = exercises[position].name,
                        imageResource = exercises[position].imageResource ?: R.drawable.ic_squat,
                        sessionUUID = sessionUUID.toString()
                    )
                }
            }
        }
        adapter.updateContent(position, exercises[position])
    }

   private companion object{
       const val RESOURCE_PATH = "android.resource://com.aatif.sevenminutesworkout/"
    }

}