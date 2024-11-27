package com.example.fitnessapi.network

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.fitnessapi.Database.DatabaseProvider
import com.example.fitnessapi.Database.UserDao
import com.example.fitnessapi.Database.UserEntity
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao: UserDao = DatabaseProvider.getDatabase(application).userDao()

    private val _userData = MutableLiveData<UserEntity?>()
    val userData: LiveData<UserEntity?> = _userData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _savedUsers = MutableLiveData<List<UserEntity>>()
    val savedUsers: LiveData<List<UserEntity>> = _savedUsers

    private val _currentUser = MutableLiveData<UserEntity?>()
    val currentUser: LiveData<UserEntity?> = _currentUser

    fun loadUser() {
        viewModelScope.launch {
            val user = userDao.getUserById(0) // Substitua pelo ID ou critério único
            _currentUser.postValue(user)
        }
    }


    // Método para salvar um novo usuário
    fun saveUserToRoom(name: String) {
        if (name.isNotEmpty()) {
            viewModelScope.launch {
                try {
                    val newUser = UserEntity(name = name)
                    userDao.insertUser(newUser)
                    Log.d("UserViewModel", "Usuário salvo: $name")
                    getUser() // Atualiza os dados do usuário atual
                } catch (e: Exception) {
                    Log.e("UserViewModel", "Erro ao salvar usuário: ${e.message}")
                }
            }
        }
    }

    // Método para obter o usuário (caso exista)
    fun getUser() {
        viewModelScope.launch {
            try {
                val user = userDao.getUserById(0) // Altere o ID conforme necessário
                _userData.postValue(user)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erro ao obter usuário: ${e.message}")
            }
        }
    }

    // Método para deletar um usuário específico
    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            try {
                userDao.deleteUser(user)
                Log.d("UserViewModel", "Usuário deletado: ${user.name}")
                getUser() // Atualiza os dados após a exclusão
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erro ao deletar usuário: ${e.message}")
            }
        }
    }

    // Método para atualizar um usuário
    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            try {
                userDao.updateUser(user)
                Log.d("UserViewModel", "Usuário atualizado: ${user.name}")
                getUser() // Atualiza os dados após a edição
            } catch (e: Exception) {
                Log.e("UserViewModel", "Erro ao atualizar usuário: ${e.message}")
            }
        }
    }
}
