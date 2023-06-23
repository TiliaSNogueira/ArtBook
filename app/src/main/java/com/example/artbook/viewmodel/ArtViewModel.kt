package com.example.artbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artbook.repo.ArtRepositoryInterface
import com.example.artbook.roomdb.Art
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtViewModel @Inject constructor(
    private val repository: ArtRepositoryInterface
) : ViewModel() {

    val artList = repository.getArt()

    fun deleteArt(art: Art) = viewModelScope.launch {
        repository.deleteArt(art)
    }

}