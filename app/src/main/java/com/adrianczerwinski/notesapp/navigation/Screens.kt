package com.adrianczerwinski.notesapp.navigation

import androidx.navigation.NavHostController
import com.adrianczerwinski.notesapp.data.util.Action
import com.adrianczerwinski.notesapp.data.util.Constants.LIST_SCREEN

class Screens(navController: NavHostController) {
    val list: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) {inclusive = true}
        }
    }

    val note: (Int) -> Unit = { noteId ->
        navController.navigate("note/$noteId")
    }
}