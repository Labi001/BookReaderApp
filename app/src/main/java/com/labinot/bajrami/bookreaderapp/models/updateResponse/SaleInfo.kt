package com.labinot.bajrami.bookreaderapp.models.updateResponse

import kotlinx.serialization.Serializable

@Serializable
data class SaleInfo(
    val country: String,
    val isEbook: Boolean,
    val saleability: String
)