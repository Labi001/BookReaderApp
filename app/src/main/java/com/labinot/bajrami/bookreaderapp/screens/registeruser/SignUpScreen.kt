package com.labinot.bajrami.bookreaderapp.screens.registeruser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.labinot.bajrami.bookreaderapp.R
import com.labinot.bajrami.bookreaderapp.components.LogInBox
import com.labinot.bajrami.bookreaderapp.components.MyTextField
import com.labinot.bajrami.bookreaderapp.helper.IsSmallScreenHeight
import com.labinot.bajrami.bookreaderapp.helper.isEmailValid
import com.labinot.bajrami.bookreaderapp.helper.rememberImeState
import com.labinot.bajrami.bookreaderapp.navigation.Routs
import com.labinot.bajrami.booksapp.screens.logIn.LogInEvents
import com.labinot.bajrami.booksapp.screens.logIn.LogInState

@Composable
fun SignUpScreen(
    navController: NavController,
    onEvent: (LogInEvents) -> Unit,
    state: LogInState,
    registerViewModel: RegisterViewModel
) {

    val isImeVisible by rememberImeState()
    val showPassword = remember {
        mutableStateOf(false)
    }

    val isLoading by registerViewModel.loading.observeAsState(false)

    val showRepeatPassword = remember {
        mutableStateOf(false)
    }

    var errorPassword by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var errorEmail by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    var errorRepeatPassword by rememberSaveable {
        mutableStateOf<String?>(null)
    }

    errorPassword = when {

        state.signUpPassword.length in 1..3 -> "Password is too short!"

        else -> null
    }

    if (state.signUpRepeatPassword.isNotEmpty()) {

        errorRepeatPassword = when {

            (state.signUpRepeatPassword != state.signUpPassword && (state.signUpRepeatPassword.trim()
                .isNotEmpty()
                    && state.signUpPassword.trim().isNotEmpty())) -> "The password doesn't match !"

            (state.signUpRepeatPassword.trim().isNotEmpty() && state.signUpPassword.trim()
                .isEmpty()) -> "Fill password field first !"

            else -> null
        }
    }


    val isValidEmail = isEmailValid(state.signUpEmail)

    errorEmail = if (!isValidEmail && state.signUpEmail.trim().isNotEmpty()){

        "Your Email is Invalid !"

    }else{

        null
    }

    val valid = remember(state.signUpEmail, state.signUpPassword, state.signUpRepeatPassword) {

        state.signUpEmail.trim().isNotEmpty()
                && state.signUpPassword.trim().isNotEmpty()
                && state.signUpRepeatPassword.trim().isNotEmpty()
                && errorPassword == null
                && errorRepeatPassword == null
                && errorEmail == null


    }

    LogInBox(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            val animatedUpperSectionRatio by animateFloatAsState(
                targetValue = if (isImeVisible) 0f else 0.35f,
                label = ""
            )

            AnimatedVisibility(
                visible = !isImeVisible,
                enter = fadeIn(), exit = fadeOut()
            ) {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(animatedUpperSectionRatio),
                    contentAlignment = Alignment.Center
                ) {


                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        )
                        {

                            FilledIconButton(
                                onClick = { navController.navigateUp() },
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            )
                            {

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Back Narrow",
                                    tint = Color.White
                                )

                            }

                            Spacer(modifier = Modifier.width(9.dp))

                            Text(
                                text = "I have already an account!",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )

                        }

                        Icon(
                            modifier = Modifier
                                .height(120.dp)
                                .width(70.dp),
                            painter = painterResource(id = R.drawable.open_book),
                            tint = MaterialTheme.colorScheme.tertiary,
                            contentDescription = "Main Logo Img"
                        )

                    }


                }

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.secondary),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                if (IsSmallScreenHeight()) {

                    Spacer(modifier = Modifier.fillMaxSize(0.05f))


                } else {

                    Spacer(modifier = Modifier.fillMaxSize(0.0f))
                }

                Spacer(modifier = Modifier.height(26.dp))
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )



                if (IsSmallScreenHeight()) {

                    Spacer(modifier = Modifier.fillMaxSize(0.05f))


                } else {

                    Spacer(modifier = Modifier.fillMaxSize(0.1f))
                }

                MyTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    label = "Email",
                    hint = "Add an Email",
                    trailing = {
                    },
                    imeAction = ImeAction.Next,
                    onAction = KeyboardActions {
                        if (!valid) return@KeyboardActions
                    },
                    keyboardType = KeyboardType.Email,
                    leadingIcon = R.drawable.mail,
                    values = state.signUpEmail,
                    onValueChange = {
                        onEvent(LogInEvents.OnEmailSignUpChange(it))
                    },
                    errorMessage = errorEmail
                )

                Spacer(modifier = Modifier.height(20.dp))

                MyTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    label = "Password",
                    hint = "Add Password",
                    visualTransformation = if (showPassword.value)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailing = {
                        Icon(
                            modifier = Modifier.clickable {
                                showPassword.value = !showPassword.value
                            },
                            painter = painterResource(
                                id = if (showPassword.value)
                                    R.drawable.ic_eye_off else R.drawable.ic_eye_open
                            ),
                            tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                            contentDescription = null
                        )
                    },
                    imeAction = ImeAction.Next,
                    onAction = KeyboardActions {
                        if (!valid) return@KeyboardActions
                    },
                    keyboardType = KeyboardType.Password,
                    leadingIcon = R.drawable.padlock,
                    values = state.signUpPassword,
                    onValueChange = {
                        onEvent(LogInEvents.OnPasswordSigUpChange(it))
                    },
                    errorMessage = errorPassword
                )

                Spacer(modifier = Modifier.height(20.dp))

                MyTextField(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    label = "Re-Enter Password",
                    hint = "repeat Password",
                    visualTransformation = if (showRepeatPassword.value)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    trailing = {
                        Icon(
                            modifier = Modifier.clickable {
                                showRepeatPassword.value = !showRepeatPassword.value
                            },
                            painter = painterResource(
                                id = if (showRepeatPassword.value)
                                    R.drawable.ic_eye_off else R.drawable.ic_eye_open
                            ),
                            tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                            contentDescription = null
                        )
                    },
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password,
                    onAction = KeyboardActions {
                        if (!valid) return@KeyboardActions else{

                            registerViewModel.CreateUserWithEmailAndPassword(state.signUpEmail,state.signUpPassword){

                                navController.navigate(Routs.HomeScreen)


                            }

                            state.signUpEmail = ""
                            state.signUpPassword = ""
                            state.signUpRepeatPassword = ""


                        }
                    },
                    leadingIcon = R.drawable.padlock,
                    values = state.signUpRepeatPassword,
                    onValueChange = {
                        onEvent(LogInEvents.OnPasswordRepeatSigUpChange(it))
                    },
                    errorMessage = if (state.signUpRepeatPassword.isNotEmpty()) errorRepeatPassword
                    else null
                )



                if (isImeVisible) {

                    Button(
                        onClick = {

                            registerViewModel.CreateUserWithEmailAndPassword(state.signUpEmail,state.signUpPassword){

                                navController.navigate(Routs.HomeScreen)


                            }

                            state.signUpEmail = ""
                            state.signUpPassword = ""
                            state.signUpRepeatPassword = ""


                        },
                        enabled = valid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp)
                            .padding(top = 20.dp)
                            .clip(RoundedCornerShape(30.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(30.dp)
                    )
                    {

                        if(isLoading){

                            AnimatedVisibility(
                                visible = isLoading,
                                enter = scaleIn(tween(500)),
                                exit = scaleOut(tween(500))
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(30.dp),
                                    color = Color.White)

                            }



                        }else {

                            AnimatedVisibility(
                                visible = !isLoading,
                                enter = scaleIn(tween(500)),
                                exit = scaleOut(tween(500))
                            ) {

                                Text(
                                    text = "Register",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(500),
                                        color = Color.White
                                    ),
                                    color = Color.White
                                )
                            }



                        }



                    }


                } else {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    )
                    {

                        Button(
                            onClick = {

                                registerViewModel.CreateUserWithEmailAndPassword(state.signUpEmail,state.signUpPassword){

                                    navController.navigate(Routs.HomeScreen)


                                }

                                state.signUpEmail = ""
                                state.signUpPassword = ""
                                state.signUpRepeatPassword = ""
                            },
                            enabled = valid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp)
                                .padding(bottom = 15.dp)
                                .clip(RoundedCornerShape(30.dp)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiary,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        {

                            if(isLoading){

                                CircularProgressIndicator(modifier = Modifier.size(30.dp),
                                    color = Color.White)

                            }else {

                                Text(
                                    text = "Register",
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight(500),
                                        color = Color.White
                                    ),
                                    color = Color.White
                                )

                            }

                        }


                    }

                }


            }


        }

    }

}