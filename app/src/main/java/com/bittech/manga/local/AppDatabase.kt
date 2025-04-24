package com.bittech.manga.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bittech.manga.local.dao.MangaDao
import com.bittech.manga.local.dao.UserDao
import com.bittech.manga.local.entity.Manga
import com.bittech.manga.local.entity.User

@Database(entities = [User::class, Manga::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun mangaDao(): MangaDao
}