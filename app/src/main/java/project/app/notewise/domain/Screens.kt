package project.app.notewise.domain

sealed class Screens(val route: String) {
    object Home : Screens("home")
    object CreateNotes : Screens("createNotes")
    object AskAI : Screens("askAI")
    object Profile : Screens("profile")
    object Settings : Screens("settings")
    object Search : Screens("search")
}