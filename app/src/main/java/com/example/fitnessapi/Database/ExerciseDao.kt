package com.example.fitnessapi.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insert(exercise: ExerciseEntity)

    @Insert
    suspend fun insertAll(exercises: List<ExerciseEntity>)

    @Query("SELECT * FROM exercise_table")
    fun getAllExercises(): LiveData<List<ExerciseEntity>>

    @Query("SELECT * FROM exercise_table WHERE name = :name LIMIT 1")
    suspend fun getExerciseByName(name: String): ExerciseEntity?

    @Query("DELETE FROM exercise_table")
    suspend fun deleteAllExercises()

    @Query("DELETE FROM exercise_table WHERE id = :id")
    suspend fun deleteExercise(id: Int)

    @Query("UPDATE exercise_table SET name = :name, series = :series, repetitions = :repetitions WHERE id = :id")
    suspend fun updateExercise(id: Int, name: String, series: Int, repetitions: Int)
}
