package com.example.fitnessapi.Database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var INSTANCE: ExerciseDatabase? = null

    fun getDatabase(context: Context): ExerciseDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ExerciseDatabase::class.java,
                "exercise_database" // Nome do banco de dados
            )
                .fallbackToDestructiveMigration() // Força a recriação do banco de dados em caso de mudanças de versão
                .build()
            INSTANCE = instance
            instance
        }
    }
}
