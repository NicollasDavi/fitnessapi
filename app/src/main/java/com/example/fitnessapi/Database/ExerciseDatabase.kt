package com.example.fitnessapi.Database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExerciseEntity::class, UserEntity::class], version = 2)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun userDao(): UserDao
}
