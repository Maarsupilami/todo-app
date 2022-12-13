package hu.maarsupilami.todolist.ui.todotasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.maarsupilami.todolist.util.UiEvent
import kotlinx.coroutines.flow.collect

@Composable
fun TaskListScreen(
    onNavigate: (UiEvent.Navigate) -> Unit,
    viewModel: TaskListViewModel = hiltViewModel()
) {
    val tasks = viewModel.tasks.collectAsState(initial = emptyList())
    val scaffoldState = rememberScaffoldState()
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {
            when(it) {
                is UiEvent.ShowToast -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = it.message,
                        actionLabel = it.action
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TaskListEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.Navigate -> onNavigate(it)
                else -> Unit
            }
        }
    }
    Scaffold (
        scaffoldState = scaffoldState,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.onEvent(TaskListEvent.OnAddTaskClick)
            }) {
                  Icon(
                      imageVector = Icons.Default.Add,
                      contentDescription = "Add"
                  )
            }
    }
    ){
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasks.value) { task ->
                TaskItem(
                    task = task,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onEvent(TaskListEvent.OnTaskClick(task))
                        }
                        .padding(16.dp)
                )
            }
        }
    }
}