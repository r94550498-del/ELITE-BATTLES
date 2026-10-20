package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Tournament
import com.example.model.TournamentStatus
import com.example.ui.components.RoomCredentialsCard
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun MyMatchesScreen(
    registeredTournaments: List<Tournament>,
    strings: Strings,
    onViewTournamentDetails: (Tournament) -> Unit,
    onCopySuccess: () -> Unit,
    onExploreTournaments: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("my_matches_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "My Registered Battles",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                    Text(
                        text = "Authorized Custom Room Credentials & Assigned Slots",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                Surface(
                    color = CrimsonRed.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "${registeredTournaments.size} Registered",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CrimsonLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        if (registeredTournaments.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MeetingRoom,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(54.dp)
                        )
                        Text(
                            text = "You haven't joined any battle yet",
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Register for upcoming free tournaments to receive custom room IDs and passwords.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Button(
                            onClick = onExploreTournaments,
                            colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.testTag("explore_tournaments_button")
                        ) {
                            Text("Browse Tournaments", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            items(registeredTournaments, key = { it.id }) { tournament ->
                val userTeam = tournament.registeredTeams.find { it.captainIgn.contains("KT_") || it.teamName.contains("KARNATAKA TIGERS") }
                    ?: tournament.registeredTeams.firstOrNull()

                RoomCredentialsCard(
                    tournament = tournament,
                    registeredTeam = userTeam,
                    strings = strings,
                    onCopySuccess = onCopySuccess,
                    modifier = Modifier.testTag("room_card_${tournament.id}")
                )
            }
        }
    }
}
