package com.labinot.bajrami.bookreaderapp.navigation

import com.labinot.bajrami.bookreaderapp.models.BookArgument
import kotlinx.serialization.Serializable

@Serializable
sealed class Routs {

    @Serializable
    data object HomeScreen: Routs()

    @Serializable
    data object LogInScreen: Routs()

    @Serializable
    data object SignUpScreen: Routs()

    @Serializable
    data object SearchScreen: Routs()

    @Serializable
    data class DetailScreen(val bookItem: BookArgument) : Routs()

    @Serializable
    data class UpdateScreen(val bookItemId: String) : Routs()

    @Serializable
    data object StatsScreen: Routs()


}
