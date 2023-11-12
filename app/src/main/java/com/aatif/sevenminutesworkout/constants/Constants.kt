package com.aatif.sevenminutesworkout.constants

import com.aatif.sevenminutesworkout.R
import com.aatif.sevenminutesworkout.model.Exercise

object Constants {

    fun getDefaultExercises(): List<Exercise>{
        return listOf(
            Exercise(
                1,
                "High Knees Running In",
                R.drawable.ic_abdominal_crunch
            ),
            Exercise(
                2,
                "Jumping Jacks",
                R.drawable.ic_jumping_jacks
            ),
            Exercise(
                3,
                "Lunge",
                R.drawable.ic_lunge
            ),
            Exercise(
                4,
                "Plank",
                R.drawable.ic_plank
            ),
            Exercise(
                5,
                "Push Up",
                R.drawable.ic_push_up
            ),
            Exercise(
                6,
                "Push Up And Rotation",
                R.drawable.ic_push_up_and_rotation
            ),
            Exercise(
                7,
                "Side Plank",
                R.drawable.ic_side_plank
            ),
            Exercise(
                8,
                "Squat",
                R.drawable.ic_squat
            ),
            Exercise(
                9,
                "Step Up Onto Chair",
                R.drawable.ic_step_up_onto_chair
            ),
            Exercise(
                10,
                "Triceps Dip On Chair",
                R.drawable.ic_triceps_dip_on_chair
            ),
            Exercise(
                11,
                "Wall Sit",
                R.drawable.ic_wall_sit
            )
        )
    }
}