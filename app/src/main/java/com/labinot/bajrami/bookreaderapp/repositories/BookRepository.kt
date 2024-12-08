package com.labinot.bajrami.bookreaderapp.repositories

import androidx.paging.PagingData
import com.labinot.bajrami.bookreaderapp.models.BookItem
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    suspend fun searchBooks (query:String):Flow<PagingData<BookItem>>
}