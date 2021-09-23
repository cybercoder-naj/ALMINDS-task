package com.nishant.customview.ui.fragments

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nishant.customview.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionsFragmentTest {

    private lateinit var scenario: FragmentScenario<TransactionsFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_CustomView)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testPostTransactionModalOnClick() {
        onView(withText("Nishant")).perform(click())
        Thread.sleep(1000)

        onView(withText("Pay")).check(matches(isDisplayed()))
        onView(withText("View Receipt")).check(matches(isDisplayed()))
    }

    @Test
    fun testPrimaryActionClickOnModal() {
        onView(withText("Nishant")).perform(click())
        Thread.sleep(1000)

        onView(withText("Pay")).check(matches(isDisplayed())).perform(click())
    }

    @Test
    fun testSecondaryActionClickOnModal() {
        onView(withText("Nishant")).perform(click())
        Thread.sleep(1000)

        onView(withText("View Receipt")).check(matches(isDisplayed())).perform(click())
    }

    fun testBankTransferModal() {
        onView(withText("To Banks")).perform(click())
        Thread.sleep(1000)

        onView(withText("ICICI")).check(matches(isDisplayed()))
        onView(withText("SBI")).check(matches(isDisplayed()))
        onView(withText("************0011")).check(matches(isDisplayed()))
    }
}