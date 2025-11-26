package com.example.recipequestfirebase.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipequestfirebase.ui.viewmodels.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .offset(y = (-14).dp),
            verticalArrangement = Arrangement.Center
            ) {
            Text("Login")
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                authViewModel.login(email, password, onSuccess = {
                    navController.navigate("home") {
                        popUpTo("start") { inclusive = true }
                    }
                })
            }, modifier = Modifier.fillMaxWidth().height(48.dp)) { Text("Login") }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(onClick = { navController.navigate("register") }, modifier = Modifier.fillMaxWidth().height(44.dp)) {
                Text("Daftar")
            }
        }
    }
}
