package com.example.recipequestfirebase.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipequestfirebase.data.RecipeModel

@Composable
fun RecipeCard(recipe: RecipeModel, onClick: ()->Unit) {
    Surface(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), tonalElevation = 2.dp) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(text = recipe.title)
                Text(text = recipe.description.ifBlank { "rating kesusahan" })
            }
            Button(onClick = onClick) { Text("Lihat") }
        }
    }
}