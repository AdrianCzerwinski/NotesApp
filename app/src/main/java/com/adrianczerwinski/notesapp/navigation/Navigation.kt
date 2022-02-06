package com.adrianczerwinski.notesapp.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.adrianczerwinski.notesapp.data.util.Constants.LIST_SCREEN
import com.adrianczerwinski.notesapp.navigation.destinations.listComposable
import com.adrianczerwinski.notesapp.navigation.destinations.noteComposable
import com.adrianczerwinski.notesapp.ui.viewModels.SharedViewModel

@ExperimentalMaterialApi
@Composable
fun SetupNavigation(
    navController:NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember(navController){
        Screens(navController = navController)
    }
    NavHost(navController = navController, startDestination = LIST_SCREEN) {
        listComposable(
            navigateToNoteScreen = screen.note,
            sharedViewModel = sharedViewModel
        )
        noteComposable (
            sharedViewModel = sharedViewModel,
            navigateToListScreen = screen.list
        )
    }
}