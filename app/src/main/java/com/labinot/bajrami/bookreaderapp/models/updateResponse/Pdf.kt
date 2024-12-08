package com.labinot.bajrami.bookreaderapp.models.updateResponse

import kotlinx.serialization.Serializable

@Serializable
data class Pdf(
    val acsTokenLink: String,
    val isAvailable: Boolean
)