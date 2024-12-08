package com.labinot.bajrami.bookreaderapp.components



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SearchField(
    modifier: Modifier = Modifier,
    hint: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction,
    onAction: KeyboardActions = KeyboardActions.Default,
    leadingIcon:Int,
    values: String,
    onValueChange: (String) -> Unit,
)
{

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
        backgroundColor = Transparent,
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
    ){

            TextField(
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.secondary),
                value = values,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
                keyboardActions = onAction,
                singleLine = true,

                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
                    cursorColor = MaterialTheme.colorScheme.tertiary,
                    focusedIndicatorColor = Transparent,
                    unfocusedIndicatorColor = Transparent,
                    errorIndicatorColor = Transparent,
                    errorCursorColor = Transparent,
                    errorTextColor = Transparent,
                    errorContainerColor = Transparent

                ),
                placeholder = {

                    Text(text = hint,
                        color = White.copy(alpha = 0.7f))
                },

                leadingIcon = {

                    Icon(
                        modifier = Modifier.size(18.dp),
                        painter = painterResource(id = leadingIcon),
                        contentDescription = "Leading Icon",
                        tint = White.copy(alpha = 0.7f))

                },

                shape = RoundedCornerShape(25.dp)


            )






    }



}

