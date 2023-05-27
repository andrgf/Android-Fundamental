package com.example.githubuser.ui.detail.followers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.remote.User
import com.example.githubuser.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel: ViewModel() {

    val list = MutableLiveData<ArrayList<User>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun setListFollowers(username: String) {
        ApiClient.instanceApi
            .getFollowers(username)
            .enqueue(object : Callback<ArrayList<User>>{
                override fun onResponse(
                    call: Call<ArrayList<User>>,
                    response: Response<ArrayList<User>>
                ) {
                    if (response.isSuccessful) {
                        list.postValue(response.body())
                    }

                }

                override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getListFollowers(): LiveData<ArrayList<User>> {
        return list
    }
}