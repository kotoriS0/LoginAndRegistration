package com.example.loginandregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.example.loginandregistration.databinding.ActivityLoginBinding
import com.example.loginandregistration.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    var firstName = ""
    var lastName = ""
    var username = ""
    var password = ""
    var checkPassword = ""
    var email = ""

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}