package com.example.unstuck.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.unstuck.BuildConfig
import com.example.unstuck.R
import com.example.unstuck.ui.screens.calendar.CalendarViewModel
import com.example.unstuck.ui.theme.NunitoFontFamily
import com.example.unstuck.ui.theme.UnstuckTheme
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {

    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val isLanguageMenuExpanded by viewModel.isLanguageMenuExpanded.collectAsStateWithLifecycle()

    val appVersion = BuildConfig.VERSION_NAME ?: "1.0.0"

    var rowWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current


    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.settings_title),
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
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    shape = RoundedCornerShape(32.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Box(modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { coordinates ->
                                rowWidth = with(density) { coordinates.size.width.toDp() }
                            }
                        ){
                            SettingsRow(
                                icon = Icons.Default.Language,
                                title = stringResource(id = R.string.settings_language_title),
                                subtitle = stringResource(id = R.string.settings_language_subtitle),
                                onClick = { viewModel.languageMenuExpanded() }
                            )

                            DropdownMenu(
                                expanded = isLanguageMenuExpanded,
                                onDismissRequest = { viewModel.languageMenuDismiss() },
                                offset = DpOffset(x = 0.dp, y = 0.dp),
                                modifier = Modifier
                                    .width(rowWidth)
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clip(RoundedCornerShape(16.dp))
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(id = R.string.lang_ukrainian),
                                            fontFamily = NunitoFontFamily
                                        )
                                    },
                                    onClick = {
                                        viewModel.setLocale("uk")
                                        viewModel.languageMenuDismiss()
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = stringResource(id = R.string.lang_english),
                                            fontFamily = NunitoFontFamily
                                        )
                                    },
                                    onClick = {
                                        viewModel.setLocale("en")
                                        viewModel.languageMenuDismiss()
                                    }
                                )
                            }
                        }

                        SettingsToggleRow(
                            icon = Icons.Default.DarkMode,
                            title = stringResource(id = R.string.settings_dark_mode),
                            checked = isDarkMode,
                            onCheckedChange = { viewModel.toggleDarkMode(it) }
                        )
                    }
                }
            }

            item {
                Text(
                    text = stringResource(id = R.string.settings_footer, appVersion),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = NunitoFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontFamily = NunitoFontFamily,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun SettingsToggleRow(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun SettingsScreenPreview(){
//    UnstuckTheme() {
//        Scaffold(){ innerPadding ->
//            SettingsScreen(modifier = Modifier.padding(innerPadding))
//        }
//    }
//}