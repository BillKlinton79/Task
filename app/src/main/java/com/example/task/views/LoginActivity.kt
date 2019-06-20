package com.example.task.views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.task.R
import com.example.task.business.UserBusiness
import com.example.task.constants.TaskConstants
import com.example.task.util.SecurityPreferences
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserBusiness: UserBusiness
    private lateinit var mSecurityPreferences: SecurityPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mSecurityPreferences = SecurityPreferences(this)

        mUserBusiness = UserBusiness(this)

        setListeners()
        verifyLoggedUser()
    }

    private fun verifyLoggedUser() {
        val userId = mSecurityPreferences.getStoredString(TaskConstants.KEY.USER_ID)
        val userName = mSecurityPreferences.getStoredString(TaskConstants.KEY.USER_NAME)

        if(userId != "" && userName != ""){
            startActivity(Intent(this,MainActivity::class.java))
            //mata a activity login
            finish()
        }
    }

    private fun setListeners() {
        buttonLogin.setOnClickListener(this)
        textRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonLogin -> {
                handleLogin()
            }
            R.id.textRegister ->{
                startActivity(Intent(this,RegisterActivity::class.java))
            }
        }
    }

    private fun handleLogin() {
        val email = editEmail.text.toString()
        val password = editPassWord.text.toString()

        if(!mUserBusiness.login(email,password)){
            Toast.makeText(this,getString(R.string.email_senha_incorretos),Toast.LENGTH_LONG).show()
        }else{
            startActivity(Intent(this,MainActivity::class.java))

            //mata a activity login
            finish()
        }
    }

}
