package com.example.taller3.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.ui.Modifier

@Composable
fun PerfilUsuario(modifier: Modifier = Modifier, navController: NavController, usuario: String){
    Text("${usuario}")
}