package com.example.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.GameMode
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun CreateTournamentDialog(
    strings: Strings,
    onDismiss: () -> Unit,
    onCreate: (title: String, gameMode: GameMode, map: String, prizePoints: Int, dateTime: String, slots: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var selectedMode by remember { mutableStateOf(GameMode.SQUAD) }
    var selectedMap by remember { mutableStateOf("Bermuda") }
    var prizePointsText by remember { mutableStateOf("12000") }
    var dateTimeText by remember { mutableStateOf("Tonight, 9:00 PM IST") }
    var slotsText by remember { mutableStateOf("48") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CrimsonRed, RoundedCornerShape(16.dp))
                .testTag("create_tournament_dialog"),
            color = CyberSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.createTournament,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_create_tournament_button")) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextPrimary)
                    }
                }

                HorizontalDivider(color = CyberCardBorder, modifier = Modifier.padding(vertical = 10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Tournament Title") },
                        placeholder = { Text("e.g. KARNATAKA PRO SCRIMS #13", color = TextMuted) },
                        colors = esportsTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("new_tournament_title_input")
                    )

                    Text("Game Mode", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GameMode.values().forEach { mode ->
                            val isSel = selectedMode == mode
                            Surface(
                                color = if (isSel) CrimsonRed else CyberDark,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) CrimsonLight else CyberCardBorder),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 2.dp)
                            ) {
                                TextButton(onClick = { selectedMode = mode }, contentPadding = PaddingValues(2.dp)) {
                                    Text(
                                        text = mode.name.replace("_", " "),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSel) Color.White else TextSecondary
                                    )
                                }
                            }
                        }
                    }

                    Text("Map", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Bermuda", "Purgatory", "Kalahari", "Alpine", "Nexterra").forEach { mapName ->
                            val isSel = selectedMap == mapName
                            Surface(
                                color = if (isSel) CrimsonRed else CyberDark,
                                shape = RoundedCornerShape(6.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) CrimsonLight else CyberCardBorder)
                            ) {
                                TextButton(onClick = { selectedMap = mapName }, contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)) {
                                    Text(
                                        text = mapName,
                                        fontSize = 11.sp,
                                        color = if (isSel) Color.White else TextSecondary
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = dateTimeText,
                        onValueChange = { dateTimeText = it },
                        label = { Text("Schedule (Date & Time)") },
                        colors = esportsTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = slotsText,
                        onValueChange = { slotsText = it },
                        label = { Text("Max Slots (e.g. 48 for Squad, 24 for Duo)") },
                        colors = esportsTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = prizePointsText,
                        onValueChange = { prizePointsText = it },
                        label = { Text("Prize Pool Community Points") },
                        colors = esportsTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        val slots = slotsText.toIntOrNull() ?: 48
                        val pts = prizePointsText.toIntOrNull() ?: 10000
                        onCreate(title, selectedMode, selectedMap, pts, dateTimeText, slots)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_create_tournament_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Publish Free Tournament", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
