package com.aatif.sevenminutesworkout.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.aatif.sevenminutesworkout.R
import com.aatif.sevenminutesworkout.model.Exercise
import com.aatif.sevenminutesworkout.model.ExerciseStatus

internal class PaginationAdapter:Adapter<PaginationAdapter.PaginationItemViewHolder>() {

    val data : MutableList<Exercise> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaginationItemViewHolder {
        val context = parent.context
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.pagination_item, parent, false)
        return PaginationItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaginationItemViewHolder, position: Int) {
        holder.updateText(data[position].id.toString())
        holder.updateStatus(data[position].exerciseStatus)
    }

    override fun getItemCount(): Int = data.size

    fun setContent(newData: List<Exercise>){
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    @Throws(IllegalArgumentException::class)
    fun updateContent(position: Int, newContent: Exercise){
        if(position>= data.size){
          throw  IllegalArgumentException("Trying to update content at position greater than the size of data in recycler view.")
            return
        }
        data[position] = newContent
        notifyItemChanged(position)
    }

    internal class PaginationItemViewHolder(itemView: View): ViewHolder(itemView){

        private val exerciseNumber = itemView.findViewById<TextView>(R.id.tv_exercise_number)

        fun updateText(text:String){
            exerciseNumber.text = text
        }

        fun updateStatus(status: ExerciseStatus){
            when(status){
                ExerciseStatus.COMPLETED -> {
                    exerciseNumber.apply {
                        background = resources.getDrawable(R.drawable.pagination_item_background_black)
                        setTextColor(Color.WHITE)
                    }
                }
                ExerciseStatus.INCOMPLETE -> {
                    exerciseNumber.apply {
                        background = resources.getDrawable(R.drawable.pagination_item_background_white)
                        setTextColor(Color.BLACK)
                    }
                }
                ExerciseStatus.INPROGRESS ->{
                    exerciseNumber.apply {
                        background = resources.getDrawable(R.drawable.pagination_item_background_grey)
                        setTextColor(Color.BLACK)
                    }
                }
            }
        }
    }
}