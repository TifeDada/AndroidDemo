package com.mytaxi.android_demo.activities;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.mytaxi.android_demo.R;
import com.mytaxi.android_demo.TestApplication;
import com.mytaxi.android_demo.dependencies.TestUtils;
import com.mytaxi.android_demo.dependencies.component.TestAppComponent;
import com.mytaxi.android_demo.models.User;
import com.mytaxi.android_demo.utils.PermissionHelper;
import com.mytaxi.android_demo.utils.network.HttpClient;
import com.mytaxi.android_demo.utils.storage.SharedPrefStorage;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import javax.inject.Inject;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(AndroidJUnit4.class)
public class AuthenticationActivityTest {

    @Inject
    HttpClient httpClient;
    @Inject
    SharedPrefStorage sharedPrefStorage;
    @Inject
    PermissionHelper permissionHelper;


    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class, true, false);
// third parameter is set to false which means the Main activity is not started automatically

    @Before
    public void setup() {
        final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        final TestApplication app =
                (TestApplication) instrumentation.getTargetContext().getApplicationContext();
        final TestAppComponent appComponent = (TestAppComponent)app.getAppComponent();
        appComponent.inject(this);

        when(sharedPrefStorage.loadUser()).thenReturn(null);


        }

    @Test
    public void shouldSeeAuthenticationActivity_whenUserIsNotAuthenticated() {

        activityTestRule.launchActivity(null);

        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_password)).check(matches(isDisplayed()));
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()));
    }

    @Test
    public void shouldRemainOnLoginPage_whenLoginCredentialsAreNotValid() {
        activityTestRule.launchActivity(null);

        onView(withId(R.id.edt_username)).perform(typeText("random_user"));
        onView(withId(R.id.edt_password)).perform(typeText("random_password"));
        onView(withId(R.id.btn_login)).perform(click());

        final ArgumentCaptor<String> seed = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<HttpClient.UserCallback> callbackCaptor =
                ArgumentCaptor.forClass(HttpClient.UserCallback.class);

        verify(httpClient).fetchUser(seed.capture(), callbackCaptor.capture());

        final HttpClient.UserCallback callback = callbackCaptor.getValue();
        callback.setUser(new User("name", "salt", "pass"));
        callback.run();

        //remain on login screen
        onView(withId(R.id.btn_login)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_username)).check(matches(isDisplayed()));
        onView(withId(R.id.edt_password)).check(matches(isDisplayed()));

        reset(httpClient);
    }

    @Test
    public void shouldOpenMainActivity_whenLoginCredentialsAreValid() {
        activityTestRule.launchActivity(null);

        onView(withId(R.id.edt_username)).perform(typeText("whiteelephant261"));
        onView(withId(R.id.edt_password)).perform(typeText("video1"));
        onView(withId(R.id.btn_login)).perform(click());

        final ArgumentCaptor<String> seed = ArgumentCaptor.forClass(String.class);
        final ArgumentCaptor<HttpClient.UserCallback> callbackCaptor =
                ArgumentCaptor.forClass(HttpClient.UserCallback.class);

        verify(httpClient).fetchUser(seed.capture(), callbackCaptor.capture());

        final User user = new User("whiteelephant261", "salt",
                TestUtils.calculateSHA256("video1", "salt"));
        when(sharedPrefStorage.loadUser()).thenReturn(user);

        final HttpClient.UserCallback callback = callbackCaptor.getValue();
        callback.setUser(user);
        callback.run();

        //we should still be directed to the main screen
        onView(withId(R.id.map)).check(matches(isDisplayed()));

        verify(sharedPrefStorage, times(3)).loadUser();
        verify(sharedPrefStorage).saveUser(user);

        reset(httpClient);
    }




}