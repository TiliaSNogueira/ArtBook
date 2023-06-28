package com.example.artbook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.artbook.repo.ArtRepositoryInterface
import com.example.artbook.roomdb.Art
import com.example.artbook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtDetailsViewModel @Inject constructor(
    private val repository: ArtRepositoryInterface
) : ViewModel() {

    private var insertArtMsg = MutableLiveData<Resource<Art>>()

    val artList = repository.getArt()
    val insertArtMessage: LiveData<Resource<Art>>
        get() = insertArtMsg


    private val selectedImage = MutableLiveData<String>()

    fun resetInsertArtMsg() {
        insertArtMsg = MutableLiveData<Resource<Art>>()
    }

    fun selectedImage(url: String) {
        selectedImage.postValue(url)
    }

    private fun insertArt(art: Art) = viewModelScope.launch {
        repository.insertArt(art)
    }

    fun makeArt(name: String, artistName: String, year: String) {
        if (name.isEmpty() || artistName.isEmpty() || year.isEmpty()) {
            insertArtMsg.postValue(Resource.error("Enter name, artist and year", null))
            return
        }

        val yearInt = try {
            year.toInt()
        } catch (e: Exception) {
            insertArtMsg.postValue(Resource.error("Year should be number", null))
            return
        }

        val art = Art(
            name = name,
            artistName = artistName,
            year = yearInt,
            imageUrl = selectedImage.value ?: ""
        )

        insertArt(art)
        setSelectedImage("")
        insertArtMsg.postValue(Resource.success(art))
    }

    private fun setSelectedImage(url: String) {
        selectedImage.postValue(url)
    }

}