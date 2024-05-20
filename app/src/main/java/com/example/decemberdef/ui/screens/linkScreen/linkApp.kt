package com.example.decemberdef.ui.screens.linkScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Monitor
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.decemberdef.R
import com.example.decemberdef.data.Direction
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.screens.listApp.components.taskItem
import com.example.decemberdef.ui.theme.DecemberDefTheme

@Composable
fun linkApp(
    direction: Direction,
    taskList: List<Task>,
    parameter: String?,
    addOtherUserDirection: () -> Unit,
    monitorDirection: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = "Название: " + direction.title
                )
                Text(
                    text = "Кол-во задач: " + direction.count
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                IconButton(
                    onClick = addOtherUserDirection
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.add),
                        contentDescription = stringResource(R.string.add),
                        modifier = Modifier.padding(5.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(
                    onClick = monitorDirection
                ) {
                    Icon(
                        imageVector = Icons.Default.Monitor,
                        contentDescription = stringResource(R.string.monitor),
                        modifier = Modifier.padding(5.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        Row() {
            val taskState = rememberLazyListState()
            LazyColumn(contentPadding = PaddingValues(5.dp), state = taskState) {
                itemsIndexed(taskList) { index, task ->
                    taskItem(
                        linkItem = parameter != null,
                        readOnly = true,
                        item = task,
                        modifier = Modifier.padding(8.dp),
                        onTaskDescriptionClick = { _, _ -> },
                        index = index,
                        taskState = taskState
                    )
                }
            }

        }
    }

}

val tasksTest = listOf(
    Task(
        "Математика",
        "Все что связано с ней",
        "https://th.bing.com/th/id/R.f6654d239f25b7148e37d042ce090755?rik=r%2bVlXx39tzmKFQ&riu=http%3a%2f%2fpluspng.com%2fimg-png%2fmaths-hd-png-open-2000.png&ehk=H816RouyHSLR0HPFpeQ%2bpAAGfo8Xnhxql24CZ7NwW3w%3d&risl=&pid=ImgRaw&r=0",
        false
    ), Task(
        "Физика",
        "asasdasfdsfdshkjfhsdjfhdskjfhjdshfkdsfjkds",
        "https://th.bing.com/th/id/R.f6654d239f25b7148e37d042ce090755?rik=r%2bVlXx39tzmKFQ&riu=http%3a%2f%2fpluspng.com%2fimg-png%2fmaths-hd-png-open-2000.png&ehk=H816RouyHSLR0HPFpeQ%2bpAAGfo8Xnhxql24CZ7NwW3w%3d&risl=&pid=ImgRaw&r=0",
        true
    )
)

@Preview
@Composable
fun linkAppPreview() {
    DecemberDefTheme {
        Surface() {
            linkApp(
                direction = Direction(),
                taskList = tasksTest,
                parameter = "asdasdasdasdsa",
                {},
                {})
        }
    }
}