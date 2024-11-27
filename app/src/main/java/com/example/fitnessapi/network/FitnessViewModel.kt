package com.example.fitnessapi.network

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fitnessapi.Database.DatabaseProvider
import com.example.fitnessapi.Database.ExerciseDao
import com.example.fitnessapi.Database.ExerciseEntity
import com.example.wgerapi.network.RetrofitInstance
import com.example.fitnessapi.network.WgerResponse
import kotlinx.coroutines.launch

class FitnessViewModel(application: Application) : AndroidViewModel(application) {

    private val exerciseDao: ExerciseDao = DatabaseProvider.getDatabase(application).exerciseDao()

    private val _exerciseData = MutableLiveData<WgerResponse?>()
    val exerciseData: LiveData<WgerResponse?> = _exerciseData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _savedExercises = MutableLiveData<List<ExerciseEntity>>()
    val savedExercises: LiveData<List<ExerciseEntity>> = _savedExercises

    // Método para carregar os exercícios salvos
    fun getSavedExercises() {
        viewModelScope.launch {
            val exercises = exerciseDao.getAllExercises() // Retorna LiveData, sem .value
            exercises.observeForever { loadedExercises ->
                // Aqui o Observer será chamado sempre que a lista de exercícios mudar
                Log.d("FitnessViewModel", "Exercícios carregados: ${loadedExercises.size} - $loadedExercises")
                _savedExercises.value = loadedExercises
            }
        }
    }

    // Método para salvar o exercício
    fun saveExerciseToRoom(name: String, series: Int, repetitions: Int) {
        if (series > 0 && repetitions > 0) {
            viewModelScope.launch {
                // Verificar se o exercício já existe
                val existingExercise = exerciseDao.getExerciseByName(name)
                if (existingExercise == null) {
                    val exerciseEntity = ExerciseEntity(name = name, series = series, repetitions = repetitions)
                    exerciseDao.insert(exerciseEntity)
                    Log.d("FitnessViewModel", "Exercício salvo: $name")
                    getSavedExercises()  // Carregar os exercícios depois de salvar
                } else {
                    Log.d("FitnessViewModel", "Exercício já existe: $name")
                }
            }
        } else {
            _errorMessage.value = "Por favor, defina séries e repetições."
        }
    }

    // Método para deletar todos os exercícios
    fun deleteAllExercises() {
        viewModelScope.launch {
            exerciseDao.deleteAllExercises()
            Log.d("FitnessViewModel", "Todos os exercícios foram removidos")
            _savedExercises.value = emptyList()  // Limpar a lista carregada
        }
    }

    // Método para deletar um exercício específico
    fun deleteExercise(exercise: ExerciseEntity) {
        viewModelScope.launch {
            // Deleta o exercício do banco de dados
            exerciseDao.deleteExercise(exercise.id)

            // Atualiza a lista de exercícios depois da exclusão
            exerciseDao.getAllExercises().observeForever { updatedExercises ->
                _savedExercises.value = updatedExercises // Atualiza a lista de exercícios no LiveData
                Log.d("FitnessViewModel", "Exercício deletado: ${exercise.name}")
            }
        }
    }

    fun updateExercise(exercise: ExerciseEntity) {
        viewModelScope.launch {
            // Atualizar o exercício no banco de dados
            exerciseDao.updateExercise(exercise.id, exercise.name, exercise.series, exercise.repetitions)

            // Recarregar os exercícios após a atualização
            exerciseDao.getAllExercises().observeForever { updatedExercises ->
                _savedExercises.value = updatedExercises
                Log.d("FitnessViewModel", "Exercício atualizado: ${exercise.name}")
            }
        }
    }


    // Método para buscar dados de exercícios de uma API externa
    fun getExerciseListData(name: String? = null, limit: Int = 100, offset: Int = 0) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val response: WgerResponse = RetrofitInstance.wgerApiService.getExerciseList(
                    name = name,
                    limit = limit,
                    offset = offset
                )

                if (response.results.isNullOrEmpty()) {
                    _errorMessage.value = "Nenhum exercício encontrado para sua pesquisa."
                    _exerciseData.value = null
                } else {
                    _exerciseData.value = response
                }

            } catch (e: Exception) {
                Log.e("FitnessViewModel", "Erro ao carregar os exercícios: ${e.message}")
                _errorMessage.value = "Erro ao carregar os exercícios: ${e.message}"
                _exerciseData.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}
