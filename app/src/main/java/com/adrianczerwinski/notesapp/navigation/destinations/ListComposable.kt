package com.adrianczerwinski.notesapp.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.adrianczerwinski.notesapp.data.util.Action
import com.google.accompanist.navigation.animation.composable
import com.adrianczerwinski.notesapp.data.util.Constants.LIST_ARGUMENT_KEY
import com.adrianczerwinski.notesapp.data.util.Constants.LIST_SCREEN
import com.adrianczerwinski.notesapp.data.util.toAction
import com.adrianczerwinski.notesapp.ui.screens.list.ListScreen
import com.adrianczerwinski.notesapp.ui.viewModels.SharedViewModel

@ExperimentalAnimationApi
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

        var myAction by rememberSaveable {
            mutableStateOf(Action.NO_ACTION)
        }

        LaunchedEffect(key1 = myAction ) {
            if(action != myAction) {
                myAction = action
                sharedViewModel.action.value = action
            }

        }

        val databaseAction by sharedViewModel.action


        ListScreen(navigateToNoteScreen = navigateToNoteScreen,
        sharedViewModel = sharedViewModel, action = databaseAction)


    }
}