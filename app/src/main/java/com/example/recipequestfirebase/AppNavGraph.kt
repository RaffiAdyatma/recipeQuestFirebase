package com.example.recipequestfirebase

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.recipequestfirebase.ui.screens.LoginScreen
import com.example.recipequestfirebase.ui.screens.RegisterScreen
import com.example.recipequestfirebase.ui.screens.StartScreen
import com.example.recipequestfirebase.ui.screens.HomeScreen
import com.example.recipequestfirebase.ui.screens.SearchScreen
import com.example.recipequestfirebase.ui.screens.DetailScreen
import com.example.recipequestfirebase.ui.screens.FormRecipeScreen

@Composable
fun AppNavGraph(startDestination: String = "start") {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable("start") { StartScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("search") { SearchScreen(navController) }
        composable("form") { FormRecipeScreen(navController = navController, recipeId = null) }
        composable(
            "form/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("recipeId")
            FormRecipeScreen(navController = navController, recipeId = id)
        }
        composable(
            "detail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("recipeId") ?: ""
            DetailScreen(recipeId = id, navController = navController)
        }
    }
}
