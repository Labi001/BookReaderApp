package com.labinot.bajrami.bookreaderapp.screens.registeruser

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.labinot.bajrami.bookreaderapp.R
import com.labinot.bajrami.bookreaderapp.components.LogInBox
import com.labinot.bajrami.bookreaderapp.components.MyTextField
import com.labinot.bajrami.bookreaderapp.helper.IsSmallScreenHeight
import com.labinot.bajrami.bookreaderapp.helper.rememberImeState
import com.labinot.bajrami.bookreaderapp.navigation.Routs
import com.labinot.bajrami.bookreaderapp.prefStored.DataStoreManager
import com.labinot.bajrami.bookreaderapp.prefStored.UserDetailStored
import com.labinot.bajrami.booksapp.screens.logIn.LogInEvents
import com.labinot.bajrami.booksapp.screens.logIn.LogInState
import kotlinx.coroutines.launch


@SuppressLint("RememberReturnType")
@Composable
fun LogInScreen(
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

    val toastMessage by registerViewModel.toastMessage.observeAsState()


    val dataStoreContext = LocalContext.current
    val dataStoreManager = DataStoreManager(dataStoreContext)
    val scope = rememberCoroutineScope()

    var rememberMeCheck by remember {

        mutableStateOf(false)
    }

    val getUserStoredDetails by dataStoreManager.getFromDataStore().collectAsState(initial = null)

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    LaunchedEffect(getUserStoredDetails) {
        email = getUserStoredDetails?.storedemail ?: ""
        password = getUserStoredDetails?.storedpassword ?: ""
        rememberMeCheck = getUserStoredDetails?.storedisChecked ?: false
    }

    if (!toastMessage.isNullOrEmpty()) {
        Toast.makeText(LocalContext.current, toastMessage, Toast.LENGTH_SHORT).show()
        registerViewModel.clearToastMessage() // Reset after showing Toast
    }

    val valids = remember(email, password) {

        email.trim().isNotEmpty()
                && password.trim().isNotEmpty()

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

                        Text(
                            text = "Welcome to BookReader",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(5.dp))
                        Image(
                            modifier = Modifier
                                .size(80.dp),
                            painter = painterResource(id = R.drawable.stack_books),
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

                    Spacer(modifier = Modifier.fillMaxSize(0.1f))
                }

                Text(
                    text = "Log In",
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
                    hint = "Add Email",
                    trailing = {
                    },
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Email,
                    leadingIcon = R.drawable.mail,
                    values = email,
                    onValueChange = {

                         email = it
                       // onEvent(LogInEvents.OnEmailLogInChange(it))
                    },
                    errorMessage = null
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

                    imeAction = ImeAction.Done,
                    onAction = KeyboardActions {

                        if (!valids) return@KeyboardActions else{

                            scope.launch {

                                if (rememberMeCheck) {
                                    dataStoreManager.saveToDataStore(

                                        UserDetailStored(
                                            storedemail = email,
                                            storedpassword = password,
                                            storedisChecked = rememberMeCheck
                                        )

                                    )
                                    registerViewModel.SignInUserWithEmailAndPassword(email,password){

                                        navController.navigate(Routs.HomeScreen) {

                                            popUpTo(0)
                                        }

                                    }
                                } else {
                                    registerViewModel.SignInUserWithEmailAndPassword(email,password){

                                        navController.navigate(Routs.HomeScreen) {
                                            popUpTo(0)

                                        }

                                    }
                                    dataStoreManager.clearDataStore()


                                }


                            }


                        }
                    },
                    keyboardType = KeyboardType.Password,
                    leadingIcon = R.drawable.padlock,
                    values = password,
                    onValueChange = {
                        password = it
                       // onEvent(LogInEvents.OnPasswordLogInChange(it))
                    },
                    errorMessage = null)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier.wrapContentSize(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {

                        CompositionLocalProvider {

                            LocalMinimumInteractiveComponentSize provides Dp.Unspecified

                            Checkbox(
                                checked = rememberMeCheck,
                                onCheckedChange = {

                                    rememberMeCheck = it
                                },
                                colors = CheckboxDefaults.colors(

                                    checkedColor = MaterialTheme.colorScheme.tertiary,
                                    uncheckedColor = MaterialTheme.colorScheme.tertiary

                                )
                            )

                        }

                        Text(
                            text = "Remember Me",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 1.dp),
                            color = MaterialTheme.colorScheme.tertiary
                        )


                    }

                    TextButton(
                        onClick = {
                            navController.navigate(Routs.SignUpScreen)
                            onEvent(LogInEvents.OnEmptyText)
                        },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(
                            text = "I don't have an Account >>",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                            textAlign = TextAlign.End
                        )
                    }

                }




                if (isImeVisible) {

                    Button(
                        onClick = {

                            scope.launch {

                                if (rememberMeCheck) {
                                    dataStoreManager.saveToDataStore(

                                        UserDetailStored(
                                            storedemail = email,
                                            storedpassword = password,
                                            storedisChecked = rememberMeCheck
                                        )

                                    )
                                    registerViewModel.SignInUserWithEmailAndPassword(email,password){

                                        navController.navigate(Routs.HomeScreen)

                                    }
                                } else {
                                    registerViewModel.SignInUserWithEmailAndPassword(email,password){

                                        navController.navigate(Routs.HomeScreen)

                                    }
                                    dataStoreManager.clearDataStore()


                                }


                            }
                        },
                        enabled = valids,
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
                                    text = "Log In",
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

                                scope.launch {

                                    if (rememberMeCheck) {
                                        dataStoreManager.saveToDataStore(

                                            UserDetailStored(
                                                storedemail = email,
                                                storedpassword = password,
                                                storedisChecked = rememberMeCheck
                                            )

                                        )
                                        registerViewModel.SignInUserWithEmailAndPassword(email,password){

                                            navController.navigate(Routs.HomeScreen){
                                                popUpTo(0)
                                            }

                                        }
                                    } else {
                                        registerViewModel.SignInUserWithEmailAndPassword(email,password){

                                            navController.navigate(Routs.HomeScreen){
                                                popUpTo(0)
                                            }

                                        }
                                        dataStoreManager.clearDataStore()


                                    }


                                }

                            },
                            enabled = valids,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp)
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
                                        text = "Log In",
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


}