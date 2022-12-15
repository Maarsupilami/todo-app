package hu.maarsupilami.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    val title: String,
    val description: String?,
    val completed: Boolean,
    @PrimaryKey val id: Int? = 0
)