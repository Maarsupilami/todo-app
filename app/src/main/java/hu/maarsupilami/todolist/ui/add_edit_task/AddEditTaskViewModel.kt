package hu.maarsupilami.todolist.ui.add_edit_task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.maarsupilami.todolist.data.Task
import hu.maarsupilami.todolist.data.TaskRepository
import hu.maarsupilami.todolist.util.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var task by mutableStateOf<Task?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val taskId = savedStateHandle.get<Int>("taskId")!!
        if (taskId != -1) {
            viewModelScope.launch {
                repository.getTaskById(taskId)?.let {
                    title = it.title
                    description = it.description ?: ""
                    this@AddEditTaskViewModel.task = task
                }
            }
        }
    }

    fun onEvent(event: AddEditTaskEvent) {
        when(event) {
            is AddEditTaskEvent.OnTitleChange -> {
                title = event.title
            }
            is AddEditTaskEvent.OnDescriptionChange -> {
                description = event.description
            }
            is AddEditTaskEvent.OnSaveTaskClick -> {
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendUiEvent(UiEvent.ShowToast(
                            message = "The task cannot be blank"
                        ))
                        return@launch
                    }
                    repository.insertTask(
                        Task(
                            title = title,
                            description = description,
                            completed = task?.completed ?: false,
                            id = task?.id
                        )
                    )
                    sendUiEvent(UiEvent.PopBackStack)
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