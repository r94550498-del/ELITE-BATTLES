package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LeaderboardEntry
import com.example.model.MatchResult
import com.example.ui.components.CyberCard
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun LeaderboardScreen(
    allTimeEntries: List<LeaderboardEntry>,
    recentResult: MatchResult,
    strings: Strings,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = All-Time, 1 = Recent Match

    val displayEntries = if (selectedTab == 0) allTimeEntries else recentResult.entries
    val top1 = displayEntries.getOrNull(0)
    val top2 = displayEntries.getOrNull(1)
    val top3 = displayEntries.getOrNull(2)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("leaderboard_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header & Tab Toggle
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = strings.communityLeaderboard,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = TextPrimary
                )

                Surface(
                    color = CyberSurfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(4.dp)) {
                        TabButton(
                            title = strings.allTimeRankings,
                            isSelected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            modifier = Modifier.weight(1f)
                        )
                        TabButton(
                            title = strings.tournamentResults,
                            isSelected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Top 3 Podium
        if (top1 != null) {
            item {
                PodiumView(top1 = top1, top2 = top2, top3 = top3, strings = strings)
            }
        }

        // If viewing recent match, show MVP badge
        if (selectedTab == 1 && recentResult.mvpName.isNotBlank()) {
            item {
                CyberCard(
                    borderColor = AmberGold.copy(alpha = 0.6f),
                    backgroundColor = CyberDark
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(AmberGold),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Filled.Star, contentDescription = null, tint = Color.Black, modifier = Modifier.size(22.dp))
                            }
                            Column {
                                Surface(
                                    color = AmberGold.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = strings.mvpBadge,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Black,
                                        color = AmberGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(
                                    text = recentResult.mvpName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            }
                        }

                        Text(
                            text = "${recentResult.mvpKills} KILLS",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = CrimsonLight
                        )
                    }
                }
            }
        }

        // Table Header
        item {
            Surface(
                color = CyberDark,
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(strings.rank, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(36.dp))
                    Text(strings.teamOrPlayer, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.weight(1f))
                    Text(strings.kills, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(48.dp), textAlign = TextAlign.Center)
                    Text(strings.points, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextMuted, modifier = Modifier.width(52.dp), textAlign = TextAlign.End)
                }
            }
        }

        // Leaderboard Rows
        items(displayEntries, key = { "${it.rank}_${it.teamName}" }) { entry ->
            LeaderboardRow(entry = entry, strings = strings)
        }
    }
}

@Composable
private fun TabButton(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (isSelected) CrimsonRed else Color.Transparent,
        shape = RoundedCornerShape(6.dp),
        modifier = modifier
            .clickable { onClick() }
            .testTag(if (isSelected) "active_tab" else "inactive_tab")
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color.White else TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Composable
private fun PodiumView(
    top1: LeaderboardEntry,
    top2: LeaderboardEntry?,
    top3: LeaderboardEntry?,
    strings: Strings
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF220C10), CyberDark)
                )
            )
            .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            // Rank 2 (Silver)
            if (top2 != null) {
                PodiumPillar(
                    entry = top2,
                    rank = 2,
                    color = Color(0xFFC0C0C0),
                    height = 90.dp,
                    modifier = Modifier.weight(1f)
                )
            }

            // Rank 1 (Gold)
            PodiumPillar(
                entry = top1,
                rank = 1,
                color = AmberGold,
                height = 120.dp,
                modifier = Modifier.weight(1.1f)
            )

            // Rank 3 (Bronze)
            if (top3 != null) {
                PodiumPillar(
                    entry = top3,
                    rank = 3,
                    color = Color(0xFFCD7F32),
                    height = 75.dp,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun PodiumPillar(
    entry: LeaderboardEntry,
    rank: Int,
    color: Color,
    height: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(if (rank == 1) 38.dp else 30.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "#$rank",
                fontWeight = FontWeight.Black,
                fontSize = if (rank == 1) 14.sp else 12.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = entry.teamName,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        Text(
            text = "${entry.totalPoints} pts",
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = color
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Pillar block
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(color.copy(alpha = 0.35f), color.copy(alpha = 0.08f))
                    )
                )
                .border(1.dp, color.copy(alpha = 0.5f), RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${entry.kills} KILLS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "${entry.booyahs} Booyahs",
                    fontSize = 9.sp,
                    color = TextSecondary
                )
            }
        }
    }
}

@Composable
private fun LeaderboardRow(
    entry: LeaderboardEntry,
    strings: Strings
) {
    val isUser = entry.isUserTeam
    Surface(
        color = if (isUser) CrimsonRed.copy(alpha = 0.15f) else CyberSurface,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isUser) CrimsonLight else CyberCardBorder.copy(alpha = 0.3f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "#${entry.rank}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = when (entry.rank) {
                    1 -> AmberGold
                    2 -> Color(0xFFC0C0C0)
                    3 -> Color(0xFFCD7F32)
                    else -> TextSecondary
                },
                modifier = Modifier.width(36.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = entry.teamName,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUser) CrimsonLight else TextPrimary
                    )
                    if (isUser) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            color = CrimsonRed,
                            shape = RoundedCornerShape(3.dp)
                        ) {
                            Text(
                                text = "YOU",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                }
                Text(
                    text = "Captain: ${entry.captainIgn} • ${entry.booyahs} Booyahs",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            Text(
                text = "${entry.kills}",
                fontSize = 12.sp,
                color = CrimsonLight,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(48.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "${entry.totalPoints}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = AmberGold,
                modifier = Modifier.width(52.dp),
                textAlign = TextAlign.End
            )
        }
    }
}
