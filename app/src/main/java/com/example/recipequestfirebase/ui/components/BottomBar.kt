package com.example.recipequestfirebase.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavBar(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    Surface(
        tonalElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onHomeClick) { Icon(Icons.Filled.Home, contentDescription = "Home", Modifier.size(50.dp)) }
            IconButton(onClick = onSearchClick) { Icon(Icons.Filled.Search , contentDescription = "Community", Modifier.size(50.dp)) }
        }
    }
}