package com.example.android_tuannq.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RemoteKeys")
data class RemoteKeys(
    @PrimaryKey var id: String = "", val prevKey: Int?, val nextKey: Int?
)
