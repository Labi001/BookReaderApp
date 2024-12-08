package com.labinot.bajrami.bookreaderapp.data


import com.labinot.bajrami.bookreaderapp.models.updateResponse.BookSearchResult
import retrofit2.http.GET
import retrofit2.http.Query


interface BookApi {

    @GET("volumes")
    suspend fun getSearchedBooks(@Query("q") query: String):BookSearchResult

}