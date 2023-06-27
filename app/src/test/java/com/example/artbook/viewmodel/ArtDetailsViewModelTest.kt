package com.example.artbook.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.artbook.MainCoroutineRule
import com.example.artbook.getOrAwaitValue
import com.example.artbook.repo.FakeArtRepository
import com.example.artbook.util.Status
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArtDetailsViewModelTest {

    @get: Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ArtDetailsViewModel

    @Before
    fun setup(){
        //Test Doubles
       viewModel = ArtDetailsViewModel(FakeArtRepository())
    }

    @Test
    fun `insert art without year returns error`(){
        viewModel.makeArt("Mona Lisa", "Da Vinci", "")
        val value = viewModel.insertArtMessage.getOrAwaitValue()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without name returns error`(){
        viewModel.makeArt("", "Da Vinci", "1503")
        val value = viewModel.insertArtMessage.getOrAwaitValue()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without artistName returns error`(){
        viewModel.makeArt("Mona Lisa", "", "1503")
        val value = viewModel.insertArtMessage.getOrAwaitValue()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

}