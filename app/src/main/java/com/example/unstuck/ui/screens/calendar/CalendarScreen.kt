package com.example.unstuck.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unstuck.ui.theme.NunitoFontFamily
import com.example.unstuck.ui.theme.PlayfairFontFamily
import com.example.unstuck.ui.theme.UnstuckTheme
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unstuck.ui.screens.elements.TaskCard
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    viewModel: CalendarViewModel,
    modifier: Modifier = Modifier
){
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val tasks by viewModel.taskState.collectAsStateWithLifecycle()

    val dynamicDateFormatter = remember { DateTimeFormatter.ofPattern("d MMMM", Locale("uk")) }
    val sectionTitle = remember(selectedDate) { "Плани на ${selectedDate.format(dynamicDateFormatter)}" }

    val datesWithTasks by viewModel.datesWithTasksState.collectAsStateWithLifecycle()

       Column(modifier = modifier.fillMaxSize()){
           TopAppBar(
               title = {
                   Text(
                       text = "Календар",
                       fontSize = 28.sp,
                       fontWeight = FontWeight.Medium,
                       modifier = Modifier.fillMaxWidth(),
                       textAlign = TextAlign.Center
                   )
               },
               windowInsets = WindowInsets(0, 0, 0, 0)
           )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    CustomCalendar(
                        selectedDate = selectedDate,
                        onDateSelected = { date ->
                            viewModel.changeSelectedDate(date)
                        },
                        datesWithTasks = datesWithTasks
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = sectionTitle,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (tasks.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Немає планів на цей день",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    items(items = tasks, key = { it.taskId }) { task ->
                        TaskCard(
                            task = task,
                            onCheckedChange = { viewModel.toggleTaskStatus(task) }
                        )
                    }
                }
            }
        }
    }

@Composable
fun CustomCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    datesWithTasks: List<LocalDate> = emptyList()
){
    var currentMonth by remember { mutableStateOf(YearMonth.from(selectedDate)) }

    val localeUk = remember { Locale("uk") }
    val monthName = currentMonth.month.getDisplayName(TextStyle.FULL_STANDALONE, localeUk)
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeUk) else it.toString() }
    val yearTitle = currentMonth.year

    val daysInMonth = currentMonth.lengthOfMonth()
    val firstDayOfWeek = currentMonth.atDay(1).dayOfWeek.value

    val emptySlotsBefore = firstDayOfWeek - 1

    val daysOfWeekHeaders = listOf("ПН", "ВТ", "СР", "ЧТ", "ПТ", "СБ", "НД")

    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        ),
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Попередній місяць",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                }

                Text(
                    text = "$monthName $yearTitle",
                    fontFamily = PlayfairFontFamily,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Наступний місяць",
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                daysOfWeekHeaders.forEach { header ->
                    Text(
                        text = header,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontFamily = NunitoFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val totalGridItems = emptySlotsBefore + daysInMonth

            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                userScrollEnabled = false
            ) {
                items(emptySlotsBefore) {
                    Box(modifier = Modifier.aspectRatio(1f))
                }

                items(items = (1..daysInMonth).toList()) { day ->
                    val dateAsLocalDate = currentMonth.atDay(day)
                    val isSelected = dateAsLocalDate == selectedDate
                    val hasTask = datesWithTasks.contains(dateAsLocalDate)

                    Column(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onDateSelected(dateAsLocalDate)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = day.toString(),
                            fontFamily = NunitoFontFamily,
                            fontSize = 15.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )

                        if (hasTask && !isSelected) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                            )
                        } else if (hasTask && isSelected) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .background(Color.White, CircleShape)
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun CalendarScreenPreview(){
//    UnstuckTheme() {
//        CustomCalendar()
////        Scaffold() { innerPadding ->
////            CalendarScreen(modifier = Modifier.padding(innerPadding))
////        }
//    }
//}