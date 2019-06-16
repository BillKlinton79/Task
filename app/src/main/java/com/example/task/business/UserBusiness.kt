package com.example.task.business

import android.content.Context
import com.example.task.repository.UserRepository

class UserBusiness(val context: Context) {
    val mUserRepository = UserRepository.getInstance(context)
    fun insert(name: String, email: String, password: String){

        val userId = mUserRepository.insert(name,email,password)
    }
}