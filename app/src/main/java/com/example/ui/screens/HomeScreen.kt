package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.GameMode
import com.example.model.Tournament
import com.example.model.TournamentStatus
import com.example.ui.components.EsportsHeader
import com.example.ui.components.TournamentItemCard
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    tournaments: List<Tournament>,
    registeredIds: Set<String>,
    strings: Strings,
    onViewDetails: (Tournament) -> Unit,
    onRegister: (Tournament) -> Unit,
    onViewCredentials: (Tournament) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf<String>("ALL") }

    val filteredTournaments = remember(tournaments, selectedFilter) {
        when (selectedFilter) {
            "SQUAD" -> tournaments.filter { it.gameMode == GameMode.SQUAD }
            "DUO" -> tournaments.filter { it.gameMode == GameMode.DUO }
            "SOLO" -> tournaments.filter { it.gameMode == GameMode.SOLO }
            "CLASH" -> tournaments.filter { it.gameMode == GameMode.CLASH_SQUAD }
            "LIVE" -> tournaments.filter { it.status == TournamentStatus.LIVE }
            else -> tournaments
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_feed"),
        contentPadding = PaddingValues(bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Featured Hero Esports Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF2C0A0E),
                                Color(0xFF14080B),
                                Color(0xFF0C0A0E)
                            )
                        )
                    )
                    .border(1.dp, CrimsonRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = CrimsonRed,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = "FREE FIRE COMMUNITY",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                letterSpacing = 1.sp
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Security,
                                contentDescription = null,
                                tint = LiveNeonGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Fair Play Verified",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = LiveNeonGreen
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "KARNATAKA PRO SCRIMS",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )

                    Text(
                        text = "Daily Competitive Custom Rooms • Zero Entry Fee • Leaderboard Trophies",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Quick badge 1
                        Surface(
                            color = CyberSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("50,000+", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AmberGold)
                                Text("Pts Awarded", fontSize = 10.sp, color = TextMuted)
                            }
                        }

                        // Quick badge 2
                        Surface(
                            color = CyberSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("100% Free", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = LiveNeonGreen)
                                Text("No Real Money", fontSize = 10.sp, color = TextMuted)
                            }
                        }

                        // Quick badge 3
                        Surface(
                            color = CyberSurfaceVariant,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Mobile Only", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = CrimsonLight)
                                Text("No Emulators", fontSize = 10.sp, color = TextMuted)
                            }
                        }
                    }
                }
            }
        }

        // Filter chips row
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("filter_chips_row"),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf(
                    "ALL" to strings.filterAll,
                    "LIVE" to "● ${strings.liveTournaments}",
                    "SQUAD" to strings.filterSquad,
                    "DUO" to strings.filterDuo,
                    "SOLO" to strings.filterSolo,
                    "CLASH" to strings.filterClashSquad
                )

                items(filters) { (key, label) ->
                    val isSelected = selectedFilter == key
                    Surface(
                        color = if (isSelected) CrimsonRed else CyberSurfaceVariant,
                        shape = RoundedCornerShape(20.dp),
                        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder) else null,
                        modifier = Modifier
                            .clickable { selectedFilter = key }
                            .testTag("filter_chip_$key")
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else TextSecondary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.upcomingTournaments,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${filteredTournaments.size} Available",
                    fontSize = 12.sp,
                    color = TextMuted
                )
            }
        }

        // Tournaments List
        if (filteredTournaments.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.SportsEsports,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Text(
                            text = "No tournaments in this category right now",
                            color = TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            items(filteredTournaments, key = { it.id }) { tournament ->
                Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TournamentItemCard(
                        tournament = tournament,
                        isRegistered = registeredIds.contains(tournament.id),
                        strings = strings,
                        onViewDetails = { onViewDetails(tournament) },
                        onRegister = { onRegister(tournament) },
                        onViewCredentials = { onViewCredentials(tournament) }
                    )
                }
            }
        }
    }
}
