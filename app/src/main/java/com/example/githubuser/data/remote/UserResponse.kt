package com.example.githubuser.data.remote


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("items")
    val items: ArrayList<User>
)