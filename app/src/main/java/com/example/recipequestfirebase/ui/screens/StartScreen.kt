package com.example.recipequestfirebase.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import com.example.recipequestfirebase.R


@Composable
fun StartScreen(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = "App Logo"
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(onClick = { navController.navigate("login") }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(onClick = { navController.navigate("register") }, modifier = Modifier.fillMaxWidth().height(48.dp)) {
                Text("Daftar")
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(onClick = { navController.navigate("home") }, modifier = Modifier.fillMaxWidth().height(44.dp)) {
                Text("Lanjut tanpa autentikasi (demo)")
            }
        }
    }
}
