package com.bittech.manga.ui.auth

import com.bittech.manga.local.dao.UserDao
import com.bittech.manga.local.entity.User
import com.bittech.manga.utils.AppPreferences
import javax.inject.Inject

class LoginRepository @Inject constructor(private val userDao: UserDao, private val appPreferences: AppPreferences) {


    suspend fun loginOrSignUp(email: String, password: String): Boolean {
        val user = userDao.getUserByEmail(email)
        userDao.signOutAllUsers()
        return if (user == null) {
            userDao.insertUser(User(email = email, password = password, isSignedIn = true))
            appPreferences.setSignIn(true)
            true
        } else if (user.password == password) {
            userDao.updateUser(user.copy(isSignedIn = true))
            appPreferences.setSignIn(true)
            true
        } else {
            false
        }
    }

    suspend fun getSignedInUser(): User {
        return userDao.getSignedInUser()
    }

    suspend fun logOut() {
        userDao.signOutAllUsers()
        appPreferences.clear()
    }

    fun isSignedIn(): Boolean {
        return appPreferences.isSignIn()
    }
}