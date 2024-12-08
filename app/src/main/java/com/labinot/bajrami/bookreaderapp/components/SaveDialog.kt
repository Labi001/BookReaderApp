package com.labinot.bajrami.bookreaderapp.components


import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.labinot.bajrami.bookreaderapp.R


@Composable
fun SaveAlertDialog(
    isOpen: Boolean,
    title: String,
    bodyText: String,
    OnDismissRequest: () -> Unit,
    OnConfirmButtonClick: () -> Unit
) {




    if (isOpen) {

        AlertDialog(onDismissRequest = { OnDismissRequest.invoke() },
            title = { Text(text = title,
                fontWeight = FontWeight.Bold)},
            text = { Text(text = bodyText) },
            icon = {

                Icon(
                    painter = painterResource(R.drawable.open_book),
                    modifier = Modifier.size(50.dp),
                    tint = Color.White,
                    contentDescription = ""
                )

            },
            confirmButton = {

                TextButton(
                    onClick = { OnConfirmButtonClick.invoke() },
                ) {

                    Text(text = "Yes",
                        color = Color.White)

                }

            },
            dismissButton = {

                TextButton(onClick = { OnDismissRequest.invoke() }) {

                    Text(text = "Cancel",
                        color = Color.White)

                }

            },
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = Color.White,
            textContentColor = Color.White)

    }


}