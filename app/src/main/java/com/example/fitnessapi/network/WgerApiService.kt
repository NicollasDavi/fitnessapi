package com.example.fitnessapi.network

import retrofit2.http.GET
import retrofit2.http.Query

interface WgerApiService {

    // Busca exercícios base com filtros adicionais
    @GET("exercise-base/")
    suspend fun getExercisesBase(
        @Query("language") language: Int = 2,  // Idioma fixo
        @Query("category") category: Int? = null, // Parâmetro opcional de categoria
        @Query("equipment") equipment: List<Int>? = null, // Parâmetro opcional de equipamentos
        @Query("muscles") muscles: List<Int>? = null, // Parâmetro opcional de músculos primários
        @Query("muscles_secondary") musclesSecondary: List<Int>? = null, // Músculos secundários
        @Query("limit") limit: Int = 100,  // Número de resultados por página
        @Query("offset") offset: Int = 0, // Posição inicial para a pesquisa (paginado)
        @Query("ordering") ordering: String? = null // Ordem dos resultados (opcional)
    ): WgerResponse

    @GET("exercise/")
    suspend fun getExerciseList(
        @Query("language") language: Int = 2,  // Idioma fixo
        @Query("name") name: String? = null,  // Parâmetro opcional de nome
        @Query("limit") limit: Int = 100,  // Número de resultados por página
        @Query("offset") offset: Int = 0, // Posição inicial para a pesquisa (paginado)
        @Query("ordering") ordering: String? = null // Ordem dos resultados (opcional)
    ): WgerResponse


    // Busca exercícios com base na pesquisa e parâmetros adicionais
    @GET("exercise-base/")
    suspend fun getExerciseBySearchQuery(
        @Query("search") searchQuery: String,  // Parâmetro de pesquisa
        @Query("language") language: Int = 2,  // Idioma fixo
        @Query("category") category: Int? = null, // Parâmetro opcional de categoria
        @Query("equipment") equipment: List<Int>? = null, // Parâmetro opcional de equipamentos
        @Query("muscles") muscles: List<Int>? = null, // Parâmetro opcional de músculos primários
        @Query("muscles_secondary") musclesSecondary: List<Int>? = null, // Músculos secundários
        @Query("limit") limit: Int = 100,  // Número de resultados por página
        @Query("offset") offset: Int = 0, // Posição inicial para a pesquisa (paginado)
        @Query("ordering") ordering: String? = null // Ordem dos resultados (opcional)
    ): WgerResponse
}
