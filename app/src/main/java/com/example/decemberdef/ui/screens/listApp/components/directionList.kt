package com.example.decemberdef.ui.screens.listApp.components

import android.content.ContentValues
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.decemberdef.R
import com.example.decemberdef.data.Direction
import com.example.decemberdef.ui.screens.listApp.DirectionListViewModel
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor

@Composable
fun directionsList(
    onDirectionClick: (String) -> Unit,
    viewModel: DirectionListViewModel,
    isDoneClick: (Boolean, String) -> Unit,
    directions: List<Direction>

) {
    var addList: MutableList<Direction> = mutableListOf()
    addList.add(
        Direction(
            title = stringResource(id = R.string.add),
            imgURL = "https://cdn-icons-png.flaticon.com/512/7666/7666164.png"
        )
    )
    Column() {
        LazyColumn(contentPadding = PaddingValues(5.dp)) {
            items(addList) {
                addDirectionItem(
                    viewModel = viewModel,
                    modifier = Modifier.padding(8.dp)
                )
            }
            items(directions.reversed()) {
                directionItem(
                    direction = it,
                    onDirectionClick = onDirectionClick,
                    isDoneClick = isDoneClick,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun addDirectionItem(
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
                contentDescription = stringResource(id = R.string.add),
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}

@Composable
fun directionItem(
    direction: Direction,
    onDirectionClick: (String) -> Unit = {},
    isDoneClick: (Boolean, String) -> Unit,
    modifier: Modifier
) {
    val taskEditorState = rememberRichTextState()
    var expanded by remember { mutableStateOf(false) }
    var isDone by remember { mutableStateOf(direction.isDone) }

    taskEditorState
        .toggleSpanStyle(
            SpanStyle(
                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                fontWeight = FontWeight.Bold
            )
        )
    taskEditorState.setHtml(direction.description)
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
                        onDirectionClick(direction.uid)
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
                        onDirectionClick(direction.uid)
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
                        state = taskEditorState,
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth()
                    )
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
                        IconButton(onClick = {
                            isDone = !isDone
                            isDoneClick(isDone, direction.uid) }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.is_done),
                                contentDescription = stringResource(R.string.is_done),
                                tint = if (isDone)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Прогресс: " + direction.progress.toString(),
                            modifier = Modifier.padding(5.dp)
                        )
                    }

                }

            }

        }
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