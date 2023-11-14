package com.aatif.sevenminutesworkout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aatif.sevenminutesworkout.R
import com.aatif.sevenminutesworkout.model.Exercise
import com.aatif.sevenminutesworkout.room.entities.History

class HistoryAdapter(private val listener:HistoryAdapterListener): Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val history: MutableList<History> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.history_item, parent, false)
        return HistoryViewHolder(view)
    }


    override fun getItemCount(): Int {
        return history.size
    }
    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(history[position], listener)
    }

    fun setContent(content:List<History>){
        history.clear()
        history.addAll(content)
    }

    class HistoryViewHolder(itemView: View):ViewHolder(itemView){

        private val image: ImageView by lazy { itemView.findViewById(R.id.iv_history) }
        private val title: TextView by lazy{itemView.findViewById(R.id.tv_history_exercise_name)}
        private val date : TextView by lazy{itemView.findViewById(R.id.tv_history_date)}
        private val duration:TextView by lazy{itemView.findViewById(R.id.tv_history_duration)}
        private val btn: ImageView by lazy{itemView.findViewById(R.id.ib_restart)}
        fun bind(historyItem: History, listener: HistoryAdapterListener){
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

        }
    }

    interface HistoryAdapterListener{
        fun onClick(exercises: List<Exercise>)
    }
}