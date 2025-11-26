package com.example.recipequestfirebase.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipequestfirebase.ui.components.BottomNavBar
import com.example.recipequestfirebase.ui.components.HomeTopBar
import com.example.recipequestfirebase.ui.viewmodels.RecipeViewModel
import com.example.recipequestfirebase.ui.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    val recipeVm: RecipeViewModel = viewModel()
    val items by recipeVm.recipes.collectAsState()
    val currentUserId by authViewModel.currentUserId.collectAsState()
    val scope = rememberCoroutineScope()

    // map userId->name cache
    val sharerNames = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(Unit) {
        recipeVm.observeShared()
    }

    // whenever items change, fetch sharer names if missing
    LaunchedEffect(items) {
        val ids = items.map { it.sharedByUserId ?: it.ownerUserId }.distinct()
        ids.forEach { id ->
            if (!sharerNames.containsKey(id)) {
                scope.launch {
                    val u = recipeVm.getUserById(id)
                    sharerNames[id] = u?.name ?: "User"
                }
            }
        }
    }

    Scaffold(
        topBar = { HomeTopBar() },
        bottomBar = {BottomNavBar(onSearchClick = { navController.navigate("search") }, onHomeClick = {navController.navigate("home")})  })
    { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {
            var query by remember { mutableStateOf("") }
            OutlinedTextField(value = query, onValueChange = { query = it }, label = { Text("Cari resep publik...") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {
                items(items.filter { query.isBlank() || it.title.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) }) { r ->
                    Surface(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp), tonalElevation = 2.dp) {
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text(r.title)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(r.description)
                                Spacer(modifier = Modifier.height(6.dp))
                                val sharerId = r.sharedByUserId ?: r.ownerUserId
                                val sharerName = sharerNames[sharerId] ?: "User"
                                Text("Shared by: $sharerName")
                            }
                            Column {
                                Button(onClick = {
                                    val target = currentUserId ?: ""
                                    scope.launch { recipeVm.copyShared(r, target) }
                                }) {
                                    Text("Simpan")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}