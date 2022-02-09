package com.adrianczerwinski.notesapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import com.adrianczerwinski.notesapp.data.util.Constants.SPLASH_SCREEN
import com.adrianczerwinski.notesapp.navigation.destinations.listComposable
import com.adrianczerwinski.notesapp.navigation.destinations.noteComposable
import com.adrianczerwinski.notesapp.navigation.destinations.splashComposable
import com.adrianczerwinski.notesapp.ui.viewModels.SharedViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SetupNavigation(
    navController:NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember(navController){
        Screens(navController = navController)
    }
    AnimatedNavHost(navController = navController, startDestination = SPLASH_SCREEN) {
        splashComposable(
            navigateToListScreen = screen.splash
        )
        listComposable(
            navigateToNoteScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
        noteComposable (
            sharedViewModel = sharedViewModel,
            navigateToListScreen = screen.note
        )
    }
}