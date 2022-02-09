package com.adrianczerwinski.notesapp.navigation

import androidx.navigation.NavHostController
import com.adrianczerwinski.notesapp.data.util.Action
import com.adrianczerwinski.notesapp.data.util.Constants.LIST_SCREEN
import com.adrianczerwinski.notesapp.data.util.Constants.SPLASH_SCREEN

class Screens(navController: NavHostController) {
    val splash: () -> Unit ={
        navController.navigate(route = "list/${Action.NO_ACTION.name}"){
            popUpTo(SPLASH_SCREEN) {inclusive = true} //inclusice(true) deletes it from a back stock

        }
    }
    val note: (Action) -> Unit = { action ->
        navController.navigate("list/${action.name}") {
            popUpTo(LIST_SCREEN) {inclusive = true}
        }
    }

    val list: (Int) -> Unit = { noteId ->
        navController.navigate("note/$noteId")
    }
}