package com.adrianczerwinski.notesapp.navigation.destinations

import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.adrianczerwinski.notesapp.data.util.Constants.LIST_ARGUMENT_KEY
import com.adrianczerwinski.notesapp.data.util.Constants.LIST_SCREEN
import com.adrianczerwinski.notesapp.ui.screens.list.ListScreen
import com.adrianczerwinski.notesapp.ui.viewModels.SharedViewModel

@ExperimentalMaterialApi
fun NavGraphBuilder.listComposable(
    navigateToNoteScreen: (noteId: Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = LIST_SCREEN,
        arguments = listOf(navArgument(LIST_ARGUMENT_KEY){
            type = NavType.StringType
        })
    ){
        ListScreen(navigateToNoteScreen = navigateToNoteScreen,
        sharedViewModel = sharedViewModel)


    }
}