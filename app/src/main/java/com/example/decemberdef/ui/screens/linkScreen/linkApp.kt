package com.example.decemberdef.ui.screens.linkScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.decemberdef.R
import com.example.decemberdef.data.Task
import com.example.decemberdef.ui.screens.listApp.components.taskItem
import com.example.decemberdef.ui.theme.DecemberDefTheme

@Composable
fun linkApp(
    taskList: List<Task>,
    parameter: String?,
    addOtherUserDirection: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = addOtherUserDirection
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.add),
                    contentDescription = stringResource(R.string.add),
                    modifier = Modifier.padding(5.dp)
                )
                Text(text = "Добавить траекторию")
            }
        }
        Row(){
            LazyColumn(contentPadding = PaddingValues(5.dp)) {
                items(taskList) {
                    taskItem(
                        linkItem = parameter != null,
                        readOnly = true,
                        item = it,
                        modifier = Modifier.padding(8.dp),
                        onTaskDescriptionClick = { _, _ -> }
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
    ),
    Task(
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
            linkApp(taskList = tasksTest, parameter = "asdasdasdasdsa") {
            }
        }
    }
}