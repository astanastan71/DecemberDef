package com.example.decemberdef.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.decemberdef.ui.navigation.BottomNav.BottomNavItem
import com.example.decemberdef.ui.theme.DecemberDefTheme
import com.example.decemberdef.ui.theme.roboto

@Composable
fun navigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.DirectionChooser,
        BottomNavItem.TaskBuilder
    )
    NavigationBar() {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            val selected = currentDestination == screen.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.background
                ),
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = screen.icon),
                            contentDescription = screen.title
                        )
                        if (selected) {
                            Text(
                                text = screen.title,
                                style = TextStyle(
                                    textAlign = TextAlign.Center,
                                    fontFamily = roboto,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                )
                            )
                        }
                    }
                }
            )

        }


    }


}

@Composable
@Preview
fun navigationBarPreview() {
    DecemberDefTheme {
        Surface {
            val navController = rememberNavController()
            navigationBar(navController)
        }
    }
}