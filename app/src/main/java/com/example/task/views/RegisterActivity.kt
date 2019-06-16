package com.example.task.views

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.task.R
import com.example.task.business.UserBusiness
import com.example.task.repository.UserRepository
import com.example.task.util.ValidationException
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mUserBusiness: UserBusiness

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setListeners()

        mUserBusiness = UserBusiness(this)
    }

    private fun setListeners() {
        buttonSave.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonSave -> {
                handleSave()
            }
        }
    }

    private fun handleSave() {
        try {
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val password = editPassWord.text.toString()

            mUserBusiness.insert(name, email, password)

            startActivity(Intent(this,MainActivity::class.java))
            //mata a activity register
            finish()
            
        } catch (e: ValidationException) {
            Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.erro_inesperado), Toast.LENGTH_LONG).show()
        }
    }
}
