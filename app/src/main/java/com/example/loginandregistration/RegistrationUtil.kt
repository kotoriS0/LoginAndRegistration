package com.mistershorr.loginandregistration

import android.text.TextUtils.indexOf
import android.util.Log
import com.mistershorr.loginandregistration.RegistrationUtil.existingEmails

// object keyword makes it so all the functions are
// static functions
object RegistrationUtil {

    val TAG = "Registration Util"
    // use this in the test class for the is username taken test
    // make another similar list for some taken emails
    var existingUsers = listOf("cosmicF", "cosmicY", "bob", "alice")
    var existingEmails = listOf("cosmicF@mail.com", "cosmicY@mail.com", "bob@mail.com", "alice@mail.com")
//    you can use listOf<type>() instead of making the list & adding individually
//    List<String> blah = new ArrayList<String>();
//    blah.add("hi")
//    blah.add("hello")
//

    // isn't empty
    // already taken
    // minimum number of characters is 3
    fun validateUsername(username: String?) : Boolean {
        if(username == null || username.length <=3) {
            return false
        }
        for(name in existingUsers) {
            if(name.equals(username)) {
                return false
            }
        }
        return true
    }

    // make sure meets security requirements (deprecated ones that are still used everywhere)
    // min length 8 chars
    // at least one digit
    // at least one capital letter
    // both passwords match
    // not empty
    fun validatePassword(password : String?, confirmPassword: String?) : Boolean {
        if(password == null || confirmPassword == null || password.length <= 8 || !password.equals(confirmPassword)) {
            return false
        }
        var passwordTest = password.toCharArray()
        var digit = false
        var capital = false
        for(character in passwordTest) {
            if(character.isDigit()) {
                digit = true
            }
            if(character.isUpperCase()) {
                capital = true
            }
        }
        return digit && capital

    }

    // isn't empty
    fun validateName(firstName: String?, lastName: String?) : Boolean {
        return !(firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty())
    }

    // isn't empty
    // make sure the email isn't used
    // make sure it's in the proper email format user@domain.tld
    fun validateEmail(email: String?) : Boolean {
        if(email == null || email.length <= 5) {
            return false
        }
        for(mail in existingEmails) {
            if(mail == email) {
                return false
            }
        }
        if(email.indexOf("@") >= 2 && email.lastIndexOf(".") >= email.indexOf("@") + 2) {
            return true
        }
        return false
        /*val emailPattern = """^[a-z0-9._+@[a-z]+[.][a-z]+$""".toRegex()
        return emailPattern.matches(email.toLowerCase)

        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()*/
    }
}