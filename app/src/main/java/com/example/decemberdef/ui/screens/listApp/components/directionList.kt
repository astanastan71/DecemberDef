package com.example.decemberdef.ui.screens.listApp.components

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlignHorizontalCenter
import androidx.compose.material.icons.filled.AlignHorizontalLeft
import androidx.compose.material.icons.filled.AlignHorizontalRight
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatUnderlined
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.launch

@Composable
fun directionsList(
    onDirectionClick: (Direction) -> Unit,
    viewModel: DirectionListViewModel,
    setSharedStatus: (Boolean, String) -> Unit,
    onDescriptionClick: (RichTextState, String) -> Unit,
    directions: List<Direction>,
    onTitleChange: (String, String) -> Unit,
    onDirectionDelete: (String) -> Unit,
    link: String
) {
    val listState = rememberLazyListState()
    var localDirections = directions
    var addList: MutableList<Direction> = mutableListOf()
    addList.add(
        Direction(
            title = stringResource(id = R.string.add),
            imgURL = "https://cdn-icons-png.flaticon.com/512/7666/7666164.png"
        )
    )
    Column(
        modifier = Modifier
    ) {
        LazyColumn(contentPadding = PaddingValues(top = 5.dp), state = listState) {
            items(addList) {
                addDirectionItem(
                    direction = it,
                    viewModel = viewModel,
                    modifier = Modifier.padding(8.dp)
                )
            }
            itemsIndexed(localDirections.reversed()) { index, direction ->
                directionItem(
                    direction = direction,
                    onDirectionClick = onDirectionClick,
                    onDirectionDescriptionClick = onDescriptionClick,
                    link = link,
                    viewModel = viewModel,
                    onTitleChange = onTitleChange,
                    setSharedStatus = setSharedStatus,
                    onDirectionDelete = onDirectionDelete,
                    index = index,
                    listState = listState,
                    modifier = Modifier
                        .padding(8.dp)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun directionItem(
    direction: Direction,
    onDirectionClick: (Direction) -> Unit = {},
    onDirectionDescriptionClick: (RichTextState, String) -> Unit,
    link: String,
    viewModel: DirectionListViewModel,
    onTitleChange: (String, String) -> Unit,
    setSharedStatus: (Boolean, String) -> Unit,
    onDirectionDelete: (String) -> Unit,
    listState: LazyListState,
    index: Int,
    modifier: Modifier
) {
    var paragraphStyle by remember { mutableStateOf(ParagraphStyle(textAlign = TextAlign.Start)) }
    var boldSelected by rememberSaveable { mutableStateOf(false) }
    var underlinedSelected by rememberSaveable { mutableStateOf(false) }


    val coroutineScope = rememberCoroutineScope()
    var text by remember { mutableStateOf(direction.title) }
    val openDialog = remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val descriptionEditorState = rememberRichTextState()
    var expanded by remember { mutableStateOf(false) }
    var shared by remember { mutableStateOf(direction.shared) }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    descriptionEditorState.toggleParagraphStyle(
        paragraphStyle
    )
    if (openDialog.value) {
        titleChangeDialog(
            onConfirmation = { text ->
                onTitleChange(text, direction.uid)
                openDialog.value = false
            },
            onDismissRequest = {
                openDialog.value = false
            },
            text = text,
            onValueChange = {
                text = it
            }
        )
    }
    Card(
        modifier = modifier.clickable {
            onDirectionClick(direction)
            Log.d(ContentValues.TAG, "directionItem usage")

        }
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(4.dp)
                    .clickable {
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
                        openDialog.value = true
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
                    if (descriptionEditorState.toHtml() != direction.description) {
                        onDirectionDescriptionClick(
                            descriptionEditorState,
                            direction.uid
                        )
                    }
                    keyboardController?.hide()
                    coroutineScope.launch {
                        listState.scrollToItem(index)
                    }
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
                    Row(modifier = Modifier.fillMaxSize()) {
                        IconButton(onClick = {
                            when (paragraphStyle) {
                                ParagraphStyle(textAlign = TextAlign.Start) -> {
                                    paragraphStyle = ParagraphStyle(textAlign = TextAlign.Center)
                                }

                                ParagraphStyle(textAlign = TextAlign.Center) -> {
                                    paragraphStyle = ParagraphStyle(textAlign = TextAlign.End)
                                }

                                ParagraphStyle(textAlign = TextAlign.End) -> {
                                    paragraphStyle = ParagraphStyle(textAlign = TextAlign.Start)
                                }
                            }
                        }
                        ) {
                            Icon(
                                imageVector =
                                when (paragraphStyle) {
                                    ParagraphStyle(textAlign = TextAlign.Start) -> Icons.Default.AlignHorizontalLeft
                                    ParagraphStyle(textAlign = TextAlign.Center) ->
                                        Icons.Default.AlignHorizontalCenter
                                    ParagraphStyle(textAlign = TextAlign.End) ->
                                        Icons.Default.AlignHorizontalRight
                                    else -> {
                                        Icons.Default.Face
                                    }
                                },
                                contentDescription = stringResource(R.string.Bold),
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                        IconButton(onClick = {
                            descriptionEditorState.toggleSpanStyle(
                                SpanStyle(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            boldSelected = !boldSelected
                        }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatBold,
                                contentDescription = stringResource(R.string.Bold),
                                tint = if (!boldSelected) {
                                    Color.Gray
                                } else {
                                    Color.Black
                                },
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                        IconButton(onClick = {
                            descriptionEditorState.toggleSpanStyle(
                                SpanStyle(
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                            underlinedSelected = !underlinedSelected
                        }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatUnderlined,
                                contentDescription = stringResource(R.string.underlined),
                                tint = if (!underlinedSelected) {
                                    Color.Gray
                                } else {
                                    Color.Black
                                },
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxSize()) {
                        RichTextEditor(
                            state = descriptionEditorState,
                            enabled = true,
                            readOnly = false,
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth()
                                .onFocusEvent {
                                    if (it.isFocused) {
                                        coroutineScope.launch {
                                            bringIntoViewRequester.bringIntoView()
                                        }
                                    }
                                },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                autoCorrect = false,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Default
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                }
                            )
                        )
                    }

                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(5.dp),
                    horizontalAlignment = Alignment.End
                ) {
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = {
                            shared = !shared
                            setSharedStatus(shared, direction.uid)
                        }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.share_lock),
                                contentDescription = stringResource(R.string.share_lock),
                                tint = if (shared) {
                                    Color.Gray
                                } else {
                                    Color.Black
                                },
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = {
                            onDirectionDelete(direction.uid)
                        }
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.delete),
                                contentDescription = stringResource(R.string.delete),
                                modifier = Modifier.padding(5.dp)
                            )
                        }

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
    }
}