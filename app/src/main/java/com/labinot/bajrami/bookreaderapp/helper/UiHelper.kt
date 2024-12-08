package com.labinot.bajrami.bookreaderapp.helper

import android.icu.text.DateFormat
import android.util.Patterns
import android.view.ViewTreeObserver
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun IsSmallScreenHeight(): Boolean {

    val conf = LocalConfiguration.current
    return conf.screenHeightDp <= 786

}

@Composable
fun rememberImeState(): State<Boolean> {
    val imeState = remember {
        mutableStateOf(false)
    }
    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen = ViewCompat.getRootWindowInsets(view)
                ?.isVisible(WindowInsetsCompat.Type.ime()) ?: true
            imeState.value = isKeyboardOpen
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose {
            view.viewTreeObserver.removeOnGlobalLayoutListener(listener)
        }
    }
    return imeState
}


fun isEmailValid(email: CharSequence): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun formatDate(timestamp: Timestamp): String {

    val date = DateFormat.getDateInstance()
        .format(timestamp.toDate())
        .toString().split(",")[0]

    return date

}

fun formatTimestampToDate(timestamp: Timestamp?): String {
    return if (timestamp != null) {
        val date = timestamp.toDate() // Convert to Date
        val format = SimpleDateFormat("dd MMM", Locale.getDefault()) // e.g., 04 Dec
        format.format(date)
    } else {
        "Unknown"
    }
}