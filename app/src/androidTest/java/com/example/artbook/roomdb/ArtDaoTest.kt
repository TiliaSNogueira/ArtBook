package com.example.artbook.roomdb

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.example.artbook.dependencyinjection.TestAppModule
import com.example.artbook.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@SmallTest
@ExperimentalCoroutinesApi
@HiltAndroidTest
class ArtDaoTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var dao: ArtDao

    @Inject
    @Named("testDatabase")
    lateinit var database: ArtDatabase

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.artDao()
    }


    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertArtTesting() = runBlocking {

        val exampleArt = Art(1, "Mona Lisa", "Da Vinci", 1503, "imageurltest.com")
        dao.insertArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValueTest()
        assertThat(list).contains(exampleArt)

    }

    @Test
    fun deleteArtTesting() = runBlocking {
        val exampleArt = Art(1, "Mona Lisa", "Da Vinci", 1503, "imageurltest.com")
        dao.insertArt(exampleArt)
        dao.deleteArt(exampleArt)

        val list = dao.observeArts().getOrAwaitValueTest()
        assertThat(list).doesNotContain(exampleArt)
    }


}