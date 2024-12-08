package com.labinot.bajrami.bookreaderapp.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.labinot.bajrami.bookreaderapp.data.BookApi
import com.labinot.bajrami.bookreaderapp.helper.Constants.ITEMS_PER_PAGE
import com.labinot.bajrami.bookreaderapp.models.BookItem
import com.labinot.bajrami.bookreaderapp.pagging.SearchPagingSource
import kotlinx.coroutines.flow.Flow

class BookRepositoryImpl(
    private val bookApi: BookApi
):BookRepository {
    override suspend fun searchBooks(query: String): Flow<PagingData<BookItem>> {

        return Pager(
            config = PagingConfig(pageSize = ITEMS_PER_PAGE),
            pagingSourceFactory = {

                SearchPagingSource(query,
                    bookApi,
                    itemsPerPage = 5
                    )

            }
        ).flow

    }


}