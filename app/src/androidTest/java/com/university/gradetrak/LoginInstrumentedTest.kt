package com.university.gradetrak

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.university.gradetrak.ui.login.LoginActivity
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@LargeTest

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class LoginInstrumentedTest {
    private val studentExistsEmail = "dan@dan.com"
    private val studentExistPassword = "123456"
    private val studentDoesNotExistEmail = "doesnotexist@doesnotexist.com"
    private val studentDoesNotExistPassword = "doesNotExist"
    private val emptyString = ""


    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> =
        ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setup(){
//        Insert any setup logic
    }

    private fun enterEmail(email: String){
        onView(withId(R.id.et_login_page_email_address))
            .perform(typeText(email))
    }

    private fun enterPassword(email: String){
        onView(withId(R.id.et_login_page_password))
            .perform(typeText(email))
    }

    private fun clearData(){
        onView(withId(R.id.et_login_page_email_address)).perform(clearText())
        onView(withId(R.id.et_login_page_password)).perform(clearText())
    }

    private fun clickLoginButton(){
        onView(withId(R.id.btn_login)).perform(click())
    }

    private fun clickSignUpButton(){
        onView(withId(R.id.btn_sign_up)).perform(click())
    }

    private fun clickForgotPasswordButton(){
        onView(withId(R.id.btn_forgot_password)).perform(click())
    }

    private fun checkIsOnLoginPage(){
        onView(withId(R.id.fl_login_header_image))
    }

    private fun checkIsOnModulesPage(){
        onView(withId(R.id.et_module_name))
    }

    private fun checkIsOnSignUpPage(){
        onView(withId(R.id.et_sign_up_page_first_name))
    }

    private fun checkIsOnForgotPasswordPage(){
        onView(withId(R.id.et_forgot_password_page_email_address))
    }


    @Test
    fun test01_Login_Activity_Loaded() {
        checkIsOnLoginPage()
    }

    @Test
    fun test02_try_login_with_no_email(){
        clearData()
        clickLoginButton()
        checkIsOnLoginPage()
    }

    @Test
    fun test03_try_login_with_invalid_email(){
        clearData()
        enterEmail(emptyString)
        clickLoginButton()
        checkIsOnLoginPage()
    }

    @Test
    fun test04_try_login_with_valid_email_but_no_password(){
        clearData()
        enterEmail(studentExistsEmail)
        clickLoginButton()
        checkIsOnLoginPage()
    }

    @Test
    fun test05_try_login_with_valid_email_and_password_but_user_does_not_exist(){
        clearData()
        enterEmail(studentDoesNotExistEmail)
        enterPassword(studentDoesNotExistPassword)
        clickLoginButton()
        checkIsOnLoginPage()
    }

    @Test
    fun test06_try_login_with_valid_username_and_password_user_exists(){
        clearData()
        enterEmail(studentExistsEmail)
        enterPassword(studentExistPassword)
        clickLoginButton()
        checkIsOnModulesPage()
    }

    @Test
    fun test07_click_sign_up_button(){
        clickSignUpButton()
        checkIsOnSignUpPage()
    }

    @Test
    fun test08_click_forgot_password_button(){
        clickForgotPasswordButton()
        checkIsOnForgotPasswordPage()
    }

    @After
    fun tearDown(){
//        Insert code to be run after a test
    }
}