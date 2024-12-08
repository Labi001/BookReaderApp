package com.labinot.bajrami.bookreaderapp.models.updateResponse

import kotlinx.serialization.Serializable

@Serializable
data class IndustryIdentifier(
    val identifier: String,
    val type: String
)