package com.example.model

data class LeaderboardEntry(
    val rank: Int,
    val teamName: String,
    val captainIgn: String,
    val kills: Int,
    val placementPoints: Int,
    val totalPoints: Int,
    val booyahs: Int,
    val isUserTeam: Boolean = false,
    val mvpPlayer: String? = null
)

data class MatchResult(
    val tournamentId: String,
    val tournamentTitle: String,
    val date: String,
    val winningTeam: String,
    val mvpName: String,
    val mvpKills: Int,
    val entries: List<LeaderboardEntry>
)
