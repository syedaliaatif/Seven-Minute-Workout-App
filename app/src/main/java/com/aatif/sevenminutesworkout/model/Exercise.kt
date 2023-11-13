package com.aatif.sevenminutesworkout.model

import android.os.Parcel
import android.os.Parcelable

data class Exercise(
     val id: Int,
     val name: String,
     val imageResource: Int? = null,
     val exerciseStatus: ExerciseStatus = ExerciseStatus.INCOMPLETE,
     val isSelected:Boolean = false
) : Parcelable {
     constructor(parcel: Parcel) : this(
          id = parcel.readInt(),
          name = parcel.readString().orEmpty(),
          imageResource = parcel.readValue(Int::class.java.classLoader) as? Int,
          exerciseStatus = parcel.readInt().let {
              when (it) {
                    0 -> {
                         ExerciseStatus.COMPLETED
                    }
                    2 -> {
                         ExerciseStatus.INPROGRESS
                    }
                    else -> {
                         ExerciseStatus.INCOMPLETE
                    }
               }
          },
          isSelected = parcel.readByte() != 0.toByte()
     ) {
     }

     override fun writeToParcel(parcel: Parcel, flags: Int) {
          parcel.writeInt(id)
          parcel.writeString(name)
          parcel.writeValue(imageResource)
          parcel.writeInt(exerciseStatus.value)
          parcel.writeByte(if (isSelected) 1 else 0)
     }

     override fun describeContents(): Int {
          return 0
     }

     companion object CREATOR : Parcelable.Creator<Exercise> {
          override fun createFromParcel(parcel: Parcel): Exercise {
               return Exercise(parcel)
          }

          override fun newArray(size: Int): Array<Exercise?> {
               return arrayOfNulls(size)
          }
     }
}

