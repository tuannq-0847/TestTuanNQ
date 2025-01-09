package com.example.android_tuannq.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
open class User {
    @SerializedName("id")
    @PrimaryKey
    var id: String = ""

    @SerializedName("login")
    var name: String? = null

    @SerializedName("avatar_url")
    var url: String? = null

    @SerializedName("html_url")
    var htmlUrl: String? = null

    override fun toString(): String {
        return "id: $id name: $name url: $url htmlUrl: $htmlUrl"
    }
}
