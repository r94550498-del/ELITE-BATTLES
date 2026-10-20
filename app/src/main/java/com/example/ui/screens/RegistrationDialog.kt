package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Shield
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
import com.example.model.PlayerProfile
import com.example.model.TeamMember
import com.example.model.Tournament
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun RegistrationDialog(
    tournament: Tournament,
    currentUser: PlayerProfile,
    strings: Strings,
    onDismiss: () -> Unit,
    onSubmit: (teamName: String, captainIgn: String, captainUid: String, phone: String, members: List<TeamMember>) -> Unit
) {
    var teamName by remember { mutableStateOf(currentUser.squadRoster.teamName) }
    var captainIgn by remember { mutableStateOf(currentUser.ign) }
    var captainUid by remember { mutableStateOf(currentUser.freeFireUid) }
    var contactPhone by remember { mutableStateOf(currentUser.phoneNumber) }

    var p2Ign by remember { mutableStateOf(currentUser.squadRoster.player2.nameOrIgn) }
    var p2Uid by remember { mutableStateOf(currentUser.squadRoster.player2.uid) }

    var p3Ign by remember { mutableStateOf(currentUser.squadRoster.player3.nameOrIgn) }
    var p3Uid by remember { mutableStateOf(currentUser.squadRoster.player3.uid) }

    var p4Ign by remember { mutableStateOf(currentUser.squadRoster.player4.nameOrIgn) }
    var p4Uid by remember { mutableStateOf(currentUser.squadRoster.player4.uid) }

    var agreedFairPlay by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.90f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CrimsonRed, RoundedCornerShape(16.dp))
                .testTag("registration_dialog"),
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
                    Column {
                        Text(
                            text = strings.teamRegistration,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = "${tournament.title} (${tournament.gameMode.label})",
                            fontSize = 11.sp,
                            color = AmberGold
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_registration_button")
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextPrimary)
                    }
                }

                HorizontalDivider(color = CyberCardBorder, modifier = Modifier.padding(vertical = 10.dp))

                // Scrollable Form
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Notice banner
                    Surface(
                        color = CrimsonDark.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonLight.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Filled.Shield, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(20.dp))
                            Text(
                                text = strings.registrationNotice,
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                        }
                    }

                    // Team Name
                    OutlinedTextField(
                        value = teamName,
                        onValueChange = { teamName = it },
                        label = { Text(strings.teamName) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_team_name_input"),
                        singleLine = true,
                        colors = esportsTextFieldColors()
                    )

                    // Captain IGN
                    OutlinedTextField(
                        value = captainIgn,
                        onValueChange = { captainIgn = it },
                        label = { Text(strings.captainIgn) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_captain_ign_input"),
                        singleLine = true,
                        colors = esportsTextFieldColors()
                    )

                    // Captain UID
                    OutlinedTextField(
                        value = captainUid,
                        onValueChange = { captainUid = it },
                        label = { Text(strings.captainUid) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_captain_uid_input"),
                        singleLine = true,
                        colors = esportsTextFieldColors()
                    )

                    // WhatsApp Phone
                    OutlinedTextField(
                        value = contactPhone,
                        onValueChange = { contactPhone = it },
                        label = { Text(strings.captainPhone) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("reg_captain_phone_input"),
                        singleLine = true,
                        colors = esportsTextFieldColors()
                    )

                    // Squad Members (if Squad/Duo)
                    if (tournament.gameMode == GameMode.SQUAD || tournament.gameMode == GameMode.DUO) {
                        Text(
                            text = "Roster Players",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = AmberGold
                        )

                        // Player 2
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = p2Ign,
                                onValueChange = { p2Ign = it },
                                label = { Text("Player 2 IGN") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = esportsTextFieldColors()
                            )
                            OutlinedTextField(
                                value = p2Uid,
                                onValueChange = { p2Uid = it },
                                label = { Text("Player 2 UID") },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                colors = esportsTextFieldColors()
                            )
                        }

                        if (tournament.gameMode == GameMode.SQUAD) {
                            // Player 3
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = p3Ign,
                                    onValueChange = { p3Ign = it },
                                    label = { Text("Player 3 IGN") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = esportsTextFieldColors()
                                )
                                OutlinedTextField(
                                    value = p3Uid,
                                    onValueChange = { p3Uid = it },
                                    label = { Text("Player 3 UID") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = esportsTextFieldColors()
                                )
                            }

                            // Player 4
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = p4Ign,
                                    onValueChange = { p4Ign = it },
                                    label = { Text("Player 4 IGN") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = esportsTextFieldColors()
                                )
                                OutlinedTextField(
                                    value = p4Uid,
                                    onValueChange = { p4Uid = it },
                                    label = { Text("Player 4 UID") },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    colors = esportsTextFieldColors()
                                )
                            }
                        }
                    }

                    // Fair play agreement check
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreedFairPlay,
                            onCheckedChange = { agreedFairPlay = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = CrimsonRed,
                                checkmarkColor = Color.White
                            ),
                            modifier = Modifier.testTag("fair_play_checkbox")
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "I guarantee all players are on mobile devices without emulators or hacks.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Submit Button
                Button(
                    onClick = {
                        val members = mutableListOf<TeamMember>()
                        members.add(TeamMember(captainIgn, captainUid, "Captain"))
                        if (p2Ign.isNotBlank()) members.add(TeamMember(p2Ign, p2Uid, "Player 2"))
                        if (p3Ign.isNotBlank()) members.add(TeamMember(p3Ign, p3Uid, "Player 3"))
                        if (p4Ign.isNotBlank()) members.add(TeamMember(p4Ign, p4Uid, "Player 4"))

                        onSubmit(
                            teamName.ifBlank { "TEAM ${captainIgn}" },
                            captainIgn,
                            captainUid,
                            contactPhone,
                            members
                        )
                    },
                    enabled = teamName.isNotBlank() && captainIgn.isNotBlank() && captainUid.isNotBlank() && agreedFairPlay,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_registration_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrimsonRed,
                        disabledContainerColor = CyberSurfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = strings.submitRegistration,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun esportsTextFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = CrimsonRed,
        unfocusedBorderColor = CyberCardBorder,
        focusedLabelColor = CrimsonLight,
        unfocusedLabelColor = TextSecondary,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
        focusedContainerColor = CyberDark,
        unfocusedContainerColor = CyberDark
    )
}
