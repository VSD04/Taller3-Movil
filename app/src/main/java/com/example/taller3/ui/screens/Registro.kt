package com.example.taller3.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.processNextEventInCurrentThread
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.taller3.Routes
import com.example.taller3.data.Usuario

@Composable
fun CrearCuenta(modifier: Modifier = Modifier, navController: NavController){
    val context = LocalContext.current
    var email by remember {mutableStateOf("")}
    var contraseña by remember {mutableStateOf("")}
    var nombre by remember {mutableStateOf("")}
    var apellidos by remember {mutableStateOf("")}
    var numero by remember {mutableIntStateOf(0)}
    var showError by remember {mutableStateOf(false)}
    var Conectado by remember {mutableStateOf(false)}
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    Column(
        modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(21.dp)
        )
        OutlinedTextField(
            value = email,
            onValueChange = {email=it},
            label ={Text("Correo Electronico")},
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                autoCorrectEnabled = true
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = showError && email.isBlank()
        )
        OutlinedTextField(
            value = contraseña,
            onValueChange = {contraseña=it},
            label = {Text("Contraseña")},
            modifier = Modifier.fillMaxWidth(),
            isError = showError && !isValidPassword(contraseña),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
                autoCorrectEnabled = true
            )
        )
        OutlinedTextField(
            value = nombre,
            onValueChange = {nombre=it},
            label = {Text("Nombre de Usuario")},
            modifier = Modifier.fillMaxWidth(),
            isError = showError && nombre.isBlank(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
                autoCorrectEnabled = true
            )
        )

        OutlinedTextField(
            value = numero.toString(),
            onValueChange = { newText ->
                numero= newText.toIntOrNull()?:numero
            },
            modifier = Modifier.fillMaxWidth(),
            label = {Text("Numero de Telefono")},
            isError = showError && numero ==0,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next,
                autoCorrectEnabled = true
            )
        )
        Button(onClick = {
            showError = true
            if(validateAllFields(email,contraseña,nombre,numero)){
                val hashedPassword = BCrypt.withDefaults().hashToString(12, contraseña.toCharArray())
                auth.createUserWithEmailAndPassword(email,contraseña)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            val user= auth.currentUser
                            user?.let {
                                val uid = it.uid
                                val usuario = Usuario(
                                    email =email,
                                    contraseña = contraseña,
                                    nombreUsuario = nombre,
                                    numeroTelefono = numero,
                                    Activo = Conectado
                                )
                                db.collection("usuarios")
                                    .document(uid)
                                    .set(usuario)
                                    .addOnSuccessListener {
                                        Toast.makeText(context,"Registro Completado",Toast.LENGTH_SHORT).show()
                                        navController.navigate(Routes.pantallaInicioSesion)
                                    }
                                    .addOnFailureListener { e->
                                        Toast.makeText(context,"Error al guardar en Firestore ${e.message}",Toast.LENGTH_LONG).show()
                                    }
                            }
                        } else{
                            Toast.makeText(context,"Error: ${task.exception?.message}",Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrarse")
        }
        if(showError){
            ValidationErrors(email, contraseña, nombre, numero)
        }
        TextButton(onClick = { navController.navigate(Routes.pantallaInicioSesion) }) {
            Text(
                text="Si ya tienes cuenta, Inicia Sesion",
                color = Color.Cyan
            )
        }
    }
}

fun isValidPassword(password: String): Boolean{
    return password.length >=8
}

fun validateAllFields(
    email:String,
    contraseña: String,
    nombre: String,
    numero: Int
): Boolean{
    return email.isBlank() &&
            contraseña.isBlank() || isValidPassword(contraseña)&&
            nombre.isBlank() && numero==0
}

@Composable
fun ValidationErrors(email: String, contraseña: String, nombre: String, numero: Int ){
    Column {
        if(email.isBlank()){
            Text("Correo es requerido", color = Color.Red)
        }
        if (contraseña.isBlank()){
            Text("Contraseña es requerida", color = Color.Red)
        }else if(isValidPassword(contraseña)){
            Text("Contraseña debe ser más de ocho caracteres", color = Color.Red)
        }
        if (nombre.isBlank()){
            Text("nombre es requerido", color = Color.Red)
        }
        if (numero==0){
            Text("numero es requerido", color = Color.Red)
        }
    }
}

