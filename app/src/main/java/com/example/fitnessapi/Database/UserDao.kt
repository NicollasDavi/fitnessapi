package com.example.fitnessapi.Database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // Inserir um novo usuário
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Atualizar um usuário existente
    @Update
    suspend fun updateUser(user: UserEntity)

    // Excluir um usuário
    @Delete
    suspend fun deleteUser(user: UserEntity)

    // Buscar usuário por ID
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    // Buscar todos os usuários
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>
}
