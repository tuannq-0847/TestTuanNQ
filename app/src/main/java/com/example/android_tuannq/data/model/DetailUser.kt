package com.example.android_tuannq.data.model

import com.google.gson.annotations.SerializedName

data class DetailUser(
    @SerializedName("location") val location: String? = null,
    @SerializedName("followers") val followers: Int? = null,
    @SerializedName("following") val following: Int? = null
): User()