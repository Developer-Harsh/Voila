package `in`.snbapps.vidmeet.navigation

import android.os.Build
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import `in`.snbapps.vidmeet.ui.pages.AddStoryScreen
import `in`.snbapps.vidmeet.ui.pages.ConnectionScreen
import `in`.snbapps.vidmeet.ui.pages.EditProfileScreen
import `in`.snbapps.vidmeet.ui.pages.ForgotScreen
import `in`.snbapps.vidmeet.ui.pages.HomeScreen
import `in`.snbapps.vidmeet.ui.pages.IntroScreen
import `in`.snbapps.vidmeet.ui.pages.LoginScreen
import `in`.snbapps.vidmeet.ui.pages.MyProfileScreen
import `in`.snbapps.vidmeet.ui.pages.PolicyScreen
import `in`.snbapps.vidmeet.ui.pages.RegisterScreen
import `in`.snbapps.vidmeet.ui.pages.ScannerScreen
import `in`.snbapps.vidmeet.ui.pages.SplashScreen
import `in`.snbapps.vidmeet.ui.pages.ViewPhotoScreen
import `in`.snbapps.vidmeet.ui.pages.WhatVoilaScreen
import `in`.snbapps.vidmeet.utils.NetworkViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalGetImage::class)
@Composable
fun VoilaRoutes(navController: NavHostController, viewModel: NetworkViewModel) {
    NavHost(
        navController = navController,
        startDestination = Pages.Splash.route
    ) {
        composable(route = Pages.Splash.route) {
            SplashScreen(navHostController = navController)
        }
        composable(route = Pages.Home.route) {
            HomeScreen(navHostController = navController)
        }
        composable(route = Pages.Introduction.route) {
            IntroScreen(navHostController = navController)
        }
        composable(route = Pages.Login.route) {
            LoginScreen(navHostController = navController, viewModel)
        }
        composable(route = Pages.Register.route) {
            RegisterScreen(navHostController = navController, viewModel)
        }
        composable(route = Pages.Policy.route) {
            PolicyScreen(navHostController = navController, viewModel)
        }
        composable(route = Pages.Forgot.route) {
            ForgotScreen(navHostController = navController, viewModel)
        }
        composable(route = Pages.WhatVoila.route) {
            WhatVoilaScreen(navHostController = navController)
        }
        composable(route = Pages.EditProfile.route) {
            EditProfileScreen(navHostController = navController)
        }
        composable(route = Pages.Connection.route) {
            ConnectionScreen(navHostController = navController)
        }
        composable(route = Pages.AddStory.route) {
            AddStoryScreen(navHostController = navController)
        }
        composable(route = Pages.Scanner.route) {
            ScannerScreen(navHostController = navController)
        }
        composable(route = Pages.MyProfile.route) {
            MyProfileScreen(navHostController = navController)
        }
        composable(route = Pages.ViewPhoto.route) { backStackEntry ->
            val img = backStackEntry.arguments?.getString("img") ?: ""
            ViewPhotoScreen(navHostController = navController, img = img)
        }
    }
}