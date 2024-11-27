package com.example.fitnessapi

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.fitnessapi.network.Exercise
import com.example.fitnessapi.network.FitnessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseCrud(navController: NavController) {
    val viewModel: FitnessViewModel = viewModel() // Obtém o ViewModel
    val exerciseData by viewModel.exerciseData.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()

    var searchQuery by remember { mutableStateOf("") }

    // Estado para o modal
    var selectedExercise by remember { mutableStateOf<Exercise?>(null) }
    var series by remember { mutableStateOf(3) }
    var repetitions by remember { mutableStateOf(12) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background // Usa a cor de fundo do tema
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botão de voltar
            Button(
                onClick = { navController.popBackStack() },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Voltar", color = MaterialTheme.colorScheme.onSurface)
            }

            // Campo de texto para buscar exercícios
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it.trim() },
                label = { Text("Buscar Exercício") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )

            // Botão para buscar exercícios
            Button(
                onClick = { viewModel.getExerciseListData(name = searchQuery) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Buscar Exercícios", color = MaterialTheme.colorScheme.onPrimary)
            }

            // Exibindo a lista de exercícios
            exerciseData?.results?.let { exercises ->
                Text("Exercícios encontrados:", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(8.dp))
                exercises.forEach { exercise ->
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Button(
                            onClick = { selectedExercise = exercise },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(exercise.name, color = MaterialTheme.colorScheme.onSecondary)
                        }
                    }
                }
            }

            // Modal de inserção de séries e repetições
            selectedExercise?.let {
                AlertDialog(
                    onDismissRequest = { selectedExercise = null },
                    title = { Text("Adicionar Informações") },
                    text = {
                        Column {
                            Text("Séries", style = MaterialTheme.typography.bodyMedium)
                            OutlinedTextField(
                                value = series.toString(),
                                onValueChange = { series = it.toIntOrNull() ?: 3 },
                                label = { Text("Número de Séries") },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    cursorColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("Repetições", style = MaterialTheme.typography.bodyMedium)
                            OutlinedTextField(
                                value = repetitions.toString(),
                                onValueChange = { repetitions = it.toIntOrNull() ?: 12 },
                                label = { Text("Número de Repetições") },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    cursorColor = MaterialTheme.colorScheme.primary
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (series > 0 && repetitions > 0) {
                                    selectedExercise?.let { exercise ->
                                        viewModel.saveExerciseToRoom(exercise.name, series, repetitions)
                                        selectedExercise = null // Fecha o modal após salvar
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("Salvar Exercício", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { selectedExercise = null },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Text("Cancelar", color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                )
            }

            // Exibição de status
            when {
                isLoading -> Text("Carregando...", color = MaterialTheme.colorScheme.onSurface)
                !errorMessage.isNullOrEmpty() -> Text(errorMessage!!, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
