package com.labinot.bajrami.bookreaderapp.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.google.firebase.firestore.Query
import com.labinot.bajrami.bookreaderapp.models.FireBaseModelBook
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
          private val queryBooks:Query
):ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)
    val uiState = _uiState.asStateFlow()

    init {

        getAllBooksFromDataBase()
    }


  private fun getAllBooksFromDataBase() {


      viewModelScope.launch {

          try {
              _uiState.value = HomeScreenUIEvents.Loading

              queryBooks.addSnapshotListener { snapshot, exception ->
                  if (exception != null) {
                      _uiState.value = HomeScreenUIEvents.Error("Error: ${exception.message}")
                      return@addSnapshotListener
                  }


                  if (snapshot != null && !snapshot.isEmpty) {
                      val books = snapshot.documents.map { documentSnapshot ->
                          documentSnapshot.toObject(FireBaseModelBook::class.java)
                      }

                      _uiState.value = HomeScreenUIEvents.Success(databaseBooks = books)
                  } else {
                      _uiState.value = HomeScreenUIEvents.Error("No books found.")
                      _uiState.value = HomeScreenUIEvents.Success(databaseBooks = emptyList())
                  }
              }
          } catch (exception: Exception) {
              _uiState.value = HomeScreenUIEvents.Error("Error: ${exception.message}")
          }
      }


      }

    }



sealed class HomeScreenUIEvents {

    data object Loading : HomeScreenUIEvents()
    data class Success(val databaseBooks:List<FireBaseModelBook?> = emptyList()) : HomeScreenUIEvents()
    data class Error(val message: String) : HomeScreenUIEvents()

}


