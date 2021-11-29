package com.devtides.coroutinesroom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.devtides.coroutinesroom.model.LoginState
import com.devtides.coroutinesroom.model.User
import com.devtides.coroutinesroom.model.UserDatabase
import kotlinx.coroutines.*

class SignupViewModel(application: Application) : AndroidViewModel(application) {

    val signupComplete = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    private val db by lazy { UserDatabase(getApplication()).userDao() }

    private val coroutineScope= CoroutineScope(Dispatchers.IO)
    val job: Job?=null

    fun signup(username: String, password: String, info: String) {
        coroutineScope.launch {
            val user=db.getUser(username)
            if(user !=null){
                withContext(Dispatchers.Main){
                    error.value="User already exists"
                }
            }
            else{
                val user= User(username,password.hashCode(),info)
                val userId=db.insertUser(user)
                user.id=userId
                LoginState.login(user)
                withContext(Dispatchers.Main){
                    signupComplete.value=true
                }
            }
        }
    }

}