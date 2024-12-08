package com.labinot.bajrami.bookreaderapp.screens.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.labinot.bajrami.bookreaderapp.models.BookItem
import com.labinot.bajrami.bookreaderapp.repositories.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: BookRepository
):ViewModel() {

    private val _searchBooks = MutableStateFlow<PagingData<BookItem>>(PagingData.empty())
    val searchBooks = _searchBooks

    private val _uiState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _toastMessage = MutableLiveData("")
    val toastMessage: LiveData<String> = _toastMessage

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading


    fun searchImages(query:String){

        viewModelScope.launch {

            _uiState.value = LoadingState.Loading
            try {

                    repository.searchBooks(query)
                        .cachedIn(viewModelScope)
                        .collect{ data->
                            _searchBooks.value = data
                            _uiState.value = LoadingState.Success(searchBooks.value)



                        }


                _loading.value = true

            }catch (e:Exception){
                _toastMessage.value = "${e.message}"
                _loading.value = false
                _uiState.value = LoadingState.Error(e.message?:"")

            }finally {
                _loading.value = false
            }

        }

    }

}

sealed class LoadingState {
    data object Idle : LoadingState()
    data object Loading : LoadingState()
    data class Success(val items: PagingData<BookItem>) : LoadingState()
    data class Error(val message: String) : LoadingState()
}