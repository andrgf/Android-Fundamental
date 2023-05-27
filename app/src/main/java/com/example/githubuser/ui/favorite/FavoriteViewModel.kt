package com.example.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuser.data.local.FavoriteDao
import com.example.githubuser.data.local.FavoriteEntity
import com.example.githubuser.data.local.UserDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var dao: FavoriteDao?
    private var database: UserDatabase?

    init {
        database = UserDatabase.getInstance(application)
        dao = database?.favoriteDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteEntity>>? {
        return dao?.getFavoriteUser()
    }

}