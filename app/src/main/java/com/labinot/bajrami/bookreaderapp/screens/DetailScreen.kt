package com.labinot.bajrami.bookreaderapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.labinot.bajrami.bookreaderapp.R
import com.labinot.bajrami.bookreaderapp.components.ChipView
import com.labinot.bajrami.bookreaderapp.components.SaveAlertDialog
import com.labinot.bajrami.bookreaderapp.models.BookArgument
import com.labinot.bajrami.bookreaderapp.models.FireBaseModelBook
import com.labinot.bajrami.bookreaderapp.navigation.Routs
import com.labinot.bajrami.bookreaderapp.screens.home.HomeScreenUIEvents
import com.labinot.bajrami.bookreaderapp.screens.home.HomeScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    navController: NavController,
    bookItem: BookArgument,
    homeViewModel: HomeScreenViewModel
){


    val uiState = homeViewModel.uiState.collectAsState()


    var isSaveDialogOpen by rememberSaveable {

        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val allBooks = remember {
        mutableStateOf<List<FireBaseModelBook?>>(emptyList())
    }


    when(uiState.value){
        is HomeScreenUIEvents.Error -> {}
        HomeScreenUIEvents.Loading -> {}
        is HomeScreenUIEvents.Success -> {
            val data = (uiState.value as HomeScreenUIEvents.Success)

            allBooks.value = data.databaseBooks

        }
    }



    SaveAlertDialog(
        isOpen = isSaveDialogOpen,
        title = "Save Book!",
        bodyText = "Are You sure that you want to save this Book? \n" +
                 "This book it will be stored to BookList!",
        OnDismissRequest = {

            isSaveDialogOpen = false
        },
        OnConfirmButtonClick = {

           val fbook = FireBaseModelBook(

               title = bookItem.title,
               author =bookItem.authors,
               description = bookItem.description,
               categories = bookItem.categories,
               notes = "",
               photoUrl = bookItem.imageUrl,
               publishedDate = bookItem.publishedDate,
               pageCount = bookItem.pageCount.toString(),
               rating = 0.0,
               googleBookId = bookItem.bookId,
               userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

           )

            savedToFireBase(fbook,
                navController,
                snackbarHostState,
                scope,
                allBooks.value,
                bookItem.bookId,
                currentUser = FirebaseAuth.getInstance().currentUser)

            isSaveDialogOpen = false

        }
    )

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .clip(RoundedCornerShape(8.dp)),
        snackbarHost = {

            SnackbarHost(hostState = snackbarHostState){

                Snackbar(
                    snackbarData = it,
                    containerColor = Color.Black,
                    contentColor = Color.White
                )

            }

        },
        topBar = {

        DetailTopAppBar {
            navController.navigateUp()
        }

        },
        containerColor = MaterialTheme.colorScheme.primary,
        content = { innerPandding->



            Column(modifier = Modifier
                .fillMaxSize()
                .padding(innerPandding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally)
            {

                Spacer(modifier = Modifier.height(30.dp))


                ConstraintLayout(modifier = Modifier.fillMaxSize()
                    .padding(top = innerPandding.calculateTopPadding())) {

                    val (backgroundCard,content,imageId,description,button) = createRefs()

                    val context = LocalContext.current.applicationContext
                    val imageLoader = remember(context) {
                        ImageLoader.Builder(context)
                            .crossfade(true)
                            .build()
                    }

                    val localDims = LocalContext.current.resources.displayMetrics


                    val cleanDescription = HtmlCompat.fromHtml(bookItem.description,
                        HtmlCompat.FROM_HTML_MODE_LEGACY).toString()


                    Box(modifier = Modifier.height(245.dp)
                        .padding(horizontal = 25.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .constrainAs(backgroundCard){

                            start.linkTo(parent.start)
                            end.linkTo(parent.end)

                        }
                        )


                    Column(modifier = Modifier.fillMaxWidth()
                        .constrainAs(imageId){

                            top.linkTo(parent.top)
                            bottom.linkTo(backgroundCard.top)

                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center)
                    {

                        val isVisible = remember {
                            mutableStateOf(false)
                        }

                        LaunchedEffect(Unit ) {

                            isVisible.value = true

                        }

                            AsyncImage(model = bookItem.imageUrl,
                                modifier = Modifier
                                    .size(150.dp, 210.dp)
                                    .padding(12.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentDescription = "Book Image",
                                imageLoader = imageLoader,
                                contentScale = ContentScale.FillBounds)


                    }


                    Column(modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .constrainAs(content){

                            top.linkTo(imageId.bottom)
                            start.linkTo(imageId.start)
                            end.linkTo(imageId.end)

                        },
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally)
                    {



                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = bookItem.title,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 2,
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

                                val ratingm:String = if(bookItem.rating == 0.0) "N/A" else bookItem.rating.toString()

                                Text(text = ratingm,
                                    fontSize = 10.sp,
                                    color = Color.White.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.bodySmall)

                                Spacer(modifier = Modifier.width(15.dp))

                                Text(text = "by ${bookItem.authors}",
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = Color.White.copy(alpha = 0.7f))

                            }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(Modifier.wrapContentSize(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically) {



                            Text(text = "Published Date: ",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold)



                            Text(text = " ${bookItem.publishedDate}",
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White.copy(alpha = 0.7f),
                                fontWeight = FontWeight.SemiBold)

                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        ChipView(bookItem.categories)

                        Spacer(modifier = Modifier.height(5.dp))







                    }



                    Column(modifier = Modifier
                        .padding(horizontal = 25.dp)
                        .constrainAs(description){

                            top.linkTo(backgroundCard.bottom)
                            bottom.linkTo(button.top)

                        },
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top)
                    {

                        Text(text = "Description",
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start,
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold)

                        Spacer(Modifier.height(10.dp))

                        Surface(modifier = Modifier
                            .fillMaxWidth()
                            .height(localDims.heightPixels.dp.times(0.09f)),
                            color = Color.Transparent,
                        ) {

                            LazyColumn(modifier = Modifier.padding(3.dp)) {


                                item {


                                        Text(text = cleanDescription,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Start,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = Color.White.copy(alpha = 0.7f))



                                }


                            }


                        }





                    }

                    Column(modifier = Modifier.fillMaxWidth()
                        .constrainAs(button){

                            top.linkTo(description.bottom)
                            bottom.linkTo(parent.bottom)

                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center)
                    {

                        val isVisible = remember {
                            mutableStateOf(false)
                        }

                        LaunchedEffect(Unit ) {

                            isVisible.value = true

                        }



                            Button(onClick = {
                                isSaveDialogOpen = true
                            },
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 40.dp)
                                    .clip(RoundedCornerShape(30.dp)),
                                shape = RoundedCornerShape(30.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = Color.White
                                )
                            ) {

                                Text(
                                    text = "Save",
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

        })


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTopAppBar(
    onBackClickIcon:() -> Unit
){


    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 25.dp),
        title = {
        Text(text = "Detail Screen",
             style = MaterialTheme.typography.headlineSmall,
            color = Color.White)
    },
        navigationIcon = {

            Column(modifier = Modifier.wrapContentSize()) {

                Spacer(modifier = Modifier.height(15.dp))

                FilledIconButton(
                    modifier = Modifier.size(50.dp),
                    onClick = { onBackClickIcon() },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                )
                {

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.back_narrow),
                        tint = Color.White
                    )

                }


            }




        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ))


}



fun savedToFireBase(
    mBook: FireBaseModelBook,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    allBooks: List<FireBaseModelBook?>,
    bookId: String,
    currentUser: FirebaseUser?
) {

    val isBookSaved = allBooks.filter { it?.googleBookId == bookId && it.userId == currentUser?.uid.toString() }

    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")

    if(mBook.toString().isNotEmpty()){

        if(isBookSaved.isNotEmpty()){

            scope.launch {
                snackbarHostState
                    .showSnackbar("This book is already saved!",
                        duration = SnackbarDuration.Short)

            }


        }else{

            dbCollection.add(mBook).addOnSuccessListener { documentRef->

                val docId = documentRef.id
                dbCollection.document(docId)
                    .update(hashMapOf("id" to docId) as Map<String,Any>)
                    .addOnCompleteListener{ task->

                        if(task.isSuccessful){

                            scope.launch {

                                snackbarHostState
                                    .showSnackbar("Book Saved !",
                                        duration = SnackbarDuration.Short)

                                delay(100)


                                navController.navigate(Routs.HomeScreen){

                                    popUpTo(0)
                                }

                            }





                        }else{

                            scope.launch {
                                snackbarHostState
                                    .showSnackbar("Something Went Wrong ! Try again",
                                        duration = SnackbarDuration.Short)

                            }




                        }


                    }

                    .addOnFailureListener{ message->

                        scope.launch {
                            snackbarHostState
                                .showSnackbar("Failed: ${message.message}",
                                    duration = SnackbarDuration.Short)

                        }

                    }


            }

        }




    }else{

        scope.launch {
            snackbarHostState
                .showSnackbar("There is No Book to Save",
                    duration = SnackbarDuration.Short)

        }


    }

}