package com.labinot.bajrami.bookreaderapp.screens

import android.annotation.SuppressLint
import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.labinot.bajrami.bookreaderapp.R
import com.labinot.bajrami.bookreaderapp.components.ChipView
import com.labinot.bajrami.bookreaderapp.components.NoteField
import com.labinot.bajrami.bookreaderapp.components.SaveAlertDialog
import com.labinot.bajrami.bookreaderapp.helper.formatDate
import com.labinot.bajrami.bookreaderapp.models.FireBaseModelBook
import com.labinot.bajrami.bookreaderapp.navigation.Routs
import com.labinot.bajrami.bookreaderapp.screens.home.HomeScreenUIEvents
import com.labinot.bajrami.bookreaderapp.screens.home.HomeScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("SuspiciousIndentation")
@Composable
fun UpdateScreen(
    navController: NavController,
    bookItemId: String,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {


    val uiState = homeScreenViewModel.uiState.collectAsState()

    val loading = remember {
        mutableStateOf(false)
    }

    val error = remember {
        mutableStateOf<String?>(null)
    }

    val allBooks = remember {
        mutableStateOf<List<FireBaseModelBook?>>(emptyList())
    }



    val ratingVal = remember {

        mutableStateOf(0)
    }


    var isDeleteDialogOpen by rememberSaveable {

        mutableStateOf(false)
    }

    val isStartedReading = remember {

        mutableStateOf(false)
    }

    val isFinishedReading = remember {

        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    SaveAlertDialog(
        isOpen = isDeleteDialogOpen,
        title = "Delete Book!",
        bodyText = "Are You sure that you want to delete this Book? \n" +
                "This action is not reversible!",
        OnDismissRequest = {

            isDeleteDialogOpen = false
        },
        OnConfirmButtonClick = {

            isDeleteDialogOpen = false

            FirebaseFirestore.getInstance()
                .collection("books")
                .document(bookItemId)
                .delete()
                .addOnCompleteListener{

                    if(it.isSuccessful){

                        scope.launch {

                            snackbarHostState
                                .showSnackbar("Book Deleted Successfully !",
                                    duration = SnackbarDuration.Short)


                            delay(200)


                            navController.navigate(Routs.HomeScreen){

                                popUpTo(0)
                            }

                        }



                    }else{

                        scope.launch {

                            snackbarHostState
                                .showSnackbar("Something Went wrong.Try Again !",
                                    duration = SnackbarDuration.Short)


                        }


                    }
                }

                .addOnFailureListener{ errorMsg ->

                    scope.launch {

                        snackbarHostState
                            .showSnackbar("Error: ${errorMsg.message} !",
                                duration = SnackbarDuration.Short)


                    }


                }



        }
    )


    Scaffold(modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.primary,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState){

                Snackbar(
                    snackbarData = it,
                    containerColor = Color.Black,
                    contentColor = Color.White
                )

            }


        },
        content = { innerPandding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPandding),
            )
            {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                )
                {

                    FilledIconButton(
                        modifier = Modifier
                            .size(50.dp),
                        onClick = { navController.navigateUp() },
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




                    Text(
                        text = "Update Book",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )


                    FilledIconButton(
                        onClick = { isDeleteDialogOpen = true },
                        modifier = Modifier.size(50.dp),
                        shape = CircleShape,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor =  MaterialTheme.colorScheme.secondary,
                        )
                    )
                    {

                        Image(painter = painterResource(id = R.drawable.delete),
                            modifier = Modifier
                                .size(30.dp)
                                .padding(5.dp),
                            contentDescription = "Delete Icon",
                            contentScale = ContentScale.Inside,
                            colorFilter = ColorFilter.tint(
                                color = Color.White
                            ))


                    }



                }

                when (uiState.value) {
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


                Spacer(modifier = Modifier.height(10.dp))


                if (loading.value) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    )
                    {

                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = Color.White,
                            strokeWidth = 3.dp
                        )


                    }


                }



                if (allBooks.value.isNotEmpty()){

                    val myBook = allBooks.value.find { book->
                        book?.id == bookItemId
                    }

                    var value by remember {
                        mutableStateOf("")
                    }

                    val changedNotes = myBook?.notes != value
                    val changedRates = myBook?.rating?.toInt() != ratingVal.value
                    val isFinishedTimeStamp = if(isFinishedReading.value) Timestamp.now() else myBook?.finishedReading
                    val isStartedTimeStamp = if(isStartedReading.value) Timestamp.now() else myBook?.startedReading

                    val bookUpdate = changedNotes || changedRates || isStartedReading.value || isFinishedReading.value


                    val bookToUpdate = hashMapOf(

                        "finished_reading" to isFinishedTimeStamp,
                        "started_reading" to isStartedTimeStamp,
                        "rating" to ratingVal.value,
                        "notes" to value).toMap()


                    LazyColumn(modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally) {



                        item {
                            MBookItem(myBook)

                        }


                        item {
                            Spacer(modifier = Modifier.height(10.dp))


                                NoteField(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done,
                                    defaultValues = if(myBook?.notes.toString().isNotBlank())myBook?.notes.toString() else "",
                                    onDone = { noteText->

                                        value = noteText

                                    }

                                )

                            Spacer(modifier = Modifier.height(15.dp))

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 25.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically)
                            {

                                TextButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f)
                                        .padding(horizontal = 5.dp),
                                    enabled = myBook?.startedReading ==  null,
                                    onClick = {isStartedReading.value = true},

                                )
                                {


                                    if(myBook?.startedReading == null){

                                        if(!isStartedReading.value){

                                            Text(
                                                text = "Start Reading",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White.copy(alpha = 0.9f),
                                                fontWeight = FontWeight.Normal)
                                        }else {

                                            Text(text = "Started Reading",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White.copy(alpha = 0.9f),
                                                fontWeight = FontWeight.Normal)

                                        }

                                    }else{

                                        Text(text = "Started on: ${formatDate(myBook.startedReading!!) }",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontWeight = FontWeight.Normal)
                                    }



                                }

                                TextButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    onClick = {isFinishedReading.value = true},
                                    enabled = myBook?.finishedReading == null

                                )
                                {

                                    if(myBook?.finishedReading == null) {

                                        if(!isFinishedReading.value){

                                            Text(
                                                text = "Mark as Read",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White.copy(alpha = 0.9f),
                                                fontWeight = FontWeight.Normal,
                                            )

                                        }else {

                                            Text(text = "Finished Reading",
                                                style = MaterialTheme.typography.titleMedium,
                                                color = Color.White.copy(alpha = 0.9f),
                                                fontWeight = FontWeight.Normal)

                                        }


                                    }else{

                                        Text(text = "Finished on: ${formatDate(myBook.finishedReading!!)}",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontWeight = FontWeight.Normal)
                                    }



                                }


                            }

                        }

                        item {
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(text = "Rate this Book",
                                modifier = Modifier
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(18.dp))

                           RatingBar(rating = myBook?.rating?.toInt()?:0) { rating->

                               ratingVal.value = rating

                           }

                            Spacer(modifier = Modifier.height(20.dp))

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 25.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically)

                            {

                                Button( onClick = {

                                    if(bookUpdate){

                                        FirebaseFirestore.getInstance()
                                            .collection("books")
                                            .document(bookItemId)
                                            .update(bookToUpdate)
                                            .addOnCompleteListener(){ task ->

                                                if(task.isSuccessful){

                                                    scope.launch {

                                                        snackbarHostState
                                                            .showSnackbar("Book Updated Successfully !",
                                                                duration = SnackbarDuration.Short)

                                                        delay(100)


                                                        navController.navigate(Routs.HomeScreen){

                                                            popUpTo(0)
                                                        }

                                                    }

                                                }



                                            }.addOnFailureListener { errorMsg->


                                                scope.launch {

                                                    snackbarHostState
                                                        .showSnackbar("Error updating Document:  ${errorMsg.message} !",
                                                            duration = SnackbarDuration.Short)

                                                }


                                            }

                                    }



                                },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp)
                                        .clip(RoundedCornerShape(30.dp)),
                                    shape = RoundedCornerShape(30.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.tertiary,
                                        contentColor = Color.White
                                    )
                                ) {

                                    Text(
                                        text = "Update",
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

        })

}

