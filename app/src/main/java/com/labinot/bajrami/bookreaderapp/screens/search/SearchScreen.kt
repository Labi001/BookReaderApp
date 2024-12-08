package com.labinot.bajrami.bookreaderapp.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import coil.ImageLoader
import coil.compose.AsyncImage
import com.labinot.bajrami.bookreaderapp.R
import com.labinot.bajrami.bookreaderapp.components.ChipView
import com.labinot.bajrami.bookreaderapp.components.SearchField
import com.labinot.bajrami.bookreaderapp.helper.searchKeyBoard
import com.labinot.bajrami.bookreaderapp.models.BookArgument
import com.labinot.bajrami.bookreaderapp.models.BookItem
import com.labinot.bajrami.bookreaderapp.navigation.Routs
import com.labinot.bajrami.bookreaderapp.prefStored.DataStoreManager
import com.labinot.bajrami.bookreaderapp.prefStored.SearchQueryStored
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    navController: NavController,
    searchedBooks: LazyPagingItems<BookItem>,
    msearch: (String) -> Unit,
    loading: Boolean,

    ){


    var mQuery by remember {
        mutableStateOf("")
    }


    val dataStoreContext = LocalContext.current
    val dataStoreManager = DataStoreManager(dataStoreContext)
    val scope = rememberCoroutineScope()
    val getSearchedQuery by dataStoreManager.getSearchQuery().collectAsState(initial = null)


    LaunchedEffect(key1 = Unit) {


        getSearchedQuery?.searchQuery?.let { msearch(it) }

    }


    var isSuggestionChipsVisible by remember {
        mutableStateOf(false)
    }


    val valid = remember(mQuery) {

        mQuery.trim().isNotEmpty()

    }

    val focusManager = LocalFocusManager.current

    val focusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current


    

    Scaffold{


        Surface(modifier = Modifier
            .fillMaxSize(),
            color = MaterialTheme.colorScheme.primary)
        {

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(it),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally)
            {

                        Spacer(modifier = Modifier.height(20.dp))



                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 25.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically)
                    {
                        FilledIconButton(
                            modifier = Modifier.size(50.dp),
                            onClick = {
                                    keyboardController?.hide()
                                    navController.navigateUp()

                                },
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


                        Text(text = "Explore thousands of books in go",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 25.dp,
                                    end = 40.dp
                                )
                                .clickable { msearch(mQuery) },
                            maxLines = 2,
                            textAlign = TextAlign.Start,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )


                    }

                        Spacer(modifier = Modifier.height(25.dp))

                        SearchField(
                            modifier = Modifier.focusRequester(focusRequester)
                                .onFocusChanged { isSuggestionChipsVisible = it.isFocused }
                                .padding(horizontal = 25.dp),
                            hint = "Search for books...",
                            imeAction = ImeAction.Search,
                            onAction = KeyboardActions {

                                if(!valid) return@KeyboardActions
                                else {

                                    scope.launch {

                                        dataStoreManager.saveSearchQuery(
                                            SearchQueryStored(searchQuery = mQuery))

                                    }

                                    msearch(mQuery)
                                    mQuery = ""
                                    keyboardController?.hide()
                                    focusManager.clearFocus()

                                }

                            },
                            leadingIcon = R.drawable.search_modified,
                            values = mQuery,
                            onValueChange = {

                                mQuery = it
                            })

                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }

                AnimatedVisibility(visible = isSuggestionChipsVisible) {

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        items(searchKeyBoard){ keyword ->


                            SuggestionChip(
                                onClick = {
                                    mQuery = keyword
                                    scope.launch {

                                        dataStoreManager.saveSearchQuery(
                                            SearchQueryStored(searchQuery = mQuery))

                                    }
                                    msearch(keyword)
                                    keyboardController?.hide()
                                    focusManager.clearFocus()
                                    mQuery = ""

                                },
                                label = {

                                    Text(text = keyword,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontSize = 10.sp)
                                },
                                icon = {

                                    Icon(imageVector = Icons.Filled.Search,
                                        modifier = Modifier.size(15.dp),
                                        contentDescription = "Search Icon",
                                        tint = MaterialTheme.colorScheme.tertiary)

                                },

                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                                colors = SuggestionChipDefaults.suggestionChipColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.10f),
                                    iconContentColor = MaterialTheme.colorScheme.tertiary,
                                    labelColor = MaterialTheme.colorScheme.tertiary,
                                    disabledLabelColor = MaterialTheme.colorScheme.tertiary,
                                )
                            )


                        }



                    }



                }


                        Spacer(modifier = Modifier.height(20.dp))

                val isVisible = remember {
                    mutableStateOf(false)
                }

                LaunchedEffect(Unit ) {

                    isVisible.value = true

                }

                AnimatedVisibility(visible = isVisible.value,
                    enter = fadeIn() + expandVertically(),

                ) {

                    Text(text = "Famous Books",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 26.dp),
                        textAlign = TextAlign.Start,
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold)

                    Spacer(modifier = Modifier.height(10.dp))
                }

                SearchedBooksList(books = searchedBooks,
                    loading = loading,
                    ){ mbook->

                    keyboardController?.hide()
                    navController.navigate(Routs.DetailScreen(bookItem = BookArgument.fromBookItem(mbook)))
                }




                            

                
            }



        }

    }

}

@Composable
fun SearchedBooksList(
    books: LazyPagingItems<BookItem>,
    loading: Boolean = false,
    onCardBookClick: (BookItem?) -> Unit,

) {

      if(loading){

          Box(modifier = Modifier.fillMaxSize(),
              contentAlignment = Alignment.Center
          )
          {

              CircularProgressIndicator(modifier = Modifier
                  .size(30.dp),
                  color = MaterialTheme.colorScheme.tertiary,
                  strokeWidth = 2.dp)

          }

      }





            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp)) {


                items(count = books.itemCount){ index->

                    val book = books[index]

                    MBookItem(book = book,
                        onCardBookClick)

                }

            }










}

@Composable
fun MBookItem(book: BookItem?,
              onCardBookClick: (BookItem?) -> Unit) {

    val context = LocalContext.current.applicationContext
    val imageLoader = remember(context) {
        ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }


        Card(onClick = { onCardBookClick(book) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .clip(RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ),
        ) {

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {

                AsyncImage(model = book?.imageUrl,
                    modifier = Modifier
                        .size(98.dp, 145.dp)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentDescription = "Book Image",
                    imageLoader = imageLoader,
                    contentScale = ContentScale.FillBounds)

                Spacer(modifier = Modifier.width(16.dp))

                Column {

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "by ${book?.authors}",
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
