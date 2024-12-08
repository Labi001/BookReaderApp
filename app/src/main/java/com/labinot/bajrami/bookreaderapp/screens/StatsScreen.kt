package com.labinot.bajrami.bookreaderapp.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.labinot.bajrami.bookreaderapp.components.ChipView
import com.labinot.bajrami.bookreaderapp.helper.formatTimestampToDate
import com.labinot.bajrami.bookreaderapp.models.FireBaseModelBook
import com.labinot.bajrami.bookreaderapp.screens.home.HomeScreenUIEvents
import com.labinot.bajrami.bookreaderapp.screens.home.HomeScreenViewModel

@Composable
fun StatsScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel()
) {

    val uiState = homeScreenViewModel.uiState.collectAsState()

    val loading = remember {
        mutableStateOf(false)
    }

    val allBooks = remember {
        mutableStateOf<List<FireBaseModelBook?>>(emptyList())
    }

    val currentUser = FirebaseAuth.getInstance().currentUser

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {

            StatsTopAppBar {

                navController.navigateUp()
            }

        },
        containerColor = MaterialTheme.colorScheme.primary,
        content = { innerPandding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPandding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {

                when (uiState.value) {

                    is HomeScreenUIEvents.Error -> {
                        loading.value = false
                    }

                    HomeScreenUIEvents.Loading -> {
                        loading.value = true
                    }

                    is HomeScreenUIEvents.Success -> {

                        val data = (uiState.value as HomeScreenUIEvents.Success)

                        allBooks.value = data.databaseBooks

                        loading.value = false

                    }

                }

                val readBooksList = allBooks.value.filter { book ->

                    book?.userId == currentUser?.uid.toString() && book.finishedReading != null
                }

                val readingBooksList = allBooks.value.filter { book ->

                    book?.userId == currentUser?.uid.toString() && book.startedReading != null && book.finishedReading == null
                }

                val statsBooksList = allBooks.value.filter { book ->

                    book?.userId == currentUser?.uid.toString() && book.startedReading != null && book.finishedReading != null
                }



                Spacer(modifier = Modifier.height(10.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.TopStart
                )
                {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp, vertical = 15.dp),
                    ) {

                        Text(
                            "Your Stats",
                            modifier = Modifier.padding(start = 5.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 2.dp,
                            color = Color.White.copy(alpha = 0.7f)
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            "You're reading:  ${readingBooksList.size} Books",
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)

                        )

                        Text(
                            "You've read:  ${readBooksList.size} Books",
                            modifier = Modifier.padding(start = 10.dp),
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )


                    }


                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Book Stats List:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    textAlign = TextAlign.Start,
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(5.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {


                    items(statsBooksList){ mBook ->

                      MBookRow(mBook)
                      {


                      }


                    }



                }




            }

        })

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsTopAppBar(
    onBackIconClick: () -> Unit
) {

    CenterAlignedTopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Text(
                text = "Stats Screen",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        },
        navigationIcon = {

            FilledIconButton(
                onClick = { onBackIconClick() },
                modifier = Modifier
                    .size(50.dp)
                    .padding(start = 4.dp, top = 4.dp),
                shape = CircleShape,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                )
            )
            {

                Image(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(1.dp),
                    contentDescription = "Delete Icon",
                    contentScale = ContentScale.Inside,
                    colorFilter = ColorFilter.tint(
                        color = Color.White
                    )
                )


            }


        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )

}

@Composable
fun MBookRow(
    book: FireBaseModelBook?,
    onCardBookClick: (FireBaseModelBook?) -> Unit
) {

    val context = LocalContext.current.applicationContext
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }


    Card(
        onClick = { onCardBookClick(book) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp)
            .clip(RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = book?.photoUrl,
                modifier = Modifier
                    .size(98.dp, 145.dp)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentDescription = "Book Image",
                imageLoader = imageLoader,
                contentScale = ContentScale.FillBounds
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth()
                    .padding(end = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {

                    Text(
                        text = "by ${book?.author}",
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White.copy(alpha = 0.7f)
                    )

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

                }


                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = book?.title ?: "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 12.dp),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                ChipView(txt = book?.categories)

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = buildAnnotatedString {


                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)){
                            append("Started:  ")
                        }

                        append(formatTimestampToDate(book?.startedReading))

                    },
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Text(
                    text = buildAnnotatedString {


                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)){
                            append("Finished:  ")
                        }

                        append(formatTimestampToDate(book?.finishedReading))

                    },
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.White.copy(alpha = 0.7f)
                )

                Spacer(modifier = Modifier.height(15.dp))


            }


        }


    }


}
