package com.labinot.bajrami.bookreaderapp.models.updateResponse

import kotlinx.serialization.Serializable

@Serializable
data class ImageLinks(
    val smallThumbnail: String,
    val thumbnail: String
)