package com.labinot.bajrami.bookreaderapp.prefStored

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

const val USER_DATASTORE = "user_data"

val Context.preferenceDataStore : DataStore<Preferences> by preferencesDataStore(name = USER_DATASTORE)

class DataStoreManager(val context: Context) {

    companion object{

        val EMAIL = stringPreferencesKey("EMAIL")
        val PASSWORD = stringPreferencesKey("PASSWORD")
        val ISCHECKED = booleanPreferencesKey("ISCHECKED")
        val SEARCHQUERY = stringPreferencesKey("SEARCHQUERY")


    }

    suspend fun saveToDataStore( userDetailStored: UserDetailStored){

        context.preferenceDataStore.edit {

            it[EMAIL] = userDetailStored.storedemail
            it[PASSWORD] = userDetailStored.storedpassword
            it[ISCHECKED] = userDetailStored.storedisChecked

        }

    }

    fun getFromDataStore() = context.preferenceDataStore.data.map {

        UserDetailStored(

            storedemail = it[EMAIL]?:"",
            storedpassword = it[PASSWORD]?:"",
            storedisChecked = it[ISCHECKED]?:false


        )


    }

    suspend fun clearDataStore() = context.preferenceDataStore.edit {

        it.clear()

    }

    suspend fun saveSearchQuery(searchQueryStored: SearchQueryStored){

        context.preferenceDataStore.edit {

            it[SEARCHQUERY] = searchQueryStored.searchQuery


        }

    }

    fun getSearchQuery() = context.preferenceDataStore.data.map {

        SearchQueryStored(
            searchQuery = it[SEARCHQUERY]?:""
        )


    }

    fun clearSearchQuery() = context.preferenceDataStore.data.map {

        SearchQueryStored(
            searchQuery = ""
        )


    }


}