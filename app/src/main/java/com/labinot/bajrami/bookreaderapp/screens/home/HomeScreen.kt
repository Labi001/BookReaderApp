package com.labinot.bajrami.bookreaderapp.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.labinot.bajrami.bookreaderapp.R
import com.labinot.bajrami.bookreaderapp.components.ChipView
import com.labinot.bajrami.bookreaderapp.components.SaveAlertDialog
import com.labinot.bajrami.bookreaderapp.models.FireBaseModelBook
import com.labinot.bajrami.bookreaderapp.navigation.Routs

@Composable
fun HomeScreen(navController: NavController,
               viewModel: HomeScreenViewModel){

    val uiState = viewModel.uiState.collectAsState()

    val loading = remember {
        mutableStateOf(false)
    }

    val error = remember {
        mutableStateOf<String?>(null)
    }

    var isSignOutDialogOpen by rememberSaveable {

        mutableStateOf(false)
    }

    val allBooks = remember {
        mutableStateOf<List<FireBaseModelBook?>>(emptyList())
    }

  val currentUser = FirebaseAuth.getInstance().currentUser

    val currentUsername = if(!currentUser?.email.isNullOrEmpty())
        currentUser?.email?.split(".","@")?.get(0)?.replaceFirstChar { it.uppercaseChar() }
        else "N/A"


    SaveAlertDialog(
        isOpen = isSignOutDialogOpen,
        title = "Sign Out!",
        bodyText = "Are You sure that you want to Log Out from our app? \n" +
                "This action is not reversible!",
        OnDismissRequest = {

            isSignOutDialogOpen = false
        },
        OnConfirmButtonClick = {

            isSignOutDialogOpen = false

            FirebaseAuth.getInstance()
                .signOut().run {
                    navController.navigate(Routs.LogInScreen){
                        popUpTo(0)

                    }
                }


        }
    )

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(8.dp)),
        topBar = {

            HomeTopAppBar(username = currentUsername?:"N/A",
                onUserIconClick = { navController.navigate(Routs.StatsScreen) },
                onSignOutIconClick = {

                    isSignOutDialogOpen = true

                },
                onSearchIconClick = {
                    navController.navigate(Routs.SearchScreen)
                })
        },
        containerColor = MaterialTheme.colorScheme.primary,
        content = { innerPandding->

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPandding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally)
            {

                when(uiState.value){

                    is HomeScreenUIEvents.Error -> {
                        val errorMsg = (uiState.value as HomeScreenUIEvents.Error).message
                        loading.value = false
                        error.value = errorMsg
                    }
                    HomeScreenUIEvents.Loading -> {
                        loading.value = true
                        error.value = null
                    }
                    is HomeScreenUIEvents.Success -> {

                        val data = (uiState.value as HomeScreenUIEvents.Success)


                        allBooks.value = data.databaseBooks
                        loading.value = false
                        error.value = null

                    }
                }





                Spacer(modifier = Modifier.width(30.dp))

                    
                    Text(text = "Reading right now:",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 20.dp),
                        textAlign = TextAlign.Start,
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall
                        )
                Spacer(modifier = Modifier.height(10.dp))



                ReadingRightNowList(modifier = Modifier.weight(0.6f),
                    allBooks.value,
                    loading.value,
                    error.value,
                    currentUser,
                    navController)

                Spacer(modifier = Modifier.width(10.dp))

                ReadingList(modifier = Modifier.weight(1f),
                    allBooks.value,
                    loading.value,
                    error.value,
                    currentUser,
                    navController)



            }

        })



}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppBar(username: String,
                          onUserIconClick:() ->Unit,
                          onSignOutIconClick:() ->Unit,
                          onSearchIconClick:() ->Unit){

    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        title = {


            Column(modifier = Modifier.wrapContentSize()) {

                Column(modifier = Modifier.padding(start = 8.dp)) {

                    Text(text = "Hello",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White)


                    Text(text = username,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                       )

                }


            }

        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        navigationIcon = {

            FilledIconButton(
                onClick = { onUserIconClick() },
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor =  MaterialTheme.colorScheme.secondary,
                )
            )
            {

                Image(painter = painterResource(id = R.drawable.ic_profile),
                    modifier = Modifier
                        .size(30.dp)
                        .padding(5.dp),
                    contentDescription = "Profile Icon",
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(
                        color = Color.White
                    ))


            }


        },

        actions = {

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp) // Add spacing between icons
            ) {

                FilledTonalIconButton(
                    onClick = { onSearchIconClick() },
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor =  MaterialTheme.colorScheme.secondary,

                        )
                )
                {

                    Image(painter = painterResource(id = R.drawable.magnifier),
                        modifier = Modifier
                            .size(30.dp)
                            .padding(5.dp),
                        contentDescription = "Search Icon",
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(
                            color = Color.White
                        )
                    )


                }

                FilledIconButton(
                    onClick = { onSignOutIconClick() },
                    modifier = Modifier.size(50.dp),
                    shape = CircleShape,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,

                        )
                )
                {

                    Image(painter = painterResource(id = R.drawable.ic_logout),
                        modifier = Modifier
                            .size(30.dp)
                            .padding(5.dp),
                        contentDescription = "Log Out Icon",
                        contentScale = ContentScale.Inside,
                        colorFilter = ColorFilter.tint(
                            color = Color.White
                        )
                    )


                }

            }





        }
    )
    
    
}

