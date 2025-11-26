package com.example.recipequestfirebase.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppTopBar(title: String, onSettings: (() -> Unit)? = null) {
    Surface(tonalElevation = 2.dp) {
        Row(modifier = Modifier.fillMaxWidth().height(64.dp).padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { onSettings?.invoke() }) {
                Icon(Icons.Default.Settings, contentDescription = "Settings")
            }
        }
    }
}
