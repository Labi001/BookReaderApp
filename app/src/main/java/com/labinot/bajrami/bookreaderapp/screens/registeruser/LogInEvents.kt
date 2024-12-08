package com.labinot.bajrami.booksapp.screens.logIn



sealed class LogInEvents {

    data class OnEmailLogInChange(val email_LogIn:String) : LogInEvents()
    data class OnPasswordLogInChange(val password_LogIn:String) : LogInEvents()

    data class OnEmailSignUpChange(val email_signUp:String) : LogInEvents()
    data class OnPasswordSigUpChange(val password_signUp:String) : LogInEvents()
    data class OnPasswordRepeatSigUpChange(val password_repeat_signUp:String) : LogInEvents()

    data object OnEmptyText: LogInEvents()


}