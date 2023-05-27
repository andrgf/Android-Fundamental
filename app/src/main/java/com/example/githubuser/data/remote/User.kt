package com.example.githubuser.data.remote


import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("html_url")
    val htmlUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String
)