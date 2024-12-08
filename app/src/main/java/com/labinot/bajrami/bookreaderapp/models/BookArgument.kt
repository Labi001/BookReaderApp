package com.labinot.bajrami.bookreaderapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class BookArgument(

    val title:String,
    val authors:String,
    val description:String,
    val categories:String,
    val imageUrl:String,
    val publishedDate:String,
    val pageCount:Int,
    val bookId:String,
    val rating:Double

) :Parcelable{

    companion object{

       fun fromBookItem(bookItem: BookItem?) = BookArgument(

           title = bookItem?.title?:"",
           authors = bookItem?.authors?:"",
           description = bookItem?.description?:"",
           categories = bookItem?.categories?:"",
           imageUrl = bookItem?.imageUrl?:"",
           publishedDate = bookItem?.publishedDate?:"",
           pageCount = bookItem?.pageCount?:0,
           bookId = bookItem?.bookId?:"",
           rating = bookItem?.rating?:0.0
       )

    }

}
