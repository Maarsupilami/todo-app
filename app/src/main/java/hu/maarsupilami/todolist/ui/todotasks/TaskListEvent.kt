package hu.maarsupilami.todolist.ui.todotasks

import hu.maarsupilami.todolist.data.Task

sealed class TaskListEvent {
    data class OnDeleteTaskClick(val task: Task): TaskListEvent()
    data class OnCompletionChange(val task: Task, val isCompleted: Boolean): TaskListEvent()
    object OnUndoDeleteClick: TaskListEvent()
    data class OnTaskClick(val task: Task): TaskListEvent()
    object OnAddTaskClick: TaskListEvent()
}
