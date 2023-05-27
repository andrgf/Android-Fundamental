package com.example.githubuser.network

import com.example.githubuser.data.remote.DetailUserResponse
import com.example.githubuser.data.remote.User
import com.example.githubuser.data.remote.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET(SEARCH)
    fun getSearchUser(
        @Query("q") username: String
    ): Call<UserResponse>

    @GET(Companion.USER)
    fun getUserDetail(
        @Path("username") username: String
    ) : Call<DetailUserResponse>

    @GET(Companion.FOLLOWERS)
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<User>>

    @GET(Companion.FOLLOWING)
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<User>>



    companion object {
        const val SEARCH = "search/users"
        const val USER = "users/{username}"
        const val FOLLOWERS = "users/{username}/followers"
        const val FOLLOWING = "users/{username}/following"
    }
}