package com.labinot.bajrami.bookreaderapp.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.firebase.auth.FirebaseAuth
import com.labinot.bajrami.bookreaderapp.models.BookArgument
import com.labinot.bajrami.bookreaderapp.screens.DetailScreen
import com.labinot.bajrami.bookreaderapp.screens.StatsScreen
import com.labinot.bajrami.bookreaderapp.screens.UpdateScreen
import com.labinot.bajrami.bookreaderapp.screens.home.HomeScreen
import com.labinot.bajrami.bookreaderapp.screens.home.HomeScreenViewModel
import com.labinot.bajrami.bookreaderapp.screens.registeruser.LogInScreen
import com.labinot.bajrami.bookreaderapp.screens.registeruser.RegisterViewModel
import com.labinot.bajrami.bookreaderapp.screens.registeruser.SignUpScreen
import com.labinot.bajrami.bookreaderapp.screens.search.SearchScreen
import com.labinot.bajrami.bookreaderapp.screens.search.SearchViewModel
import kotlin.reflect.typeOf

@SuppressLint("SuspiciousIndentation")
@Composable
fun NavGraphSetup() {

    val navController = rememberNavController()

    val haveUser = remember {

        mutableStateOf(false)
    }

    haveUser.value = !FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()

    NavHost(navController = navController,
        startDestination = if(haveUser.value) Routs.HomeScreen else Routs.LogInScreen)
    {

        composable<Routs.HomeScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(tween(350))
            },
            popEnterTransition = {
                fadeIn(tween(350))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(350))
            },

        ) {

            val homeViewModel : HomeScreenViewModel = hiltViewModel()

            HomeScreen(navController = navController,
                viewModel = homeViewModel)

        }

        composable<Routs.LogInScreen>(

            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(tween(350))
            },
            popEnterTransition = {
                fadeIn(tween(350))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(350))
            },

        ) {

         val registerViewModel : RegisterViewModel = hiltViewModel()
            val state by registerViewModel.state.collectAsStateWithLifecycle()

          LogInScreen(navController = navController,
              onEvent = registerViewModel::onEvents,
              state = state,
              registerViewModel)

        }

        composable<Routs.SignUpScreen>(

            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(tween(350))
            },
            popEnterTransition = {
                fadeIn(tween(350))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(350))
            },

            ) {

            val registerViewModel : RegisterViewModel = hiltViewModel()
            val state by registerViewModel.state.collectAsStateWithLifecycle()

           SignUpScreen(navController = navController,
               onEvent = registerViewModel::onEvents,
               state = state,
               registerViewModel)

        }

        composable<Routs.SearchScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(tween(350))
            },
            popEnterTransition = {
                fadeIn(tween(350))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(350))
            },
        ) {

            val searchViewModel: SearchViewModel = hiltViewModel()
            val searchedBooks = searchViewModel.searchBooks.collectAsLazyPagingItems()

            val loading = remember {
                mutableStateOf(false)
            }

            if(searchedBooks.itemCount != 0)
                loading.value = false


            SearchScreen(
                navController = navController,
                searchedBooks,
                msearch = { query->
                    if(query.trim().isNotEmpty())
                        loading.value = true
                    else
                    loading.value = false

                  searchViewModel.searchImages(query)




                },

                loading.value,
            )

        }

        composable<Routs.DetailScreen>(

            typeMap = mapOf(typeOf<BookArgument>() to productNavType),

            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(tween(350))
            },
            popEnterTransition = {
                fadeIn(tween(350))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(350))
            },

        ) {

            val homeViewModel : HomeScreenViewModel = hiltViewModel()
            val detailArgs = it.toRoute<Routs.DetailScreen>()

            DetailScreen(navController = navController,
                bookItem = detailArgs.bookItem,
                homeViewModel = homeViewModel)

        }

        composable<Routs.UpdateScreen>(

            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(tween(350))
            },
            popEnterTransition = {
                fadeIn(tween(350))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(350))
            },

        ) {

            val updateArgs = it.toRoute<Routs.UpdateScreen>()

           UpdateScreen(navController = navController,
               bookItemId = updateArgs.bookItemId)

        }

        composable<Routs.StatsScreen>(
            enterTransition = {
                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(350))
            },
            exitTransition = {
                fadeOut(tween(350))
            },
            popEnterTransition = {
                fadeIn(tween(350))
            },
            popExitTransition = {
                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(350))
            },
        ) {



            StatsScreen(navController = navController)

        }


    }

}