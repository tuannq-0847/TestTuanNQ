package com.example.android_tuannq.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android_tuannq.data.model.RemoteKeys
import com.example.android_tuannq.data.model.User

@Database(entities = [User::class, RemoteKeys::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}