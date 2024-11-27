package com.example.fitnessapi.network

data class WgerResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Exercise> // Lista de exercícios
)

data class Exercise(
    val id: Int,
    val name: String,
    val description: String,
    val category: String
    // Adicione mais campos conforme necessário
)

data class WgerExercise(
    val id: Int,
    val name: String,
    val description: String // Se necessário, adicione mais campos conforme a resposta da API
)

