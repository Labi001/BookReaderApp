package com.labinot.bajrami.bookreaderapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.labinot.bajrami.bookreaderapp.helper.Constants.BASE_URL
import com.labinot.bajrami.bookreaderapp.repositories.BookRepository
import com.labinot.bajrami.bookreaderapp.repositories.BookRepositoryImpl
import com.labinot.bajrami.bookreaderapp.screens.home.HomeScreenViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideFireBookRepository()
            = HomeScreenViewModel(queryBooks = FirebaseFirestore.getInstance()
        .collection("books"))


    @Singleton
    @Provides
    fun provideBookApi(): BookApi {

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()

        return retrofit.create(BookApi::class.java)

    }

    @Provides
    @Singleton
    fun provideBookRepository(
        apiService: BookApi,

    ): BookRepository{

        return BookRepositoryImpl(apiService)
    }

}