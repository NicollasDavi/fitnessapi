package com.example.fitnessapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fitnessapi.screens.HomeScreen
import com.example.fitnessapi.ui.theme.FitnessAppTheme
import com.example.fitnessapi.network.FitnessViewModel
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtenha o ViewModel
        val fitnessViewModel = ViewModelProvider(this).get(FitnessViewModel::class.java)

        // Chama o método para excluir todos os exercícios
        fitnessViewModel.deleteAllExercises()

        setContent {
            FitnessAppTheme { // Aplica o tema global
                val navController = rememberNavController() // Cria o NavController

                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = "home") { // Definir ponto de entrada
                        composable("home") {
                            HomeScreen(navController = navController) // Tela inicial
                        }
                        composable("exerciseCrud") {
                            ExerciseCrud(navController = navController) // Tela de CRUD de exercício
                        }
                    }
                }
            }
        }
    }
}
