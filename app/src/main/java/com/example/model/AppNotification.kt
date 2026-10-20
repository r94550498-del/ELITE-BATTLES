package com.example.model

enum class NotificationType {
    ROOM_RELEASED,
    MATCH_REMINDER,
    TOURNAMENT_NEW,
    COMMUNITY_ALERT
}

data class AppNotification(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val type: NotificationType,
    val relatedTournamentId: String? = null,
    val isRead: Boolean = false
)
