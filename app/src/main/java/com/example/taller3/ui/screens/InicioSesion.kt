package com.example.taller3.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.taller3.Routes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun pantallaInicioSesion(modifier: Modifier = Modifier, navController: NavController){
    var correo by remember {mutableStateOf("")}
    var contraseña by remember {mutableStateOf("")}
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Iniciar Sesion",
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(30.dp)
        )
        Text(
            text="Ingrese correo y contraseña",
            fontFamily = FontFamily.SansSerif
        )
        OutlinedTextField(
            value = correo,
            onValueChange = {correo=it},
            label = {Text("Correo Electronico")},
            placeholder ={Text("ejemplo@gmail.com")},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
                autoCorrectEnabled = true
            )
        )
        Spacer(modifier=Modifier.padding(15.dp))

        OutlinedTextField(
            value = contraseña,
            onValueChange = {contraseña=it},
            label = {Text("Contraseña")},
            placeholder = {Text("**********")},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                autoCorrectEnabled = false
            )
        )
        Spacer(modifier=Modifier.padding(15.dp))

        Button(onClick ={
            when{
                correo.isEmpty()||contraseña.isEmpty()->{
                    Toast.makeText(context,"Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
                }
                else->{
                    val db = FirebaseFirestore.getInstance()
                    val auth = FirebaseAuth.getInstance()
                    auth.signInWithEmailAndPassword(correo, contraseña)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                db.collection("usuarios")
                                    .whereEqualTo("email", correo)
                                    .limit(1)
                                    .get()
                                    .addOnSuccessListener { documents->
                                        if(!documents.isEmpty){
                                            val userDoc = documents.documents[0]
                                            val nombreUsuario = userDoc.getString("nombreUsuario")?:""
                                            Toast.makeText(context,"Inicio de Sesion Exitoso",Toast.LENGTH_SHORT).show()
                                            navController.navigate(Routes.MapaUsuarios(nombreUsuario))
                                        }else{
                                            Toast.makeText(context,"Usuario no encontrado en BD", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(context,"Error al acceder a Firestore: ${it.message}",Toast.LENGTH_LONG).show()
                                    }
                            }else{
                                Toast.makeText(context,"\"Error:${task.exception?.message?: "Credenciales Invalidas"}\"",Toast.LENGTH_LONG).show()
                            }
                        }
                }
            }
        }) {
            Text(text = "Iniciar Sesión",
                fontFamily = FontFamily.SansSerif)
        }
        TextButton(onClick = {navController.navigate(Routes.CrearCuenta)}) {
            Text(
                text="Registrate Ya!",
                color=Color.Cyan
            )
        }
    }
}