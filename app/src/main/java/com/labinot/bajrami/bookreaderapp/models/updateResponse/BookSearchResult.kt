package com.labinot.bajrami.bookreaderapp.models.updateResponse

import kotlinx.serialization.Serializable


@Serializable
data class BookSearchResult(
    val items: List<BookDto>,
    val kind: String,
    val totalItems: Int
)