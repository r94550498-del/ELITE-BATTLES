package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlayerProfile
import com.example.model.SquadRoster
import com.example.model.TeamMember
import com.example.model.UserRole
import com.example.ui.components.CyberCard
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    profile: PlayerProfile,
    strings: Strings,
    currentLanguage: AppLanguage,
    onLanguageSelect: (AppLanguage) -> Unit,
    onToggleRole: () -> Unit,
    onOpenAuthDialog: () -> Unit,
    onSaveProfile: (name: String, ign: String, uid: String, phone: String) -> Unit,
    onSaveRoster: (SquadRoster) -> Unit,
    onOpenHelp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showEditRosterDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("profile_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Gamer Card
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF2E090F), CyberSurface, CyberDark)
                        )
                    )
                    .border(1.dp, CrimsonRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(CrimsonRed)
                                    .border(2.dp, AmberGold, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.SportsEsports,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(30.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = profile.ign,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "FF UID: ${profile.freeFireUid}",
                                    fontSize = 12.sp,
                                    color = AmberGold,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = profile.name,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        IconButton(
                            onClick = { showEditProfileDialog = true },
                            modifier = Modifier.testTag("edit_profile_button")
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit Profile", tint = CrimsonLight)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tier Pill
                    Surface(
                        color = CrimsonRed.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonLight)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Filled.Shield, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                            Text(
                                text = "${strings.tier}: ${profile.tier}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Stats Matrix
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatPill(title = strings.matchesPlayed, value = "${profile.matchesPlayed}", color = TextPrimary, modifier = Modifier.weight(1f))
                StatPill(title = strings.totalWins, value = "${profile.wins}", color = AmberGold, modifier = Modifier.weight(1f))
                StatPill(title = strings.totalKills, value = "${profile.kills}", color = CrimsonLight, modifier = Modifier.weight(1f))
                StatPill(title = strings.kdRatio, value = "${profile.kdRatio}", color = LiveNeonGreen, modifier = Modifier.weight(1f))
            }
        }

        // Squad Team Roster
        item {
            CyberCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Groups, contentDescription = null, tint = CrimsonLight)
                        Column {
                            Text(strings.squadTeam, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text(profile.squadRoster.teamName, fontSize = 12.sp, color = AmberGold, fontWeight = FontWeight.Bold)
                        }
                    }

                    TextButton(
                        onClick = { showEditRosterDialog = true },
                        modifier = Modifier.testTag("manage_roster_button")
                    ) {
                        Text(strings.editRoster, fontSize = 12.sp, color = CrimsonLight)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 4 Members list
                val roster = profile.squadRoster
                RosterMemberRow(label = "Captain", ign = roster.captainIgn, uid = roster.captainUid, role = "Leader", isCaptain = true)
                Spacer(modifier = Modifier.height(6.dp))
                RosterMemberRow(label = "Player 2", ign = roster.player2.nameOrIgn, uid = roster.player2.uid, role = roster.player2.role)
                Spacer(modifier = Modifier.height(6.dp))
                RosterMemberRow(label = "Player 3", ign = roster.player3.nameOrIgn, uid = roster.player3.uid, role = roster.player3.role)
                Spacer(modifier = Modifier.height(6.dp))
                RosterMemberRow(label = "Player 4", ign = roster.player4.nameOrIgn, uid = roster.player4.uid, role = roster.player4.role)
            }
        }

        // Authentication & Role Section
        item {
            CyberCard {
                Text(
                    text = "Account & Authentication",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Role badge and switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(strings.roleBadge, fontSize = 11.sp, color = TextMuted)
                        Text(
                            text = if (profile.role == UserRole.ADMIN) strings.adminRole else strings.playerRole,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (profile.role == UserRole.ADMIN) AmberGold else LiveNeonGreen
                        )
                    }

                    Button(
                        onClick = onToggleRole,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (profile.role == UserRole.ADMIN) CyberSurfaceVariant else CrimsonRed
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.testTag("switch_role_button")
                    ) {
                        Text(
                            text = if (profile.role == UserRole.ADMIN) "Exit Admin" else strings.switchRole,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onOpenAuthDialog,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_auth_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberDark),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Filled.Lock, contentDescription = null, modifier = Modifier.size(16.dp), tint = AmberGold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Google Sign-In / Phone OTP Setup", color = TextPrimary, fontSize = 13.sp)
                }
            }
        }

        // Language & Community Settings
        item {
            CyberCard {
                Text(strings.selectLanguage, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LanguageOptionCard(
                        title = "English",
                        isSelected = currentLanguage == AppLanguage.ENGLISH,
                        onClick = { onLanguageSelect(AppLanguage.ENGLISH) },
                        modifier = Modifier.weight(1f)
                    )
                    LanguageOptionCard(
                        title = "ಕನ್ನಡ (Kannada)",
                        isSelected = currentLanguage == AppLanguage.KANNADA,
                        onClick = { onLanguageSelect(AppLanguage.KANNADA) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onOpenHelp,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberSurfaceVariant),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Outlined.HelpCenter, contentDescription = null, tint = TextPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(strings.helpAndRules, color = TextPrimary, fontSize = 13.sp)
                }
            }
        }
    }

    // Edit Profile Modal
    if (showEditProfileDialog) {
        var nameInput by remember { mutableStateOf(profile.name) }
        var ignInput by remember { mutableStateOf(profile.ign) }
        var uidInput by remember { mutableStateOf(profile.freeFireUid) }
        var phoneInput by remember { mutableStateOf(profile.phoneNumber) }

        AlertDialog(
            onDismissRequest = { showEditProfileDialog = false },
            title = { Text("Edit Gamer Profile", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = ignInput,
                        onValueChange = { ignInput = it },
                        label = { Text("Free Fire IGN") },
                        colors = esportsTextFieldColors(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = uidInput,
                        onValueChange = { uidInput = it },
                        label = { Text("Free Fire UID") },
                        colors = esportsTextFieldColors(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Player Real Name") },
                        colors = esportsTextFieldColors(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("Contact Phone") },
                        colors = esportsTextFieldColors(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSaveProfile(nameInput, ignInput, uidInput, phoneInput)
                        showEditProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed)
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditProfileDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CyberSurface
        )
    }

    // Edit Roster Modal
    if (showEditRosterDialog) {
        var tName by remember { mutableStateOf(profile.squadRoster.teamName) }
        var cIgn by remember { mutableStateOf(profile.squadRoster.captainIgn) }
        var cUid by remember { mutableStateOf(profile.squadRoster.captainUid) }
        var p2Name by remember { mutableStateOf(profile.squadRoster.player2.nameOrIgn) }
        var p2UidVal by remember { mutableStateOf(profile.squadRoster.player2.uid) }
        var p3Name by remember { mutableStateOf(profile.squadRoster.player3.nameOrIgn) }
        var p3UidVal by remember { mutableStateOf(profile.squadRoster.player3.uid) }
        var p4Name by remember { mutableStateOf(profile.squadRoster.player4.nameOrIgn) }
        var p4UidVal by remember { mutableStateOf(profile.squadRoster.player4.uid) }

        AlertDialog(
            onDismissRequest = { showEditRosterDialog = false },
            title = { Text("Manage Squad Roster", color = TextPrimary) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = tName,
                        onValueChange = { tName = it },
                        label = { Text("Team Name") },
                        colors = esportsTextFieldColors(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = p2Name,
                        onValueChange = { p2Name = it },
                        label = { Text("Player 2 IGN") },
                        colors = esportsTextFieldColors(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = p3Name,
                        onValueChange = { p3Name = it },
                        label = { Text("Player 3 IGN") },
                        colors = esportsTextFieldColors(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = p4Name,
                        onValueChange = { p4Name = it },
                        label = { Text("Player 4 IGN") },
                        colors = esportsTextFieldColors(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newRoster = SquadRoster(
                            teamName = tName,
                            captainIgn = cIgn,
                            captainUid = cUid,
                            player2 = TeamMember(p2Name, p2UidVal, "Assaulter"),
                            player3 = TeamMember(p3Name, p3UidVal, "Rusher"),
                            player4 = TeamMember(p4Name, p4UidVal, "Support")
                        )
                        onSaveRoster(newRoster)
                        showEditRosterDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed)
                ) {
                    Text("Save Roster")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditRosterDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = CyberSurface
        )
    }
}

@Composable
private fun StatPill(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CyberSurface,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontSize = 16.sp, fontWeight = FontWeight.Black, color = color)
            Text(title, fontSize = 10.sp, color = TextMuted, maxLines = 1)
        }
    }
}

@Composable
private fun RosterMemberRow(
    label: String,
    ign: String,
    uid: String,
    role: String,
    isCaptain: Boolean = false
) {
    Surface(
        color = CyberDark,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    color = if (isCaptain) CrimsonRed else CyberSurfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (isCaptain) "CPT" else "P",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                    )
                }
                Column {
                    Text(text = ign, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    Text(text = "UID: $uid", fontSize = 10.sp, color = TextSecondary)
                }
            }

            Text(text = role, fontSize = 11.sp, color = AmberGold)
        }
    }
}

@Composable
private fun LanguageOptionCard(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isSelected) CrimsonRed.copy(alpha = 0.2f) else CyberDark,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) CrimsonLight else CyberCardBorder
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = CrimsonRed,
                    unselectedColor = TextMuted
                )
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) TextPrimary else TextSecondary
            )
        }
    }
}
