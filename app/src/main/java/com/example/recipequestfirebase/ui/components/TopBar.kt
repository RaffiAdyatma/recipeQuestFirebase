package com.example.recipequestfirebase.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onProfileClick,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Filled.Person, contentDescription = "Profile")
            }

            Button(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Filled.Settings, contentDescription = "Settings")
            }
        }
    }
}