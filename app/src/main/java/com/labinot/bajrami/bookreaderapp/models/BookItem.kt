package com.labinot.bajrami.bookreaderapp.models

data class BookItem(

    val title:String,
    val authors:String,
    val description:String,
    val categories:String,
    val imageUrl:String,
    val publishedDate:String,
    val pageCount:Int,
    val bookId:String,
    val rating:Double

)
