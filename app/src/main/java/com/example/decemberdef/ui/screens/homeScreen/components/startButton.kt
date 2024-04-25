package com.example.decemberdef.ui.screens.homeScreen.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.decemberdef.ui.theme.roboto

@Composable
fun startButton(
    onClickStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedColor by rememberInfiniteTransition().animateColor(
        initialValue = MaterialTheme.colorScheme.onPrimary,
        targetValue = MaterialTheme.colorScheme.onSecondary,
        animationSpec = infiniteRepeatable(tween(1000), RepeatMode.Reverse)
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = onClickStart,
            colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Начать работу",
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = roboto,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    ),
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