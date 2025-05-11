package com.example.taller3.ui.screens

import android.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MapaUsuarios(modifier: Modifier = Modifier, navController: NavController, nombreUsuario: String){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                titleContentColor = MaterialTheme.colorScheme.primary
            ),
            title ={
                Text(
                    "Mapa Usuarios",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Perfil de usuario"
                    )
                }
            },
            scrollBehavior =scrollBehavior
        )}
    ){ innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text("Falta mapa y perdir permiso para el uso del gps, en la creacion del usuario falta asignar la latitud y longitud" +
                    "para luego usar en el switch que se pondra en esta pagina y para la parte del perfil del usuario por el momento solo" +
                    "se vera el nombre del usuario")
        }
    }
}