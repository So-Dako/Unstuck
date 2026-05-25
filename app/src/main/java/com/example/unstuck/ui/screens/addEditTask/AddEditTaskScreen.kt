package com.example.unstuck.ui.screens.addEditTask

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unstuck.navigation.Screens
import com.example.unstuck.ui.theme.UnstuckTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unstuck.formats.toFormattedString
import java.time.Instant
import java.time.ZoneOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTask(
    viewModel: AddEditTaskViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
){
    val state = viewModel.addEditTaskState.collectAsStateWithLifecycle()
    val showDatePicker by remember { mutableStateOf(state.value.showDatePicker) }

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
                title = {Text("Нове завдання")}
            )
        }
    ){ innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .padding(16.dp)
        ){
            Text("Назва завдання")
            OutlinedTextField(
                value = state.value.title,
                onValueChange = { viewModel.onEvent(AddEditTaskEvent.onTitleChanged(it)) },
                //state = rememberTextFieldState(initialText = "Ранкове заняття з йоги"),
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
                            .pointerInput(state.value.date){
                                awaitEachGesture {
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if(upEvent != null){
                                        viewModel.onEvent(AddEditTaskEvent.onDateTextFieldClicked)
                                    }
                                }
                            }
                    )

                    if(state.value.showDatePicker){
                        val datePickerState = rememberDatePickerState(
                            state.value.date
                                .atStartOfDay(ZoneOffset.UTC)
                                .toInstant()
                                .toEpochMilli())

                        DatePickerDialog(
                            onDismissRequest = { viewModel.onEvent(AddEditTaskEvent.onDismissDatePicker) },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        datePickerState.selectedDateMillis?.let{ millis ->
                                            val date = Instant
                                                .ofEpochMilli(millis)
                                                .atZone(ZoneOffset.UTC)
                                                .toLocalDate()
                                            viewModel.onEvent(AddEditTaskEvent.onDateSelected(date))
                                        }
                                    }
                                ) {
                                    Text("ОК")
                                }
                            },
                            dismissButton = {
                                TextButton(onClick = { viewModel.onEvent(AddEditTaskEvent.onDismissDatePicker) }) {
                                    Text("Скасувати")
                                }
                            }
                        ) {
                            DatePicker(
                                state = datePickerState
                            )
                        }
                    }
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
                    )
                }
            }
            Text("Нотатки")
            OutlinedTextField(
                value = state.value.notes,
                onValueChange = { viewModel.onEvent(AddEditTaskEvent.onNotesChanged(it)) },
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
                    onCheckedChange = { viewModel.onEvent(AddEditTaskEvent.onRemindClicked(it)) },
                    )
            }
            Button(
                onClick = { viewModel.onEvent(AddEditTaskEvent.onSaveTask) },
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
}

/*@Preview(showSystemUi = true)
@Composable
fun AddTaskPreview(){
    UnstuckTheme(){
        Scaffold() { innerPadding -> AddTask(modifier = Modifier.padding(innerPadding)) }
    }
}
 */