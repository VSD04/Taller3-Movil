package com.example.taller3

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.taller3.ui.screens.pantallaInicioSesion
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.taller3.ui.screens.CrearCuenta
import com.example.taller3.ui.screens.MapaUsuarios
import com.example.taller3.ui.screens.PerfilUsuario

@Serializable
sealed interface Routes{
    @Serializable
    object pantallaInicioSesion
    @Serializable
    object CrearCuenta
    @Serializable
    data class MapaUsuarios(val usuario: String)
    @Serializable
    data class PerfilUsuario(val usuario: String)
}

@Composable
fun NavigationStack (modifier : Modifier = Modifier){
val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.pantallaInicioSesion,
        modifier = modifier
    ) {
        composable<Routes.pantallaInicioSesion>{
            pantallaInicioSesion(navController = navController)
        }

        composable<Routes.CrearCuenta>{
            CrearCuenta(navController=navController)
        }

        composable<Routes.MapaUsuarios> {
            val args = it.toRoute<Routes.MapaUsuarios>()
            MapaUsuarios(navController = navController, nombreUsuario = args.usuario)
        }

        composable<Routes.PerfilUsuario> {
            val args = it.toRoute<Routes.PerfilUsuario>()
            PerfilUsuario(navController = navController, usuario = args.usuario)
        }
    }
}