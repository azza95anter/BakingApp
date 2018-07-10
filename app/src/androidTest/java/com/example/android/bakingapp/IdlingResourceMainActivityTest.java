package com.example.android.bakingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.deps.guava.base.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by azza anter on 4/13/2018.
 */
@RunWith(AndroidJUnit4.class)
public class IdlingResourceMainActivityTest {
    // these to test the Nutella Pie and Brownies items in the recyclerView in MainActivity
    public static final String Recipe_Name = "Nutella Pie";
    public static final String Brownies = "Brownies";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private IdlingResource mIdlingResource;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void RecyclerViewIsBeingDisplayed() {
        onView(withId(R.id.rv_Movies)).check(matches(isDisplayed()));

    }

    @Test
    public void RecyclerViewTextViewsHaveCorrectText() {
        onView(withId(R.id.rv_Movies))
                .check(matches(atPosition(0, hasDescendant(withText(Recipe_Name)))));

        onView(withId(R.id.rv_Movies))
                .check(matches(atPosition(1, hasDescendant(withText(Brownies)))));
    }

    @Test
    public void TestClickOnRecyclerView() throws InterruptedException {
        onView(withId(R.id.rv_Movies)).perform(RecyclerViewActions.actionOnItemAtPosition(2, click()));
    }

    @Test
    public void testIntent() {
        Intent intent = new Intent(mActivityTestRule.getActivity(), MainActivity.class);
        mActivityTestRule.launchActivity(intent);

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                // has no item on such position
                return viewHolder != null && itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
    //  here i will  test the IngredientsAndSteps Activity

}
