package com.example.loginandregistration

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.example.loginandregistration.databinding.ActivityLoginBinding
import com.example.loginandregistration.databinding.ActivityRegistrationBinding
import com.mistershorr.loginandregistration.RegistrationUtil

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // retrieve any information from the intent using the extras keus
        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME) ?: ""
        val password = intent.getStringExtra(LoginActivity.EXTRA_PASSWORD) ?: ""

        // prefill the username and password fields
        binding.editTextRegisterUsername.setText(username)
        binding.editTextRegisterPassword.setText(password)

        binding.editTextRegisterUsername.setOnClickListener {
            Toast.makeText(this, "Minimum 3 characters", Toast.LENGTH_SHORT).show()
        }
        binding.editTextRegisterPassword.setOnClickListener {
            Toast.makeText(this, "Password must have 8 characters, 1 digit and 1 capital letter", Toast.LENGTH_SHORT).show()
        }

        // register an account and send back username and password to login to prefil
        binding.buttonRegisterRegister.setOnClickListener {
            val firstName = binding.editTextRegisterFirstName.text.toString()
            val lastName = binding.editTextRegisterLastName.text.toString()
            val username = binding.editTextRegisterUsername.text.toString()
            val password = binding.editTextRegisterPassword.text.toString()
            val confirm = binding.editTextRegisterPasswordCheck.text.toString()
            val email = binding.editTextRegisterEmail.text.toString()
            if(RegistrationUtil.validateName(firstName, lastName) &&
                RegistrationUtil.validateUsername(username) &&
                RegistrationUtil.validatePassword(password, confirm) &&
                RegistrationUtil.validateEmail(email)) {
                // apply lambda calls functions inside on object that apply is called on
                val resultIntent = Intent().apply {
                    // apply { putExtra } is doing same thing as resultIntent.putExtra()
                    putExtra(
                        LoginActivity.EXTRA_USERNAME,
                        binding.editTextRegisterUsername.text.toString()
                    )
                    putExtra(
                        LoginActivity.EXTRA_PASSWORD,
                        binding.editTextRegisterPassword.text.toString()
                    )
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            else {
                Toast.makeText(this, "Invalid Data, all queries must be properly filled in", Toast.LENGTH_SHORT).show()
            }
        }
    }
}