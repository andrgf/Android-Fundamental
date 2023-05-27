package com.example.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.data.local.FavoriteDao
import com.example.githubuser.data.local.FavoriteEntity
import com.example.githubuser.data.local.UserDatabase
import com.example.githubuser.data.remote.DetailUserResponse
import com.example.githubuser.network.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application): AndroidViewModel(application) {

    val user = MutableLiveData<DetailUserResponse>()

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private var dao: FavoriteDao?
    private var database: UserDatabase?

    init {
        database = UserDatabase.getInstance(application)
        dao = database?.favoriteDao()
    }

    fun setDetailUser(username: String){
        ApiClient.instanceApi
            .getUserDetail(username)
            .enqueue(object : Callback<DetailUserResponse>{
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getDetailUser() : LiveData<DetailUserResponse> {
        return user
    }

    fun addToFavorite(avatarUrl: String, htmlUrl: String, id: Int, username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteEntity(
                avatarUrl,
                htmlUrl,
                id,
                username
            )
            dao?.addFavorite(user)
        }
    }

    suspend fun checkUser(id: Int) = dao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            dao?.deleteFavorite(id)
        }
    }
}
