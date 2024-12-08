package com.labinot.bajrami.bookreaderapp.components



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NoteField(

    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction,
    defaultValues: String,
    onDone: (String) ->Unit

)
{

    val txtFieldValue = rememberSaveable { mutableStateOf(defaultValues) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(txtFieldValue.value) { txtFieldValue.value.trim().isNotEmpty() }

    val focusManager = LocalFocusManager.current


    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
        backgroundColor = Transparent,
    )

    CompositionLocalProvider(
        LocalTextSelectionColors provides customTextSelectionColors,
    ){

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(130.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .background(MaterialTheme.colorScheme.secondary),
                value = txtFieldValue.value,
                onValueChange = {
                    txtFieldValue.value = it
                },
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
                keyboardActions = KeyboardActions{

                    if(!valid)return@KeyboardActions
                    onDone(txtFieldValue.value.trim())
                    focusManager.clearFocus()
                    keyboardController?.hide()



                },

                colors = TextFieldDefaults.colors(

                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    cursorColor = MaterialTheme.colorScheme.tertiary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.secondary,
                    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    errorIndicatorColor = Transparent,
                    errorCursorColor = Transparent,
                    errorTextColor = Transparent,
                    errorContainerColor = Transparent

                ),
                label = {

                    Text(text = "Enter your Thought about this Book!",
                        color = MaterialTheme.colorScheme.tertiary)

                },
                shape = RoundedCornerShape(25.dp)


            )






    }



}

