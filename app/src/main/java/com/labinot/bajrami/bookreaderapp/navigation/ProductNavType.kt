package com.labinot.bajrami.bookreaderapp.navigation

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.navigation.NavType
import com.labinot.bajrami.bookreaderapp.models.BookArgument

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder


val productNavType = object : NavType<BookArgument>(isNullableAllowed = false) {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun get(bundle: Bundle, key: String): BookArgument? {

        return bundle.getParcelable(key,BookArgument::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun parseValue(value: String): BookArgument {

        val item = Json.decodeFromString<BookArgument>(value)
        return item.copy(
            title = String(java.util.Base64.getDecoder().decode(item.title.toByteArray())).replace("_","/"),
            authors = String(java.util.Base64.getDecoder().decode(item.authors.toByteArray())).replace("_","/"),
            description = String(java.util.Base64.getDecoder().decode(item.description.toByteArray())).replace("_","/"),
            categories = String(java.util.Base64.getDecoder().decode(item.categories.toByteArray())).replace("_","/"),
            imageUrl = URLDecoder.decode(item.imageUrl,"UTF-8"),
            publishedDate = String(java.util.Base64.getDecoder().decode(item.publishedDate.toByteArray())).replace("_","/"),
            bookId = String(java.util.Base64.getDecoder().decode(item.bookId.toByteArray())).replace("_","/")



        )

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun serializeAsValue(value: BookArgument): String {

        return Json.encodeToString(
            value.copy(
                title = String(java.util.Base64.getEncoder().encode(value.title.toByteArray())).replace("/","_"),
                authors = String(java.util.Base64.getEncoder().encode(value.authors.toByteArray())).replace("/","_"),
                description = String(java.util.Base64.getEncoder().encode(value.description.toByteArray())).replace("/","_"),
                categories = String(java.util.Base64.getEncoder().encode(value.categories.toByteArray())).replace("/","_"),
                imageUrl = URLEncoder.encode(value.imageUrl,"UTF-8"),
                publishedDate = String(java.util.Base64.getEncoder().encode(value.publishedDate.toByteArray())).replace("/","_"),
                bookId = String(java.util.Base64.getEncoder().encode(value.bookId.toByteArray())).replace("/","_"),



            )

        )


    }

    override fun put(bundle: Bundle, key: String, value: BookArgument) {

        bundle.putParcelable(key, value)
    }


}