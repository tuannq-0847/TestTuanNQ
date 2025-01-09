package com.example.android_tuannq.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android_tuannq.data.model.User
import com.example.android_tuannq.di.AppModule.DB_NAME

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

//    @Query("SELECT * FROM users WHERE label LIKE :query")
//    fun pagingSource(query: String): PagingSource<Int, User>

    @Query("DELETE FROM user")
    suspend fun clearAll()

    @Query("SELECT * FROM user")
    fun getAll(): PagingSource<Int, User>
}