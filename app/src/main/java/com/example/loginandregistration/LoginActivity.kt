package com.example.loginandregistration

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.loginandregistration.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    companion object {
        // values to send in intents are called Extras and have EXTRA_BLAH format for naming key
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
    }

    val startRegistrationForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            // Handle the Intent to do stuff with return info
            binding.editTextLoginUsername.setText(intent?.getStringExtra(LoginActivity.EXTRA_USERNAME))
            binding.editTextLoginPassword.setText(intent?.getStringExtra(LoginActivity.EXTRA_PASSWORD))
        }
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // launch registration activity
        binding.textViewLoginSignUp.setOnClickListener {
            // 1. create an Intent object with current activitu and destination activity's class
            val registrationIntent = Intent(this, RegistrationActivity::class.java)

            // 2. optionally add information to send with the intent
            // key value pairs just like with bundles
            registrationIntent.putExtra(EXTRA_USERNAME, binding.editTextLoginUsername.text.toString())
            registrationIntent.putExtra(EXTRA_PASSWORD, binding.editTextLoginPassword.text.toString())

            //// 3a. launch the new activity using the intent
            //startActivity(registrationIntent)
            //3b. launch the activity for a result using the variable from the register for result contract above
            startRegistrationForResult.launch(registrationIntent)
        }
    }
}