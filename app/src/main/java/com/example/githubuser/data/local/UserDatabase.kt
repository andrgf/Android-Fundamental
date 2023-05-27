package com.example.githubuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class],
    version = 1
)
abstract class UserDatabase: RoomDatabase() {

    companion object {
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase? {
            if (INSTANCE==null){
                synchronized(UserDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "zonehub.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun favoriteDao(): FavoriteDao
}