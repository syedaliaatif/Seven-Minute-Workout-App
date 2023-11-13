package com.aatif.sevenminutesworkout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aatif.sevenminutesworkout.R
import com.aatif.sevenminutesworkout.adapter.listener.ItemClickListener
import com.aatif.sevenminutesworkout.model.Exercise

class ExerciseSelectionAdapter(private val itemClickListener:ItemClickListener): Adapter<ExerciseSelectionAdapter.ExerciseDetailViewHolder>() {

    private val content : MutableList<Exercise> = mutableListOf()

    fun setContent(newContent: List<Exercise>){
        content.clear()
        content.addAll(newContent)
        notifyDataSetChanged()
    }

    fun updateContent(position: Int, exercise: Exercise){
        if(position < content.size) {
            content[position] = exercise
            notifyItemChanged(position)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseDetailViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.exercise_detail_item, parent, false)
        return ExerciseDetailViewHolder(view)
    }

    override fun getItemCount(): Int = content.size

    override fun onBindViewHolder(holder: ExerciseDetailViewHolder, position: Int) {
        content[position].apply {
            imageResource?.let {
                holder.updateImage(it)
            }
            holder.updateTitle(name)
        }
        holder.setOnClickListener(itemClickListener, position, content)
    }

    fun getSelected(): List<Exercise> = content.filter {
        it.isSelected
    }

    class ExerciseDetailViewHolder(private val itemView: View):ViewHolder(itemView){
        private val image: ImageView = itemView.findViewById(R.id.iv_exercise_image)
        private val title: TextView = itemView.findViewById(R.id.tv_exercise_title)
        private val radioButton: RadioButton = itemView.findViewById(R.id.rb_select_exercise_toggle)
        init {
            radioButton.setActivated(false)
        }
        fun updateImage(@DrawableRes resId:Int){
            image.setImageResource(resId)
        }

        fun updateTitle(text: String){
            title.text = text
        }

        fun setOnClickListener(itemClickListener: ItemClickListener, position: Int,content:MutableList<Exercise>){
            itemView.setOnClickListener {
                itemClickListener.onItemClick(itemView,position,content )
            }
            radioButton.isClickable = false
        }
    }
}