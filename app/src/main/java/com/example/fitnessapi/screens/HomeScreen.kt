package com.example.fitnessapi.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fitnessapi.Database.ExerciseEntity
import com.example.fitnessapi.network.FitnessViewModel
import com.example.fitnessapi.network.UserViewModel

@Composable
fun HomeScreen(navController: NavController) {
    val fitnessViewModel: FitnessViewModel = viewModel() // para exercícios
    val userViewModel: UserViewModel = viewModel() // para usuário

    val savedExercises by fitnessViewModel.savedExercises.observeAsState(emptyList()) // Exercícios salvos
    val context = LocalContext.current // Contexto do Compose
    val user by userViewModel.currentUser.observeAsState(null)

    LaunchedEffect(Unit) {
        userViewModel.loadUser()
    }
    var selectedExercise by remember { mutableStateOf<ExerciseEntity?>(null) }
    var showUserDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        fitnessViewModel.getSavedExercises()
        userViewModel.getUser()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (user == null) {
            Button(
                onClick = { showUserDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Criar Novo Usuário", color = MaterialTheme.colorScheme.onPrimary)
            }
        } else {
            Text(
                "Olá, ${user!!.name}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Text(
            "Exercícios Salvos",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (savedExercises.isEmpty()) {
            Text(
                "Nenhum exercício salvo.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        } else {
            savedExercises.forEach { exercise ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { selectedExercise = exercise },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Alterar", color = MaterialTheme.colorScheme.onPrimary)
                            }

                            Button(
                                onClick = { fitnessViewModel.deleteExercise(exercise) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Deletar", color = MaterialTheme.colorScheme.onError)
                            }

                            Button(
                                onClick = {
                                    val message =
                                        "Confira este exercício: ${exercise.name}\nSéries: ${exercise.series}\nRepetições: ${exercise.repetitions}"
                                    val uri = Uri.parse(
                                        "https://api.whatsapp.com/send?text=${
                                            Uri.encode(message)
                                        }"
                                    )
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                            ) {
                                Text("Compartilhar", color = MaterialTheme.colorScheme.onSecondary)
                            }
                        }
                    }
                }
            }
        }

        Button(
            onClick = { navController.navigate("exerciseCrud") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Criar Novo Exercício", color = MaterialTheme.colorScheme.onPrimary)
        }
    }

    selectedExercise?.let {
        EditExerciseDialog(
            exercise = it,
            onDismiss = { selectedExercise = null },
            onSave = { updatedExercise ->
                fitnessViewModel.updateExercise(updatedExercise)
                selectedExercise = null
            }
        )
    }

    if (showUserDialog) {
        CreateUserDialog(
            onDismiss = { showUserDialog = false },
            onCreate = { name ->
                userViewModel.saveUserToRoom(name)
                showUserDialog = false
            }
        )
    }
}

@Composable
fun CreateUserDialog(onDismiss: () -> Unit, onCreate: (String) -> Unit) {
    var userName by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Criar Novo Usuário")
        },
        text = {
            Column {
                Text("Nome:")
                BasicTextField(
                    value = userName,
                    onValueChange = { userName = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (userName.isNotEmpty()) {
                        onCreate(userName)
                    }
                }
            ) {
                Text("Criar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun EditExerciseDialog(
    exercise: ExerciseEntity,
    onDismiss: () -> Unit,
    onSave: (ExerciseEntity) -> Unit
) {
    var name by remember { mutableStateOf(exercise.name) }
    var series by remember { mutableStateOf(exercise.series.toString()) }
    var repetitions by remember { mutableStateOf(exercise.repetitions.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Editar Exercício",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Nome:")
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                )

                Text("Séries:")
                TextField(
                    value = series,
                    onValueChange = { series = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                )

                Text("Repetições:")
                TextField(
                    value = repetitions,
                    onValueChange = { repetitions = it },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotEmpty() && series.toIntOrNull() != null && repetitions.toIntOrNull() != null) {
                        val updatedExercise = exercise.copy(
                            name = name,
                            series = series.toInt(),
                            repetitions = repetitions.toInt()
                        )
                        onSave(updatedExercise)
                    } else {
                        Log.d("EditExerciseDialog", "Dados inválidos")
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Salvar", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Cancelar", color = MaterialTheme.colorScheme.onSecondary)
            }
        }
    )
}
