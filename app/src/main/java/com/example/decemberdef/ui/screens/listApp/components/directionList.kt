package com.example.decemberdef.ui.screens.listApp.components

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.decemberdef.R
import com.example.decemberdef.data.Direction
import com.example.decemberdef.ui.screens.listApp.DirectionListViewModel
import com.google.firebase.Firebase
import com.google.firebase.dynamiclinks.androidParameters
import com.google.firebase.dynamiclinks.dynamicLinks
import com.google.firebase.dynamiclinks.shortLinkAsync
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@Composable
fun directionsList(
    onDirectionClick: (Direction) -> Unit,
    viewModel: DirectionListViewModel,
    mainNavControllerDestinationId: Int?,
    isDoneClick: (Boolean, String) -> Unit,
    navController: NavHostController,
    onDescriptionClick: (RichTextState, String) -> Unit,
    directions: List<Direction>,
    link: String
) {
    var localDirections = directions
    var addList: MutableList<Direction> = mutableListOf()
    addList.add(
        Direction(
            title = stringResource(id = R.string.add),
            imgURL = "https://cdn-icons-png.flaticon.com/512/7666/7666164.png"
        )
    )
    Column(modifier = Modifier) {
        LazyColumn(contentPadding = PaddingValues(5.dp)) {
            items(addList) {
                addDirectionItem(
                    direction = it,
                    viewModel = viewModel,
                    modifier = Modifier.padding(8.dp)
                )
            }
            items(localDirections.reversed()) {
                directionItem(
                    direction = it,
                    onDirectionClick = onDirectionClick,
                    onDirectionStatusClick = isDoneClick,
                    onDirectionDescriptionClick = onDescriptionClick,
                    link = link,
                    viewModel = viewModel,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun addDirectionItem(
    direction: Direction,
    viewModel: DirectionListViewModel,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.clickable { viewModel.addCustomDirection() }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.add),
                contentDescription = direction.title,
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}

@Composable
fun directionItem(
    onDirectionStatusClick: (Boolean, String) -> Unit,
    direction: Direction,
    onDirectionClick: (Direction) -> Unit = {},
    onDirectionDescriptionClick: (RichTextState, String) -> Unit,
    link: String,
    viewModel: DirectionListViewModel,
    modifier: Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val descriptionEditorState = rememberRichTextState()
    var expanded by remember { mutableStateOf(false) }
    var isDone by remember { mutableStateOf(direction.isDone) }

    descriptionEditorState
        .toggleSpanStyle(
            SpanStyle(
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                fontWeight = FontWeight.Bold
            )
        )
    Card(
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable {
                        onDirectionClick(direction)
                        Log.d(ContentValues.TAG, "directionItem usage")
                    }

            )
            {
                AsyncImage(
                    modifier = Modifier
                        .size(64.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop,
                    model = direction.imgURL,
                    contentDescription = direction.title
                )
            }
            Column(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable {
                        onDirectionClick(direction)
                        Log.d(ContentValues.TAG, "directionItem usage")
                    },
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = direction.title,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {
                    if (!expanded) {
                        descriptionEditorState.setHtml(direction.description)
                    }
                    expanded = !expanded
                }) {
                    Icon(
                        imageVector = if (expanded)
                            ImageVector.vectorResource(id = R.drawable.expand_less)
                        else
                            ImageVector.vectorResource(id = R.drawable.expand_more),
                        contentDescription = stringResource(R.string.expand_button_content_description),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
        if (expanded) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.weight(3f)) {
                    RichTextEditor(
                        state = descriptionEditorState,
                        enabled = true,
                        readOnly = false,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (descriptionEditorState.toHtml() != direction.description) {
                                    onDirectionDescriptionClick(
                                        descriptionEditorState,
                                        direction.uid
                                    )
                                }
                                keyboardController?.hide()
                            }
                        )
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    // DIRECTION STATUS CHANGE
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        IconButton(onClick = {
//                            isDone = !isDone
//                            onDirectionStatusClick(isDone, direction.uid)
//                        }) {
//                            Icon(
//                                imageVector = ImageVector.vectorResource(id = R.drawable.is_done),
//                                contentDescription = stringResource(R.string.is_done),
//                                tint = if (isDone)
//                                    Color.Green
//                                else
//                                    Color.Red
//                            )
//                        }
//                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Прогресс: " + direction.progress.toString(),
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Share(
                            viewModel = viewModel,
                            direction = direction,
                            text = link,
                            context = LocalContext.current
                        )


                    }

                }

            }

        }
    }
}

@Composable
fun Share(viewModel: DirectionListViewModel, direction: Direction, text: String, context: Context) {
    Button(onClick = {
        val deepLink = "https://sites.google.com/view/pensell" // Your deep link
        val packageName = "com.example.decemberdef"
        Firebase.dynamicLinks.shortLinkAsync {
            longLink = Uri.parse(
                "https://decemberdef.page.link/?link=" +
                        "https://decemberdef.page.link/LINKNAME?PARAMETER=${direction.uid}AndAlso${viewModel.getUser()}&apn=com.example.decemberdef&ofl=$deepLink",
            )
            androidParameters(packageName) {
            }
        }.addOnSuccessListener { shortLink ->
            // You'll need to import com.google.firebase.dynamiclinks.component1 and
            // com.google.firebase.dynamiclinks.component2
            // Short link created
            Log.d(ContentValues.TAG, shortLink.shortLink.toString())
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, shortLink.shortLink.toString())
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            ContextCompat.startActivity(context, shareIntent, null)
        }.addOnFailureListener { e ->
            Log.w(ContentValues.TAG, "warning", e)
            // Error
            // ...
        }
    }) {
        Icon(imageVector = Icons.Default.Share, contentDescription = null)
        Text("Share", modifier = Modifier.padding(start = 8.dp))
    }
}

val directions = listOf(
    Direction(
        "Математика",
        "Все что связано с ней",
        "https://th.bing.com/th/id/R.f6654d239f25b7148e37d042ce090755?rik=r%2bVlXx39tzmKFQ&riu=http%3a%2f%2fpluspng.com%2fimg-png%2fmaths-hd-png-open-2000.png&ehk=H816RouyHSLR0HPFpeQ%2bpAAGfo8Xnhxql24CZ7NwW3w%3d&risl=&pid=ImgRaw&r=0",
        false
    ),
    Direction(
        "Физика",
        "asasdasfdsfdshkjfhsdjfhdskjfhjdshfkdsfjkds",
        "https://th.bing.com/th/id/R.f6654d239f25b7148e37d042ce090755?rik=r%2bVlXx39tzmKFQ&riu=http%3a%2f%2fpluspng.com%2fimg-png%2fmaths-hd-png-open-2000.png&ehk=H816RouyHSLR0HPFpeQ%2bpAAGfo8Xnhxql24CZ7NwW3w%3d&risl=&pid=ImgRaw&r=0",
        true
    )
)