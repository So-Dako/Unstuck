package com.example.unstuck.ui.screens.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unstuck.ui.screens.settings.SettingsViewModel
import com.example.unstuck.ui.theme.DarkErrorRed
import com.example.unstuck.ui.theme.DarkSuccessGreen
import com.example.unstuck.ui.theme.LightErrorRed
import com.example.unstuck.ui.theme.LightSuccessGreen
import com.example.unstuck.ui.theme.NunitoFontFamily
import com.example.unstuck.ui.theme.PlayfairFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    viewModel: StatisticsViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    val selectedPeriodIndex by viewModel.selectedPeriod.collectAsStateWithLifecycle()
    val stats by viewModel.statisticsState.collectAsStateWithLifecycle()

    val totalTasksCount = stats.completedCount + stats.uncompletedCount
    val completedTasksCount = stats.completedCount

    val progress =
        if (totalTasksCount > 0) completedTasksCount.toFloat() / totalTasksCount else 0f

    val percentage = (progress * 100).toInt()

    val periods = listOf("Тиждень", "Місяць", "Рік")

    val isDark by settingsViewModel.isDarkMode.collectAsStateWithLifecycle()

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = "Статистика",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            windowInsets = WindowInsets(0, 0, 0, 0)
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    periods.forEachIndexed { index, period ->
                        val isSelected = selectedPeriodIndex == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) // Або твій рожевий колір з макета
                                    else Color.Transparent
                                )
                                .clickable { viewModel.changePeriod(index) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = period,
                                fontFamily = NunitoFontFamily,
                                fontSize = 16.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatisticCard(
                        title = "Виконано",
                        value = stats.completedCount.toString(),
                        trendPercent = stats.completedTrend,
                        trendCount = stats.completedCountTrend,
                        periodIndex = selectedPeriodIndex,
                        isInverseLogic = false,
                        isDark = isDark,
                        modifier = Modifier.weight(1f)
                    )
                    StatisticCard(
                        title = "Не виконано",
                        value = stats.uncompletedCount.toString(),
                        trendPercent = stats.uncompletedTrend,
                        trendCount = stats.completedCountTrend,
                        periodIndex = selectedPeriodIndex,
                        isInverseLogic = true,
                        isDark = isDark,
                        modifier = Modifier.weight(1f)
                    )
                }
                Text(
                    text = "Виконано $completedTasksCount завдань з $totalTasksCount",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                CircularProgress(progress = progress)
            }
        }
    }
}

@Composable
fun StatisticCard(
    title: String,
    value: String,
    trendPercent: Int,
    trendCount: Int,
    periodIndex: Int,
    isInverseLogic: Boolean,
    isDark: Boolean,
    modifier: Modifier = Modifier
) {
    val isGoodNews = if (isInverseLogic) {
        trendPercent < 0
    } else {
        trendPercent > 0
    }

    val trendIcon = if (trendPercent >= 0) {
        Icons.AutoMirrored.Filled.TrendingUp
    } else {
        Icons.AutoMirrored.Filled.TrendingDown
    }

    val periodWord = when(periodIndex) {
        0 -> "тиж"
        1 -> "міс"
        else -> "рік"
    }

    val sign = if (trendPercent > 0) "+" else if (trendPercent < 0) "-" else ""
    val absCount = kotlin.math.abs(trendCount)
    val absPercent = kotlin.math.abs(trendPercent)
    val trendText = "$sign$absCount ($absPercent% / $periodWord)"

    val trendColor = if (trendPercent == 0) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else if (isGoodNews) {
        if (isDark) DarkSuccessGreen else LightSuccessGreen
    } else {
        if (isDark) DarkErrorRed else LightErrorRed
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 14.sp
            )

            Text(
                text = value,
                fontFamily = PlayfairFontFamily,
                fontSize = 40.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (trendPercent != 0) {
                    Icon(
                        imageVector = trendIcon,
                        contentDescription = null,
                        tint = trendColor,
                        modifier = Modifier
                            .size(14.dp)
                            .padding(top = 2.dp)
                    )
                }
                Text(
                    text = trendText,
                    color = trendColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun CircularProgress(
    progress: Float,
    modifier: Modifier = Modifier
){
    val percentage = (progress * 100).toInt()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.primary.copy(0.2f))
            .padding(20.dp),
        contentAlignment = Alignment.Center,
    ){
        CircularProgressIndicator(
            progress = 1f,
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface,
            strokeWidth = 24.dp,
            strokeCap = StrokeCap.Round
        )
        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 24.dp,
            strokeCap = StrokeCap.Round
        )
        Text(
            text = "$percentage%",
            fontSize = 32.sp
        )
    }
}
