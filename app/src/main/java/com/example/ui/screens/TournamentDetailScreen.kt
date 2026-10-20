package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Tournament
import com.example.model.TournamentStatus
import com.example.ui.components.CyberCard
import com.example.ui.components.RoomCredentialsCard
import com.example.ui.components.StatusBadge
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TournamentDetailScreen(
    tournament: Tournament,
    isRegistered: Boolean,
    strings: Strings,
    onBack: () -> Unit,
    onRegister: () -> Unit,
    onCopySuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val registeredUserTeam = tournament.registeredTeams.find { it.captainIgn.contains("KT_") || it.teamName.contains("KARNATAKA TIGERS") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        strings.tournamentDetails,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CyberDark)
            )
        },
        bottomBar = {
            Surface(
                color = CyberDark,
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .border(1.dp, CyberCardBorder)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (isRegistered) {
                        Surface(
                            color = LiveNeonGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, LiveNeonGreen),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = LiveNeonGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Your Team is Registered! (Slot #${registeredUserTeam?.slotNumber ?: 7})",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = LiveNeonGreen
                                )
                            }
                        }
                    } else if (tournament.status == TournamentStatus.UPCOMING && tournament.filledSlots < tournament.totalSlots) {
                        Button(
                            onClick = onRegister,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("detail_register_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CrimsonRed,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Filled.HowToReg, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${strings.registerFree} (100% Free Entry)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Surface(
                            color = CyberSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Registration is Closed for this tournament",
                                color = TextMuted,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(14.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
        },
        containerColor = CyberBlack,
        modifier = modifier
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Hero Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF2B080C), CyberSurface)
                            )
                        )
                        .border(1.dp, CrimsonRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            StatusBadge(status = tournament.status)
                            Surface(
                                color = CrimsonRed.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = strings.freeEntry,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = CrimsonLight,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = tournament.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Filled.MilitaryTech, contentDescription = null, tint = AmberGold, modifier = Modifier.size(18.dp))
                            Text(
                                text = "Prize: ${tournament.prizePoolPoints} Community Points & Trophy",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AmberGold
                            )
                        }
                    }
                }
            }

            // Key Specs 2x2 Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SpecCard(title = strings.mode, value = tournament.gameMode.label, icon = Icons.Filled.Gamepad, modifier = Modifier.weight(1f))
                    SpecCard(title = strings.map, value = tournament.map, icon = Icons.Filled.Map, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SpecCard(title = strings.dateTime, value = tournament.matchDateTime, icon = Icons.Filled.AccessTime, modifier = Modifier.weight(1f))
                    SpecCard(title = strings.slotsFilled, value = "${tournament.filledSlots}/${tournament.totalSlots}", icon = Icons.Filled.Groups, modifier = Modifier.weight(1f))
                }
            }

            // If registered, show Room Credentials Card right here!
            if (isRegistered) {
                item {
                    RoomCredentialsCard(
                        tournament = tournament,
                        registeredTeam = registeredUserTeam,
                        strings = strings,
                        onCopySuccess = onCopySuccess
                    )
                }
            }

            // Official Fair Play Rules
            item {
                CyberCard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.Gavel, contentDescription = null, tint = CrimsonLight)
                        Text(
                            text = strings.matchRules,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    tournament.rules.forEachIndexed { idx, rule ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "${idx + 1}.",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = AmberGold
                            )
                            Text(
                                text = rule,
                                fontSize = 12.sp,
                                color = TextSecondary,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            // Points System Breakdown
            item {
                CyberCard {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = AmberGold)
                        Text(
                            text = strings.prizeBreakdown,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("BOOYAH (1st Place)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Text("2nd Place", fontSize = 12.sp, color = TextSecondary)
                            Text("3rd Place", fontSize = 12.sp, color = TextSecondary)
                            Text("Each Kill Point", fontSize = 12.sp, color = CrimsonLight)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("12 Placement Pts", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AmberGold)
                            Text("9 Placement Pts", fontSize = 12.sp, color = TextPrimary)
                            Text("8 Placement Pts", fontSize = 12.sp, color = TextPrimary)
                            Text("+1 Pt per Kill", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CrimsonLight)
                        }
                    }
                }
            }

            // Registered Teams List
            item {
                Text(
                    text = "Registered Teams (${tournament.registeredTeams.size})",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            if (tournament.registeredTeams.isEmpty()) {
                item {
                    Text(
                        text = "Be the first team to register for this battle!",
                        color = TextMuted,
                        fontSize = 13.sp
                    )
                }
            } else {
                itemsIndexed(tournament.registeredTeams) { index, team ->
                    CyberCard(backgroundColor = CyberSurfaceVariant) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    color = CyberDark,
                                    shape = RoundedCornerShape(4.dp),
                                    modifier = Modifier.size(28.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "#${team.slotNumber}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = AmberGold
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = team.teamName,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Captain: ${team.captainIgn} (UID: ${team.captainUid})",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Surface(
                                color = LiveNeonGreen.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "CONFIRMED",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = LiveNeonGreen,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CyberSurface,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = CrimsonLight, modifier = Modifier.size(18.dp))
            Column {
                Text(title, fontSize = 10.sp, color = TextMuted)
                Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            }
        }
    }
}
