package com.aatif.sevenminutesworkout.adapter

import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aatif.sevenminutesworkout.HistoryActivity
import com.aatif.sevenminutesworkout.R
import com.aatif.sevenminutesworkout.model.Exercise
import com.aatif.sevenminutesworkout.room.entities.History
import com.google.android.material.card.MaterialCardView

class HistoryAdapter(private val listener:HistoryAdapterListener): Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var history: MutableList<History> = mutableListOf()
    private var historyBySet: MutableList<List<History>> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }


    override fun getItemCount(): Int {
        return if(listener.getStatus() == HistoryActivity.State.BY_EXERCISE) {
            history.size
        } else {
            historyBySet.size
        }

    }
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        if(listener.getStatus() == HistoryActivity.State.BY_EXERCISE)
        {
            holder.bindHistory(history[position], listener)
        }
        else {
            holder.bindSet(historyBySet[position], listener)
        }
    }

    fun setContent(content:List<History>){
        history = mutableListOf<History>().also {
            it.addAll(content)
        }
        historyBySet = mutableListOf()
        val map = mutableMapOf<String, MutableList<History>>()
        var iterator = history.iterator()
        while(iterator.hasNext()){
            val it = iterator.next()
            map[it.sessionUUID]?.let {list ->
                list.add(it)
            }?:run{
                map[it.sessionUUID] = mutableListOf(it)
            }
        }
        map.forEach { (_, listOfHistory) ->  historyBySet.add(listOfHistory) }
        notifyDataSetChanged()
    }

    class HistoryViewHolder(itemView: View):ViewHolder(itemView){

        private val image: ImageView by lazy { itemView.findViewById(R.id.iv_history) }
        private val title: TextView by lazy{itemView.findViewById(R.id.tv_history_exercise_name)}
        private val date : TextView by lazy{itemView.findViewById(R.id.tv_history_date)}
        private val duration:TextView by lazy{itemView.findViewById(R.id.tv_history_duration)}
        private val btn: ImageView by lazy{itemView.findViewById(R.id.ib_restart)}
        private val byExerciseContainer: MaterialCardView by lazy { itemView.findViewById(R.id.by_exercise_content) }
        private val bySetContainer: FrameLayout by lazy{itemView.findViewById(R.id.by_set_content)}
        private val textContentBySet: LinearLayout by lazy{itemView.findViewById(R.id.ll_text_content)}
        private val btnBySet: ImageView by lazy{itemView.findViewById(R.id.iv_restart_set)}
        private val dateBySet:TextView by lazy{itemView.findViewById(R.id.tv_history_by_set_date)}
        fun bindHistory( historyItem: History, listener: HistoryAdapterListener){
                bySetContainer.visibility = View.GONE
                image.setImageResource(historyItem.imageResource)
                title.text = historyItem.name
                date.text = historyItem.datestr
                duration.text = "Duration: 30 seconds"
                btn.setOnClickListener {
                    val list = listOf(
                        Exercise(
                            id = historyItem.id,
                            name = historyItem.name,
                            imageResource = historyItem.imageResource
                        )
                    )
                    listener.onClick(list)
                }
                byExerciseContainer.visibility = View.VISIBLE
        }

        fun bindSet(set: List<History>, listener: HistoryAdapterListener){
            byExerciseContainer.visibility = View.GONE
            textContentBySet.removeAllViews()
            set.forEachIndexed { index, it ->
                val text = TextView(textContentBySet.context)
                val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4F,textContentBySet.context.resources.displayMetrics ).toInt()
                text.setPadding(px, px, px, px)
                text.text = "${index+1}. ${it.name}"
                text.gravity = Gravity.CENTER_VERTICAL
                text.setTextAppearance(com.google.android.material.R.style.TextAppearance_Material3_BodyLarge)
                textContentBySet.addView(text)
            }
            btnBySet.setOnClickListener {
                listener.onClick(
                    set.map {
                    Exercise(id = it.id,name = it.name, imageResource = it.imageResource)
                })
            }
            dateBySet.text = set.first().datestr
            bySetContainer.visibility = View.VISIBLE
        }
    }

    interface HistoryAdapterListener{
        fun onClick(exercises: List<Exercise>)

        fun getStatus() : HistoryActivity.State
    }
}