@Composable
fun ReadingRightNowList(
    modifier: Modifier,
    allBooks: List<FireBaseModelBook?>,
    isLoading: Boolean,
    errorMsg: String?,
    currentUser: FirebaseUser?,
    navController: NavController
) {



    Row(modifier = modifier.fillMaxSize())
    {


        if(isLoading){

                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                    )
                {
                    CircularProgressIndicator(modifier = Modifier.size(40.dp),
                        color = Color.White,
                        strokeWidth = 4.dp)

                    Spacer(modifier = Modifier.height(5.dp))

                    Text("Loading...",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = Color.White)

                }


        }

        errorMsg?.let {

            Text(text = it,
                style = MaterialTheme.typography.bodyMedium)
        }


        val readingList = allBooks.filter { book->

            book?.userId == currentUser?.uid.toString() && book.startedReading != null && book.finishedReading == null
        }

        if(readingList.isEmpty()){

            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top) {

                Spacer(modifier = Modifier.height(30.dp))

                Icon(painter = painterResource(R.drawable.empty_archive),
                    modifier = Modifier.size(100.dp),
                    contentDescription = "Icon Empty",
                    tint = Color.White.copy(alpha = 0.7f))

                Spacer(modifier = Modifier.height(10.dp))

                Text("There is No Book Saved!",
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium
                )


            }




        }

        if(readingList.isNotEmpty()){

            LazyRow(contentPadding = PaddingValues(2.dp),
                modifier = Modifier.fillMaxWidth())
            {

                items(readingList){ book->

                 BookItemColumn(book,
                     navController)

                }

            }


        }


    }


}

@Composable
fun BookItemColumn(book: FireBaseModelBook?,
                   navController: NavController) {

    val context = LocalContext.current.applicationContext

    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()

    }

        Column(modifier = Modifier
            .width(140.dp)
            .fillMaxHeight()
            .padding(horizontal = 7.dp, vertical = 10.dp)
            .clickable { navController.navigate(Routs.UpdateScreen(bookItemId = book?.id?:"")) },
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top)
        {

            Row(
                Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Star,
                    modifier = Modifier.size(12.dp),
                    contentDescription = "Star Icon",
                    tint = Color.Yellow
                )

                Spacer(modifier = Modifier.width(5.dp))

                val ratingm: String =
                    if (book?.rating == 0.0) "N/A" else book?.rating.toString()

                Text(
                    text = ratingm,
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall
                )

            }

            Spacer(modifier = Modifier.height(4.dp))

            AsyncImage(
                model = book?.photoUrl,
                modifier = Modifier.fillMaxWidth()
                    .height(170.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentDescription = null,
                imageLoader = imageLoader,
                contentScale = ContentScale.Crop

            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = book?.title ?: "",
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                color = Color.White
            )

                Text(
                    text = "by ${book?.author}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.7f)
                )



        }


    }

@Composable
fun ReadingList(
    modifier: Modifier,
    allBooks: List<FireBaseModelBook?>,
    isLoading: Boolean,
    errorMsg: String?,
    currentUser: FirebaseUser?,
    navController: NavController
) {

    Column(modifier = modifier.fillMaxSize()
        .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
        .background(MaterialTheme.colorScheme.secondary)
       )
    {

        Spacer(modifier = Modifier.width(30.dp))


        Text(text = "Reading List:",
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 20.dp, bottom = 15.dp),
            textAlign = TextAlign.Start,
            color = Color.White,
            style = MaterialTheme.typography.headlineSmall
        )

        if(isLoading){

            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center)
            {
                Column(modifier = Modifier.wrapContentSize()) {  }

                CircularProgressIndicator(modifier = Modifier.size(40.dp))
                Text("Loading...",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)

            }


        }

        errorMsg?.let {

            Text(text = it,
                style = MaterialTheme.typography.bodyMedium)
        }

        val readingList = allBooks.filter { book->

            book?.userId == currentUser?.uid.toString() && book.startedReading == null && book.finishedReading == null
        }




        if(readingList.isNotEmpty()){

            LazyColumn(modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp))
            {

                items(readingList){ book->

                    MBookItem(book,
                        navController)

                }

            }


        }




    }


}

@Composable
fun MBookItem(book: FireBaseModelBook?,
              navController: NavController
             ) {

    val context = LocalContext.current.applicationContext
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }


    Card(onClick = { navController.navigate(Routs.UpdateScreen(bookItemId = book?.id?:""))},
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
    ) {

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically) {

            AsyncImage(model = book?.photoUrl,
                modifier = Modifier
                    .size(98.dp, 125.dp)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentDescription = "Book Image",
                imageLoader = imageLoader,
                contentScale = ContentScale.FillBounds)

            Spacer(modifier = Modifier.width(10.dp))

            Column {

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "by ${book?.author}",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.7f))

                Spacer(modifier = Modifier.height(10.dp))

                Text(text = book?.title?:"",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White)

                Spacer(modifier = Modifier.height(10.dp))

                Row(Modifier.wrapContentSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically) {

                    Icon(imageVector = Icons.Default.Star,
                        modifier = Modifier.size(12.dp),
                        contentDescription = "Star Icon",
                        tint = Color.Yellow)

                    Spacer(modifier = Modifier.width(5.dp))

                    val ratingm:String = if(book?.rating == 0.0) "N/A" else book?.rating.toString()

                    Text(text = ratingm,
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.bodySmall)

                }

                Spacer(modifier = Modifier.height(12.dp))

                ChipView(txt = book?.categories)

                Spacer(modifier = Modifier.height(15.dp))



            }


        }



    }




}

