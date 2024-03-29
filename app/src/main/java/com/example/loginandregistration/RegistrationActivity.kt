package com.example.loginandregistration

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
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

            when {
                RegistrationUtil.validateName(firstName) -> {
                    binding.editTextRegisterFirstName.setTextColor(Color.GRAY)
                }
                RegistrationUtil.validateUsername(username) -> {
                    binding.editTextRegisterUsername.setTextColor(Color.GRAY)
                }
                RegistrationUtil.validatePassword(password, confirm) -> {
                    binding.editTextRegisterPassword.setTextColor(Color.GRAY)
                    binding.editTextRegisterPasswordCheck.setTextColor(Color.GRAY)
                }
                RegistrationUtil.validateEmail(email) -> {
                    binding.editTextRegisterEmail.setTextColor(Color.GRAY)
                }
            }

            if(RegistrationUtil.validateName(firstName) &&
                RegistrationUtil.validateUsername(username) &&
                RegistrationUtil.validatePassword(password, confirm) &&
                RegistrationUtil.validateEmail(email)) {

                Backendless.UserService.register(registerUserOnBackendless(username, password, firstName, email, lastName), object:
                    AsyncCallback<BackendlessUser> {
                    override fun handleResponse(registeredUser: BackendlessUser)
                    {
                        // user has been registered and now can login
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

                    override fun handleFault(fault: BackendlessFault)
                    {
                        // an error has occurred, the error code can be retrieved with fault.getCode()
                        Toast.makeText(it.context, "Error processing registration request", Toast.LENGTH_SHORT).show()
                    }
                } )
            }
            else {
                Toast.makeText(this, "Invalid Data, all queries must be properly filled in", Toast.LENGTH_SHORT).show()
                when {
                    !RegistrationUtil.validateName(firstName) -> {
                        binding.editTextRegisterFirstName.setTextColor(Color.RED)
                    }
                    !RegistrationUtil.validateUsername(username) -> {
                        binding.editTextRegisterUsername.setTextColor(Color.RED)
                    }
                    !RegistrationUtil.validatePassword(password, confirm) -> {
                        binding.editTextRegisterPassword.setTextColor(Color.RED)
                        binding.editTextRegisterPasswordCheck.setTextColor(Color.RED)
                    }
                    !RegistrationUtil.validateEmail(email) -> {
                        binding.editTextRegisterEmail.setTextColor(Color.RED)
                    }
                }
            }
        }
    }

    fun registerUserOnBackendless(username: String, password: String, name: String, email: String, lastName: String?) : BackendlessUser{
        var user = BackendlessUser()
        user.setProperty("username", username)
        user.setProperty("password", password)
        user.setProperty("name", name)
        user.setProperty("email", email)
        if(lastName != null)
            user.setProperty("lastName", lastName)
        return user
    }
}