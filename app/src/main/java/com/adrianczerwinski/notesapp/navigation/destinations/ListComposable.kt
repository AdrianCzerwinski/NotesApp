package com.adrianczerwinski.notesapp.navigation.destinations

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.adrianczerwinski.notesapp.data.util.Constants.LIST_ARGUMENT_KEY
import com.adrianczerwinski.notesapp.data.util.Constants.LIST_SCREEN
import com.adrianczerwinski.notesapp.data.util.toAction
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
    ){ navBackStackEntry ->
        val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()

        LaunchedEffect(key1 = action ) {
            sharedViewModel.action.value = action
        }


        ListScreen(navigateToNoteScreen = navigateToNoteScreen,
        sharedViewModel = sharedViewModel)


    }
}