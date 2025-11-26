package com.example.recipequestfirebase.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipequestfirebase.ui.viewmodels.RecipeViewModel
import com.example.recipequestfirebase.ui.viewmodels.AuthViewModel
import com.example.recipequestfirebase.data.RecipeModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormRecipeScreen(navController: NavController, recipeId: String? = null, authViewModel: AuthViewModel = viewModel()) {
    val recipeVm: RecipeViewModel = viewModel()
    val currentUserId by authViewModel.currentUserId.collectAsState()
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var tools by remember { mutableStateOf("") }
    var steps by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // load existing recipe if editing
    LaunchedEffect(recipeId) {
        recipeId?.let {
            val r = recipeVm.getRecipeById(it)
            r?.let { rr ->
                title = rr.title
                ingredients = rr.ingredients.joinToString("\n")
                tools = rr.tools.joinToString("\n")
                steps = rr.steps.joinToString("\n")
                description = rr.description
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TopAppBar(title = { Text(if (recipeId == null) "Tambah Resep" else "Edit Resep") }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") }
        })
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Nama Resep") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = ingredients, onValueChange = { ingredients = it }, label = { Text("Bahan-bahan (satu per baris)") }, modifier = Modifier.fillMaxWidth().height(120.dp))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = tools, onValueChange = { tools = it }, label = { Text("Alat-alat (satu per baris)") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = steps, onValueChange = { steps = it }, label = { Text("Langkah-langkah (satu per baris)") }, modifier = Modifier.fillMaxWidth().height(160.dp))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Deskripsi (opsional)") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { /* image placeholder */ }, modifier = Modifier.fillMaxWidth().height(120.dp)) { Text("Tap to add Image (Placeholder)") }
        Spacer(modifier = Modifier.height(12.dp))
        Row {
            Button(onClick = {
                val r = RecipeModel(
                    id = recipeId ?: "",
                    title = title,
                    ingredients = ingredients.lines().map { it.trim() }.filter { it.isNotEmpty() },
                    tools = tools.lines().map { it.trim() }.filter { it.isNotEmpty() },
                    steps = steps.lines().map { it.trim() }.filter { it.isNotEmpty() },
                    description = description,
                    ownerUserId = currentUserId ?: ""
                )
                scope.launch {
                    if (recipeId == null) {
                        recipeVm.addRecipe(r) { navController.popBackStack() }
                    } else {
                        recipeVm.updateRecipe(recipeId, r) { navController.popBackStack() }
                    }
                }
            }, modifier = Modifier.weight(1f)) { Text("Simpan") }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f)) { Text("Batal") }
        }
    }
}