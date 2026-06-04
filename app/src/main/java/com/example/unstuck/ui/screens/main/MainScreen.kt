package com.example.unstuck.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.unstuck.ui.screens.calendar.CalendarScreen
import com.example.unstuck.ui.screens.calendar.CalendarViewModel
import com.example.unstuck.ui.screens.home.HomeScreen
import com.example.unstuck.ui.screens.home.HomeViewModel
import com.example.unstuck.ui.screens.settings.SettingsScreen
import com.example.unstuck.ui.screens.settings.SettingsViewModel
import com.example.unstuck.ui.screens.statistics.StatisticsScreen
import com.example.unstuck.ui.screens.statistics.StatisticsViewModel

@Composable
fun MainScreen(
    onNavigateToAddTask: () -> Unit,
    modifier: Modifier = Modifier
){
    var currentTab by remember { mutableStateOf(MainTab.HOME) }
    val lineColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToAddTask() },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Додати"
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()

                        drawLine(
                            color = lineColor,
                            start = Offset(x = 0f, y = 0f),
                            end = Offset(x = size.width, y = 0f),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {
                MainTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = currentTab == tab,
                        onClick = { currentTab = tab },
                        icon = { Icon(imageVector = tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                MainTab.HOME -> {
                    val homeViewModel: HomeViewModel = hiltViewModel()
                    HomeScreen(
                        viewModel = homeViewModel
                    )
                }
                MainTab.CALENDAR -> {
                    val calendarViewModel: CalendarViewModel = hiltViewModel()
                    CalendarScreen(
                        viewModel = calendarViewModel
                    )
                }
                MainTab.STATS -> {
                    val statisticsViewModel: StatisticsViewModel = hiltViewModel()
                    StatisticsScreen(
                        viewModel = statisticsViewModel
                    )
                }
                MainTab.PROFILE -> {
                    val settingsViewModel: SettingsViewModel = hiltViewModel()
                    SettingsScreen(viewModel = settingsViewModel)
                }
            }
        }
    }
}