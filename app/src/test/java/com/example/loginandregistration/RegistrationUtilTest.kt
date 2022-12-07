package com.example.loginandregistration

import com.google.common.truth.Truth.assertThat
import com.mistershorr.loginandregistration.RegistrationUtil
import org.junit.Test

class RegistrationUtilTest {
    // methodName_someCondition_expectedResult

    // make test for: (make sure example follow all other guidelines)
        // empty password
        // passwords don't match
        // min length of 8 characters
        // at least one digit
        // at least one capital letter
        // good matching passwords working
    // make tests for other functions in the Util class with common failures and 1 success for each

    @Test
    fun validatePassword_emptyPassword_isFalse() {
        val actual = RegistrationUtil.validatePassword("", "")
        assertThat(actual).isFalse()
    }

    @Test
    fun validatePassword_passwordsDontMatch_isFalse() {
        val actual = RegistrationUtil.validatePassword("Password1!", "notPassword1!")
        assertThat(actual).isFalse()
    }

    @Test
    fun validatePassword_notLongEnough_isFalse() {
        val actual = RegistrationUtil.validatePassword("Pass1!", "Pass1!")
        assertThat(actual).isFalse()
    }

    @Test
    fun validatePassword_noDigit_isFalse() {
        val actual = RegistrationUtil.validatePassword("Password!", "Password!")
        assertThat(actual).isFalse()
    }

    @Test
    fun validatePassword_noCapitalLetter_isFalse() {
        val actual = RegistrationUtil.validatePassword("password1!", "password1!")
        assertThat(actual).isFalse()
    }

    @Test
    fun validatePassword_goodPasswords_isTrue() {
        val actual = RegistrationUtil.validatePassword("Password1!", "Password1!")
        assertThat(actual).isTrue()
    }

    @Test
    fun validateName_emptyNames_isFalse() {
        val actual = RegistrationUtil.validateName("", "")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateName_goodNames_isTrue() {
        val actual = RegistrationUtil.validateName("Name", "Name")
        assertThat(actual).isTrue()
    }

    @Test
    fun validateUsername_emptyUsername_isFalse() {
        val actual = RegistrationUtil.validateUsername("")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateUsername_notLongEnough_isFalse() {
        val actual = RegistrationUtil.validateUsername("aa")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateUsername_goodUsername_isTrue() {
        val actual = RegistrationUtil.validateUsername("User")
        assertThat(actual).isTrue()
    }

    @Test
    fun validateEmail_emptyEmail_isFalse() {
        val actual = RegistrationUtil.validateEmail("")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateEmail_wrongFormat_isFalse() {
        val actual = RegistrationUtil.validateEmail("email")
        assertThat(actual).isFalse()
    }

    @Test
    fun validateEmail_goodEmail_isTrue() {
        val actual = RegistrationUtil.validateEmail("email@email.com")
        assertThat(actual).isTrue()
    }
}