package com.example.githubuser.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.example.githubuser.data.remote.User
import com.example.githubuser.data.remote.UserResponse
import com.example.githubuser.network.ApiClient
import com.example.githubuser.ui.setting.SettingsPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val pref: SettingsPreferences) : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun setSearchUsers(query: String) {
        ApiClient.instanceApi
            .getSearchUser(query)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.d("Failure", t.message.toString())
                }
            })
    }

    fun getSearchUser() : LiveData<ArrayList<User>> {
        return listUsers
    }

    fun getThemeSetting(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }
}