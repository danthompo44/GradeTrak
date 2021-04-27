package com.university.gradetrak

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.clearText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.university.gradetrak.ui.forgotPassword.ForgotPasswordActivity
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@RunWith(AndroidJUnit4::class)
@LargeTest

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class ForgotPasswordInstrumentalTests {
    private val invalidEmail = ""
    private val validEmailExists = "dan@dan.com"
    private val validEmailDoesNotExist = "doesnotexist@test.com"

    @get:Rule
    var activityRule: ActivityScenarioRule<ForgotPasswordActivity> =
        ActivityScenarioRule(ForgotPasswordActivity::class.java)

    @Before
    fun setup(){
        clearData()
    }

    private fun clearData(){
        onView(withId(R.id.et_forgot_password_page_email_address)).perform(clearText())
    }

    private fun clickSubmitButton(){
        onView(withId(R.id.btn_forgot_password_submit)).perform(ViewActions.click())
    }

    private fun enterEmail(email: String){
        onView(withId(R.id.et_forgot_password_page_email_address)).perform(typeText(email))
    }

    private fun confirmViewIsForgotPasswordPage(){
        onView(withId(R.id.et_forgot_password_page_email_address))
    }

    private fun confirmViewIsLoginPage(){
        onView(withId(R.id.fl_login_header_image))
    }

    @Test
    fun test01_try_submit_with_invalid_email(){
        enterEmail(invalidEmail)
        clickSubmitButton()
        confirmViewIsForgotPasswordPage()
    }

    @Test
    fun test02_try_submit_with_email_that_does_not_exist(){
        enterEmail(validEmailDoesNotExist)
        clickSubmitButton()
        confirmViewIsForgotPasswordPage()
    }

    @Test
    fun test03_try_submit_with_email_that_does_exist(){
        enterEmail(validEmailExists)
        clickSubmitButton()
        confirmViewIsLoginPage()
    }
}