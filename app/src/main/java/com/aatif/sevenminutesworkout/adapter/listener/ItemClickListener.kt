package com.aatif.sevenminutesworkout.adapter.listener

import android.view.View
import com.aatif.sevenminutesworkout.model.Exercise

interface ItemClickListener {

    fun onItemClick(view:View, position:Int, data:MutableList<Exercise>)
}
