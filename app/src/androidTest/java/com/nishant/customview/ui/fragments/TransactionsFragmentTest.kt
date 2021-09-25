package com.nishant.customview.ui.fragments

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nishant.customview.MainActivity
import com.nishant.customview.R
import com.nishant.customview.adapters.TransferAdapter.TransferViewHolder
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionsFragmentTest {

    private lateinit var scenario: FragmentScenario<TransactionsFragment>

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_CustomView)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testParentLayoutInView() {
        onView(withId(R.id.transactionsFragmentParent)).check(matches(isDisplayed()))
    }

    @Test
    fun testTransferRecyclerViewInView() {
        onView(withId(R.id.transferRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun testModalOpensOnToBanksClick() {
        onView(withId(R.id.transferRecyclerView))
            .perform(actionOnItemAtPosition<TransferViewHolder>(2, click()))
        Thread.sleep(1000)

        onView(withText("Savings Accounts")).check(matches(isDisplayed()))
        onView(withId(R.id.banksRecyclerView)).check(matches(isDisplayed()))
        onView(withId(R.id.accountsRecyclerView)).check(matches(isDisplayed()))
    }

    @Test
    fun testModalOpensOnTransactionItemClick() {
        onView(withId(R.id.transactionsRecyclerView))
            .perform(actionOnItemAtPosition<TransferViewHolder>(1, click()))
        Thread.sleep(1000)

        onView(withText("Pay")).check(matches(isDisplayed()))
        onView(withText("View Receipt")).check(matches(isDisplayed()))
    }

    @Test
    fun testPaymentMethodModalOpensOnClick() {
        onView(withId(R.id.transactionsRecyclerView))
            .perform(actionOnItemAtPosition<TransferViewHolder>(1, click()))
        Thread.sleep(1000)

        onView(withText("Pay")).check(matches(isDisplayed()))
        onView(withText("Pay")).perform(click())
        Thread.sleep(1000)

        onView(withText("Choose Payment Method")).check(matches(isDisplayed()))
        onView(withId(R.id.paymentMethodRecyclerView)).check(matches(isDisplayed()))
    }
}