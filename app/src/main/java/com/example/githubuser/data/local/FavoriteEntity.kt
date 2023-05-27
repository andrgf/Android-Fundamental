package com.example.githubuser.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "favorite_entity")
data class FavoriteEntity(
    val avatarUrl: String,
    val htmlUrl: String,
    @PrimaryKey
    val id: Int,
    val login: String
) : Serializable