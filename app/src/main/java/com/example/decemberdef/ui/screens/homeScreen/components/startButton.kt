package com.example.decemberdef.ui.screens.homeScreen.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.decemberdef.ui.theme.md_theme_dark_background

@Composable
fun startButton(
    onClickStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedColor by rememberInfiniteTransition().animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = MaterialTheme.colorScheme.secondary,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse)
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClickStart,
            colors = ButtonDefaults.buttonColors(md_theme_dark_background)
        ) {
            Text(
                text = "Начать работу",
                style = MaterialTheme.typography.titleLarge,
                color = animatedColor
            )
        }


    }

}

@Composable
@Preview
fun previewStartButton() {
    startButton(
        onClickStart = {},
        modifier = Modifier.fillMaxWidth()
    )
}