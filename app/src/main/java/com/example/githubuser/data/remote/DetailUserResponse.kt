package com.example.githubuser.data.remote


import com.google.gson.annotations.SerializedName

data class DetailUserResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String,
    @SerializedName("followers")
    val followers: Int,
    @SerializedName("followers_url")
    val followersUrl: String,
    @SerializedName("following")
    val following: Int,
    @SerializedName("following_url")
    val followingUrl: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("login")
    val login: String,
    @SerializedName("name")
    val name: String
)