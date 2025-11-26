package com.example.recipequestfirebase.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipequestfirebase.ui.viewmodels.RecipeViewModel
import com.example.recipequestfirebase.data.RecipeModel
import com.example.recipequestfirebase.ui.components.AppTopBar
import com.example.recipequestfirebase.ui.components.BottomNavBar
import kotlinx.coroutines.launch
import com.example.recipequestfirebase.ui.viewmodels.AuthViewModel

@Composable
fun DetailScreen(recipeId: String, navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val recipeVm: RecipeViewModel = viewModel()
    val currentUserId by authViewModel.currentUserId.collectAsState()
    val scope = rememberCoroutineScope()
    var recipe by remember { mutableStateOf<RecipeModel?>(null) }

    LaunchedEffect(recipeId) {
        // try load from repo
        scope.launch {
            recipe = recipeVm.getRecipeById(recipeId)
        }
    }

    Scaffold(bottomBar = {
        AppTopBar(recipe?.title ?: "Not Found")
        BottomNavBar(onSearchClick = { navController.navigate("search") }
        , onHomeClick = {navController.navigate("home")})
    }) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {
            Text(recipe?.title ?: "Not Found", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = { /* image placeholder */ }, modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)) {
                Text("Gambar makanan")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Surface(modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 140.dp), tonalElevation = 0.dp) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Deskripsi :")
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(recipe?.description ?: "-")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Bahan-bahan:")
            recipe?.ingredients?.forEach { Text("- $it") }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Alat-alat:")
            recipe?.tools?.forEach { Text("- $it") }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Langkah-langkah:")
            recipe?.steps?.forEachIndexed { i, s -> Text("${i+1}. $s") }

            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = { navController.navigate("form/${recipeId}") }) { Text("Edit") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    recipe?.let {
                        scope.launch {
                            recipeVm.deleteRecipe(it.id) { navController.popBackStack() }
                        }
                    }
                }) { Text("Delete") }
                Spacer(modifier = Modifier.width(8.dp))
                if (recipe?.ownerUserId == currentUserId) {
                    Button(onClick = {
                        val isShared = recipe?.isShared ?: false
                        scope.launch {
                            recipeVm.setShared(recipeId, !isShared, currentUserId)
                            // reload
                            recipe = recipeVm.getRecipeById(recipeId)
                        }
                    }) { Text(if (recipe?.isShared == true) "Unshare" else "Share") }
                }
            }
        }
    }
}