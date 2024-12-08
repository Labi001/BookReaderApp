package com.labinot.bajrami.bookreaderapp.models.updateResponse

import kotlinx.serialization.Serializable

@Serializable
data class ReadingModes(
    val image: Boolean,
    val text: Boolean
)