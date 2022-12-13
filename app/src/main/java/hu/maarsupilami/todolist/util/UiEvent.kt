package hu.maarsupilami.todolist.util

sealed class UiEvent {

    object PopBackStack: UiEvent()
    data class Navigate(val route: String): UiEvent()
    data class ShowToast(
        val message: String,
        val action: String? = null
    ): UiEvent()
}