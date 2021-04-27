package com.university.gradetrak

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.university.gradetrak.models.Student
import com.university.gradetrak.ui.signUp.SignUpActivity
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class SignUpInstrumentedTest {
    private val studentExists = Student(null, "Dan", "Thompson")
    private val studentDoesNotExist = Student(null, "Bob", "Morris")
    private val validEmailDoesNotExist = "bob@bob.com"
    private val validEmailThatExists = "dan@dan.com"
    private val validPasswordThatExists = "123456"
    private val invalidEmail = "InvalidEmail"
    private val password = "Password"
    private val passwordDoesNotMatch = "Password1"

    @get:Rule
    var activityRule: ActivityScenarioRule<SignUpActivity> =
        ActivityScenarioRule(SignUpActivity::class.java)

    @Before
    fun setup(){
        clearData()
    }

    private fun clearData(){
        onView(withId(R.id.et_sign_up_page_first_name)).perform(clearText())
        onView(withId(R.id.et_sign_up_page_surname)).perform(clearText())
        onView(withId(R.id.et_sign_up_page_email)).perform(clearText())
        onView(withId(R.id.et_sign_up_page_password)).perform(clearText())
        onView(withId(R.id.et_sign_up_page_confirm_password)).perform(clearText())
    }

    private fun clickRegisterButton(){
        onView(withId(R.id.btn_sign_up_page_register)).perform(click())
    }

    private fun checkIsOnSignUpPage(){
        onView(withId(R.id.et_sign_up_page_first_name))
    }

    private fun registerAndConfirmOnSignUpPage(){
        clickRegisterButton()
        checkIsOnSignUpPage()
    }

    private fun enterFirstName(firstName: String){
        onView(withId(R.id.et_sign_up_page_first_name))
            .perform(ViewActions.typeText(firstName))
    }

    private fun enterSurname(surname: String){
        onView(withId(R.id.et_sign_up_page_surname))
            .perform(ViewActions.typeText(surname))
    }

    private fun enterEmail(email: String){
        onView(withId(R.id.et_sign_up_page_email))
            .perform(ViewActions.typeText(email))
    }

    private fun enterPassword(password: String){
        onView(withId(R.id.et_sign_up_page_password))
            .perform(ViewActions.typeText(password))
    }

    private fun enterConfirmPassword(password: String){
        onView(withId(R.id.et_sign_up_page_confirm_password))
            .perform(ViewActions.typeText(password))
    }

    private fun checkIsOnLoginPage(){
        onView(withId(R.id.fl_login_header_image))
    }

    @Test
    fun test01_try_register_with_no_first_name(){
        registerAndConfirmOnSignUpPage()
    }

    @Test
    fun test02_try_register_with_no_surname(){
        enterFirstName(studentDoesNotExist.firstName!!)
        registerAndConfirmOnSignUpPage()
    }

    @Test
    fun test03_try_register_with_no_email(){
        enterFirstName(studentDoesNotExist.firstName!!)
        enterSurname(studentDoesNotExist.surname!!)
        registerAndConfirmOnSignUpPage()
    }

    @Test
    fun test04_try_register_with_invalid_email(){
        enterFirstName(studentDoesNotExist.firstName!!)
        enterSurname(studentDoesNotExist.surname!!)
        enterEmail(invalidEmail)
        registerAndConfirmOnSignUpPage()
    }

    @Test
    fun test05_try_register_with_no_password(){
        enterFirstName(studentDoesNotExist.firstName!!)
        enterSurname(studentDoesNotExist.surname!!)
        enterEmail(validEmailDoesNotExist)
        registerAndConfirmOnSignUpPage()
    }

    @Test
    fun test06_try_register_with_no_confirm_password(){
        enterFirstName(studentDoesNotExist.firstName!!)
        enterSurname(studentDoesNotExist.surname!!)
        enterEmail(validEmailDoesNotExist)
        enterPassword(password)
        registerAndConfirmOnSignUpPage()
    }

    @Test
    fun test07_try_register_with_not_matching_confirm_password(){
        enterFirstName(studentDoesNotExist.firstName!!)
        enterSurname(studentDoesNotExist.surname!!)
        enterEmail(validEmailDoesNotExist)
        enterPassword(password)
        enterConfirmPassword(passwordDoesNotMatch)
        registerAndConfirmOnSignUpPage()
    }

    @Test
    fun test08_try_register_with_already_existing_credentials(){
        enterFirstName(studentExists.firstName!!)
        enterSurname(studentExists.surname!!)
        enterEmail(validEmailThatExists)
        enterPassword(validPasswordThatExists)
        enterConfirmPassword(validPasswordThatExists)
        registerAndConfirmOnSignUpPage()
    }

    @Test
    fun test09_try_register_with_valid_information_that_does_not_exist(){
        enterFirstName(studentDoesNotExist.firstName!!)
        enterSurname(studentDoesNotExist.surname!!)
        enterEmail(validEmailDoesNotExist)
        enterPassword(password)
        enterConfirmPassword(password)
        checkIsOnLoginPage()
    }

    @Test
    fun test10_try_press_back_button_toolbar_navigation(){

    }
}