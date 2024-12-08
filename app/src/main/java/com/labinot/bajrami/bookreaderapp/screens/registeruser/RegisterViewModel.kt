package com.labinot.bajrami.bookreaderapp.screens.registeruser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.labinot.bajrami.bookreaderapp.helper.Constants
import com.labinot.bajrami.bookreaderapp.models.MUser
import com.labinot.bajrami.booksapp.screens.logIn.LogInEvents
import com.labinot.bajrami.booksapp.screens.logIn.LogInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class RegisterViewModel:ViewModel() {

    private val _state = MutableStateFlow(LogInState())
    private val auth: FirebaseAuth = Firebase.auth

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    val state: StateFlow<LogInState> = _state.asStateFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), LogInState())

    private val _toastMessage = MutableLiveData("")
    val toastMessage: LiveData<String> = _toastMessage



    fun CreateUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {

            try {

                if (_loading.value == false) {

                    _loading.value = true
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                _toastMessage.value = "SignUp Successful"
                                val displayName = task.result.user?.email?.split('@','.')?.get(0)
                                createUser(displayName)
                                home()

                            } else {
                                _toastMessage.value = "${task.result}"
                                Log.d("FB", "Error: ${task.result}")
                            }

                            _loading.value = false
                        }

                        .addOnFailureListener{ errormsg->

                            _toastMessage.value = "${errormsg.message}"
                        }
                }

            } catch (ex: Exception) {
                _toastMessage.value = "${ex.message}"
                Log.d("FB", "signWithEmailAndPassword: ${ex.message}")
            }

        }

    fun SignInUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {

            try {
                if (_loading.value == false) {
                    _loading.value = true
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->

                            if (task.isSuccessful) {
                                _toastMessage.value = "LogIn Successful"
                                home()

                            } else {
                                val errorMessage = task.exception?.message ?: "Unknown error"
                                Log.d("FB", "Error: $errorMessage")
                            }

                            _loading.value = false
                        }

                        .addOnFailureListener{ exception->

                            _toastMessage.value = exception.message?:"Unknown error"
                        }

                }
            } catch (ex: Exception) {

                _toastMessage.value = ex.message?:"Unknown error"
                Log.d("FB", "signWithEmailAndPassword: ${ex.message}")
            }

        }

    fun clearToastMessage() {
        _toastMessage.postValue("")
    }

    private fun createUser(displayName: String?) {

        val userId = auth.currentUser?.uid
        val user = MUser(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Life is Great",
            profession = "Android Developer",
            id = null
        ).toMap()

        FirebaseFirestore.getInstance().collection(Constants.USERS_TABLE)
            .add(user)

    }

    fun onEvents(events: LogInEvents){

        when(events){

            is LogInEvents.OnEmailLogInChange -> {

                _state.update {
                    it.copy(logInEmail = events.email_LogIn)
                }

            }
            is LogInEvents.OnEmailSignUpChange -> {

                _state.update {
                    it.copy(signUpEmail = events.email_signUp)
                }

            }
            is LogInEvents.OnPasswordLogInChange -> {


                _state.update {
                    it.copy(logInPassword = events.password_LogIn)
                }

            }
            is LogInEvents.OnPasswordSigUpChange -> {

                _state.update {
                    it.copy(signUpPassword = events.password_signUp)
                }

            }

            is LogInEvents.OnPasswordRepeatSigUpChange -> {

                _state.update {
                    it.copy(signUpRepeatPassword = events.password_repeat_signUp)
                }

            }

            LogInEvents.OnEmptyText -> {

                _state.update {
                    it.copy(logInEmail = "")
                }

                _state.update {
                    it.copy(logInPassword = "")
                }

            }


        }


    }


}