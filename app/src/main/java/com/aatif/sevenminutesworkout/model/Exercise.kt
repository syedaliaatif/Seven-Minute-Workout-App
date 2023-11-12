package com.aatif.sevenminutesworkout.model

data class Exercise(
     val id: Int,
     val name: String,
     val imageResource: Int? = null,
     val exerciseStatus: ExerciseStatus = ExerciseStatus.INCOMPLETE
)

