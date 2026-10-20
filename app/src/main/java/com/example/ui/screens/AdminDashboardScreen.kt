package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.GameMode
import com.example.model.Tournament
import com.example.model.TournamentStatus
import com.example.model.UserRole
import com.example.ui.components.CyberCard
import com.example.ui.components.StatusBadge
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    tournaments: List<Tournament>,
    userRole: UserRole,
    strings: Strings,
    onOpenCreateTournament: () -> Unit,
    onUpdateRoomCredentials: (tournamentId: String, roomId: String, password: String, releaseNow: Boolean) -> Unit,
    onUpdateStatus: (tournamentId: String, status: TournamentStatus) -> Unit,
    onToggleRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTournamentForRoomModal by remember { mutableStateOf<Tournament?>(null) }

    if (userRole != UserRole.ADMIN) {
        // Access gate
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            CyberCard(
                borderColor = AmberGold.copy(alpha = 0.5f),
                backgroundColor = CyberDark
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AdminPanelSettings,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(56.dp)
                    )
                    Text(
                        text = "Admin Access Required",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = "Tournament organizers can create scrims, publish custom room IDs and passwords, and record match results. You are currently in Player mode.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Button(
                        onClick = onToggleRole,
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("enable_admin_access_button")
                    ) {
                        Icon(Icons.Filled.LockOpen, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Enable Community Admin Mode", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("admin_dashboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Admin Banner
        item {
            CyberCard(
                borderColor = CrimsonRed,
                backgroundColor = Color(0xFF1F0A0E)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = strings.adminDashboard,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = TextPrimary
                        )
                        Text(
                            text = "Manage tournaments, rooms, credentials and schedules",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }

                    Button(
                        onClick = onOpenCreateTournament,
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("create_tournament_button")
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("New Scrim", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Section header
        item {
            Text(
                text = strings.manageTournaments,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        // Tournaments list
        items(tournaments, key = { it.id }) { t ->
            CyberCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = t.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    StatusBadge(status = t.status)
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "${t.gameMode.label} • Map: ${t.map} • Slots: ${t.filledSlots}/${t.totalSlots}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Current Room State
                Surface(
                    color = CyberDark,
                    shape = RoundedCornerShape(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Room Status: ${if (t.roomCredentials.isReleased) "PUBLISHED (ONLINE)" else "LOCKED (OFFLINE)"}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (t.roomCredentials.isReleased) LiveNeonGreen else AmberGold
                            )
                            if (t.roomCredentials.roomId.isNotBlank()) {
                                Text(
                                    text = "ID: ${t.roomCredentials.roomId} | Pass: ${t.roomCredentials.password}",
                                    fontSize = 11.sp,
                                    color = TextPrimary
                                )
                            }
                        }

                        Button(
                            onClick = { selectedTournamentForRoomModal = t },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (t.roomCredentials.isReleased) CyberSurfaceVariant else LiveNeonGreen,
                                contentColor = if (t.roomCredentials.isReleased) TextPrimary else Color.Black
                            ),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.testTag("publish_room_btn_${t.id}")
                        ) {
                            Text(
                                text = if (t.roomCredentials.isReleased) "Edit Room" else "Publish Room",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Status Quick Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (t.status != TournamentStatus.LIVE) {
                        OutlinedButton(
                            onClick = { onUpdateStatus(t.id, TournamentStatus.LIVE) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LiveNeonGreen)
                        ) {
                            Text("Set Live", fontSize = 11.sp, color = LiveNeonGreen)
                        }
                    }

                    if (t.status != TournamentStatus.COMPLETED) {
                        OutlinedButton(
                            onClick = { onUpdateStatus(t.id, TournamentStatus.COMPLETED) },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Gray)
                        ) {
                            Text("Complete", fontSize = 11.sp, color = Color.LightGray)
                        }
                    }
                }
            }
        }
    }

    // Modal to Publish / Edit Room ID & Password
    selectedTournamentForRoomModal?.let { target ->
        var roomIdInput by remember { mutableStateOf(target.roomCredentials.roomId) }
        var passwordInput by remember { mutableStateOf(target.roomCredentials.password) }
        var broadcastAlert by remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = { selectedTournamentForRoomModal = null },
            title = {
                Text("Broadcast Room Credentials", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = target.title,
                        fontSize = 12.sp,
                        color = AmberGold
                    )
                    OutlinedTextField(
                        value = roomIdInput,
                        onValueChange = { roomIdInput = it },
                        label = { Text("Custom Room ID") },
                        colors = esportsTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("admin_room_id_input")
                    )
                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Custom Room Password") },
                        colors = esportsTextFieldColors(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("admin_room_pass_input")
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = broadcastAlert,
                            onCheckedChange = { broadcastAlert = it },
                            colors = CheckboxDefaults.colors(checkedColor = LiveNeonGreen)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Release immediately & notify registered players",
                            fontSize = 11.sp,
                            color = TextPrimary
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateRoomCredentials(target.id, roomIdInput, passwordInput, broadcastAlert)
                        selectedTournamentForRoomModal = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LiveNeonGreen, contentColor = Color.Black),
                    modifier = Modifier.testTag("admin_confirm_publish_room")
                ) {
                    Text("Broadcast Room", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedTournamentForRoomModal = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CyberSurface
        )
    }
}
