package com.example.recipequestfirebase.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipequestfirebase.ui.components.BottomNavBar
import com.example.recipequestfirebase.ui.components.HomeTopBar
import com.example.recipequestfirebase.ui.components.RecipeCard
import com.example.recipequestfirebase.ui.viewmodels.RecipeViewModel
import com.example.recipequestfirebase.ui.viewmodels.AuthViewModel

@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val recipeVm: RecipeViewModel = viewModel()
    val currentUserId by authViewModel.currentUserId.collectAsState()

    LaunchedEffect(currentUserId) { currentUserId?.let { recipeVm.observeUserRecipes(it) } }
    val items by recipeVm.recipes.collectAsState()

    Scaffold(
        topBar = { HomeTopBar() },
        bottomBar = {
            // simple bottom bar navigation; route change not wired here to keep example simple
            BottomNavBar(onSearchClick = { navController.navigate("search") }
                , onHomeClick = {navController.navigate("home")})
        },
         floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("form") })
            { Icon(Icons.Default.Add, contentDescription = "add recipe", Modifier.size(40.dp)) }
        },
    ) { padding ->
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {
            items(items) { r ->
                RecipeCard(recipe = r, onClick = { navController.navigate("detail/${r.id}") })
            }
        }
    }
}
