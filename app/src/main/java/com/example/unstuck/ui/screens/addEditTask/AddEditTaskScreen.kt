package com.example.unstuck.ui.screens.addEditTask

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unstuck.formats.toFormattedString
import java.time.LocalTime
import java.time.Instant
import java.time.ZoneOffset
import androidx.compose.runtime.*
import com.example.unstuck.database.Task

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTask(
    viewModel: AddEditTaskViewModel,
    isEdit: Boolean,
    onBack: () -> Unit,
    onSaveSuccess: () -> Unit,
    taskId: Int? = null,
    modifier: Modifier = Modifier
){
    val state = viewModel.addEditTaskState.collectAsStateWithLifecycle()
    val hasError = state.value.titleError != null
    val isSaved by viewModel.isSaved.collectAsStateWithLifecycle()

    LaunchedEffect(isSaved) {
        if (isSaved) {
            onSaveSuccess()
            viewModel.resetSaveStatus()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Назад"
                        )
                    }
                },
                title = {Text(if (!isEdit) "Нове завдання" else "Редагувати завдання")}
            )
        }
    ){ innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp)
        ){
            Text(
                "Назва завдання",
                color = if (hasError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
            OutlinedTextField(
                value = state.value.title,
                onValueChange = { viewModel.onEvent(AddEditTaskEvent.OnTitleChanged(it)) },
                isError = hasError,
                supportingText = {
                    if (hasError) {
                        Text(
                            text = state.value.titleError ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(24.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Column(
                    modifier = Modifier.weight(1f)
                ){
                    Text("Дата")
                    OutlinedTextField(
                        value = state.value.date.toFormattedString(),
                        onValueChange = { },
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .pointerInput(state.value.date) {
                                awaitEachGesture {
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEventDate =
                                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEventDate != null) {
                                        viewModel.onEvent(AddEditTaskEvent.OnDateTextFieldClicked)
                                    }
                                }
                            }
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ){
                    Text("Час")
                    OutlinedTextField(
                        value = state.value.time.toFormattedString(),
                        onValueChange = {},
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(24.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .pointerInput(state.value.time) {
                                awaitEachGesture {
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEventTime =
                                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEventTime != null) {
                                        viewModel.onEvent(AddEditTaskEvent.OnTimeTextFieldClicked)
                                    }
                                }
                            }
                    )
                }
            }
            Text("Нотатки")
            OutlinedTextField(
                value = state.value.notes,
                onValueChange = { viewModel.onEvent(AddEditTaskEvent.OnNotesChanged(it)) },
                //state = rememberTextFieldState(initialText = "Не забудь новий лавандовий килимок для йоги. Виконай 30-хвилинний комплекс для гнучкості від Адрієн."),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(24.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Text("Нагадати мені",
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f))
                Switch(
                    checked = state.value.remind,
                    onCheckedChange = { viewModel.onEvent(AddEditTaskEvent.OnRemindClicked(it)) },
                    )
            }
            Button(
                onClick = {
                    viewModel.onEvent(AddEditTaskEvent.OnSaveTask) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ){
                Text (
                    "Зберегти зміни",
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }

    }
    //date dialog
    if(state.value.showDatePicker){
        val datePickerState = rememberDatePickerState(
            state.value.date
                .atStartOfDay(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli())

        DatePickerDialog(
            onDismissRequest = { viewModel.onEvent(AddEditTaskEvent.OnDismissDatePicker) },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let{ millis ->
                            val date = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                            viewModel.onEvent(AddEditTaskEvent.OnDateSelected(date))
                        }
                    }
                ) {
                    Text("ОК")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(AddEditTaskEvent.OnDismissDatePicker) }) {
                    Text("Скасувати")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
    //time dialog
    if (state.value.showTimePicker){
        val timePickerState = rememberTimePickerState(
            initialHour = state.value.time.hour,
            initialMinute = state.value.time.minute,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = { viewModel.onEvent(AddEditTaskEvent.OnDismissTimePicker) },
            confirmButton = {
                TextButton(onClick = {
                    val time = LocalTime.of(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                    viewModel.onEvent(AddEditTaskEvent.OnTimeSelected(time))
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.onEvent(AddEditTaskEvent.OnDismissTimePicker) }) {
                    Text("Скасувати")
                }
            },
            text = { TimePicker(state = timePickerState) }
        )
    }
}

/*@Preview(showSystemUi = true)
@Composable
fun AddTaskPreview(){
    UnstuckTheme(){
        Scaffold() { innerPadding -> AddTask(modifier = Modifier.padding(innerPadding)) }
    }
}
 */