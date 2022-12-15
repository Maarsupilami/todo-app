package hu.maarsupilami.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import hu.maarsupilami.todolist.ui.add_edit_task.AddEditTodoScreen
import hu.maarsupilami.todolist.ui.todotasks.TaskListScreen
import hu.maarsupilami.todolist.util.Routes

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppTheme()
        }
    }
}

@Preview
@Composable
fun TodoAppTheme() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.TASK_LIST
        ) {
        composable(Routes.TASK_LIST) {
            TaskListScreen(
                onNavigate = {
                    navController.navigate(it.route)
                }
            )
        }
        composable(
            route = Routes.ADD_EDIT_TASK + "?taskId={taskId}",
            arguments = listOf(
                navArgument(name = "taskId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) {
            AddEditTodoScreen(onPopBackStack = {
                navController.popBackStack()
            })
        }
    }
}