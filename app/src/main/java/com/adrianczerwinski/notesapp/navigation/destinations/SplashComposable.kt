package com.adrianczerwinski.notesapp.navigation.destinations

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import com.adrianczerwinski.notesapp.data.util.Constants.SPLASH_SCREEN
import com.adrianczerwinski.notesapp.ui.splash.SplashScreen

@ExperimentalAnimationApi
@ExperimentalMaterialApi
fun NavGraphBuilder.splashComposable(
    navigateToListScreen: () -> Unit
) {
    composable(
        route = SPLASH_SCREEN,
        exitTransition = { _, _ ->
            slideOutVertically(
      //          targetOffsetY = {-it},
                animationSpec = tween(300))
        }
    ){
        SplashScreen(
            navigateToListScreen = navigateToListScreen
        )
    }
}