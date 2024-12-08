package com.labinot.bajrami.bookreaderapp.mappers

import com.labinot.bajrami.bookreaderapp.models.BookItem
import com.labinot.bajrami.bookreaderapp.models.updateResponse.BookDto


fun BookDto.toMyModel():BookItem {
    return BookItem(
        title = this.volumeInfo.title,
        authors = this.volumeInfo.authors[0],
        description = this.volumeInfo.description,
        categories = this.volumeInfo.categories[0],
        imageUrl = if(this.volumeInfo.imageLinks.smallThumbnail.isNullOrEmpty())
            "http://books.google.com/books/content?id=kEFPDwAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api"
        else
        this.volumeInfo.imageLinks.smallThumbnail,
        publishedDate = this.volumeInfo.publishedDate,
        pageCount = this.volumeInfo.pageCount,
        bookId = this.id,
        rating = this.volumeInfo.averageRating
    )
}

fun List<BookDto>.toMyModelList(): List<BookItem>{

    return this.map {
        it.toMyModel()
    }
}