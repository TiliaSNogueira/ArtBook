package com.example.artbook.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.artbook.R
import com.example.artbook.getOrAwaitValueTest
import com.example.artbook.launchFragmentInHiltContainer
import com.example.artbook.repo.FakeArtRepositoryTest
import com.example.artbook.roomdb.Art
import com.example.artbook.viewmodel.ArtDetailsViewModel
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject


@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class ArtDetailsFragmentTest {

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    @get: Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ArtFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }


    val args = ArtDetailsFragmentArgs("art")
    val bundle = args.toBundle()

    @Test
    fun testNavigationFromArtDetailsToImageAPI() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.artImageView)).perform(ViewActions.click())

        Mockito.verify(navController).navigate(
            ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageApiFragment()
        )

    }

    @Test
    fun testOnBackPressed() {
        val navController = Mockito.mock(NavController::class.java)

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        Espresso.pressBack()
        Mockito.verify(navController).popBackStack()

    }


    @Test
    fun testSave() {
        val navController = Mockito.mock(NavController::class.java)
        val testViewModel = ArtDetailsViewModel(FakeArtRepositoryTest())

        launchFragmentInHiltContainer<ArtDetailsFragment>(
            factory = fragmentFactory,
            fragmentArgs = bundle
        ) {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = testViewModel
        }

        Espresso.onView(withId(R.id.nameText)).perform(ViewActions.replaceText("Chicken"))
        Espresso.onView(withId(R.id.artistText)).perform(ViewActions.replaceText("Chicken"))
        Espresso.onView(withId(R.id.yearText)).perform(ViewActions.replaceText("2023"))
        Espresso.onView(withId(R.id.saveButton)).perform(ViewActions.click())


        assertThat(testViewModel.artList.getOrAwaitValueTest()).contains(
            Art(name ="Chicken", artistName = "Chicken", year = 2023, imageUrl =  "")
        )

    }

}