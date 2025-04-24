package com.bittech.manga.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.bittech.manga.local.entity.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM users WHERE isSignedIn = 1 LIMIT 1")
    suspend fun getSignedInUser(): User

    @Query("UPDATE users SET isSignedIn = 0")
    suspend fun signOutAllUsers()
}