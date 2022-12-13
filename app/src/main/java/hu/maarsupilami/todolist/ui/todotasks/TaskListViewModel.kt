package hu.maarsupilami.todolist.ui.todotasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.maarsupilami.todolist.data.Task
import hu.maarsupilami.todolist.data.TaskRepository
import hu.maarsupilami.todolist.util.Routes
import hu.maarsupilami.todolist.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {

    val tasks = repository.getTasks()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTask: Task? = null

    fun onEvent(event: TaskListEvent) {
        when(event) {
            is TaskListEvent.OnTaskClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TASK + "?taskId=${event.task.id}"))
            }
            is TaskListEvent.OnAddTaskClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.ADD_EDIT_TASK))
            }
            is TaskListEvent.OnUndoDeleteClick -> {
                deletedTask?.let {
                    viewModelScope.launch {
                        repository.insertTask(it)
                    }
                }
            }
            is TaskListEvent.OnDeleteTaskClick -> {
                viewModelScope.launch {
                    deletedTask = event.task
                    repository.deleteTask(event.task)
                    sendUiEvent(UiEvent.ShowToast(
                        message = "Task deleted",
                        action = "Undo"
                    ))
                }
            }
            is TaskListEvent.OnCompletionChange -> {
                viewModelScope.launch {
                    repository.insertTask(
                       event.task.copy(
                           completed =  event.isCompleted
                       )
                    )
                }
            }
        }
    }

    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}