@Composable
fun MBookItem(
    book: FireBaseModelBook?,
) {

    val context = LocalContext.current.applicationContext
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }


    val cleanDescription = HtmlCompat.fromHtml(book?.description?:"",
        HtmlCompat.FROM_HTML_MODE_LEGACY).toString()


    Card(
        onClick = {  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 15.dp)
            .clip(RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
    ) {

        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AsyncImage(
                    model = book?.photoUrl,
                    modifier = Modifier
                        .size(118.dp, 165.dp)
                        .padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 10.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentDescription = "Book Image",
                    imageLoader = imageLoader,
                    contentScale = ContentScale.FillBounds
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "by ${book?.author}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = book?.title ?: "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 12.dp),
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        textAlign = TextAlign.Start,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(10.dp))

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

                    Spacer(modifier = Modifier.height(12.dp))

                    ChipView(txt = book?.categories)

                    Spacer(modifier = Modifier.height(15.dp))


                }


            }


        }


    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(8.dp))

        Column(modifier = Modifier
            .padding(horizontal = 10.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top)
        {

            Text(text = "Description",
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                color = Color.White.copy(alpha = 0.9f),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold)

            Spacer(Modifier.height(10.dp))

            Text(text = cleanDescription,
                modifier = Modifier.fillMaxWidth()
                    .padding(bottom = 10.dp),
                textAlign = TextAlign.Start,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f))









        }
        Spacer(modifier = Modifier.height(5.dp))

    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingBar(rating: Int,
              onPressRating:(Int) -> Unit) {

    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = "star",
                modifier = Modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }

                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFF6C12D) else Color(0xFFA2ADB1)
            )
        }


    }
}