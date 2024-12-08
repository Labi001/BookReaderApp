package com.labinot.bajrami.bookreaderapp.pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.labinot.bajrami.bookreaderapp.data.BookApi
import com.labinot.bajrami.bookreaderapp.mappers.toMyModelList
import com.labinot.bajrami.bookreaderapp.models.BookItem


class SearchPagingSource(
    private val query: String,
    private val apiService: BookApi,
    private var itemsPerPage: Int = 10
) : PagingSource<Int, BookItem>() {

    companion object {
        private const val STARTING_PAGE_INDEX = 1

    }

    override fun getRefreshKey(state: PagingState<Int, BookItem>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.let { anchorPage ->
                anchorPage.prevKey?.plus(1) ?: anchorPage.nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, BookItem> {
        val currentPage = params.key ?: STARTING_PAGE_INDEX
        val startIndex = (currentPage - 1) * itemsPerPage


        return try {
            println("Query: $query, Page: $currentPage")

            // Fetch the data
            val response = apiService.getSearchedBooks(query)

            // Ensure `items` is not null
            val items = response.items
            val endIndex = minOf(startIndex + itemsPerPage,items.size)
            if (startIndex >= items.size) {
                println("Start Index ($startIndex) exceeds Items Size (${items.size})")
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1,
                    nextKey = null // End of pagination
                )
            }

            // Apply manual pagination
            val paginatedItems = items.subList(startIndex, minOf(endIndex, items.size))

            println("Start: $startIndex, End: $endIndex, Items fetched: ${paginatedItems.size}, Total items: ${response.totalItems}")

            LoadResult.Page(
                data = paginatedItems.toMyModelList(),
                prevKey = if (currentPage == STARTING_PAGE_INDEX) null else currentPage - 1,
                nextKey = if (endIndex >= items.size) null else currentPage + 1
            )
        } catch (e: Exception) {
            println("Error fetching data: ${e.localizedMessage}")
            LoadResult.Error(e)

        }
    }
}

