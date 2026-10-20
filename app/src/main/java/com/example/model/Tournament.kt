package com.example.model

enum class TournamentStatus(val label: String) {
    UPCOMING("UPCOMING"),
    LIVE("LIVE"),
    COMPLETED("COMPLETED")
}

enum class GameMode(val label: String) {
    SQUAD("Squad BR"),
    DUO("Duo BR"),
    SOLO("Solo BR"),
    CLASH_SQUAD("Clash Squad 4v4")
}

data class RoomCredentials(
    val roomId: String = "",
    val password: String = "",
    val isReleased: Boolean = false,
    val instructions: String = "Slot numbers correspond to your registration sequence. Emulators/PC are not allowed. Gun attributes OFF."
)

data class TeamMember(
    val nameOrIgn: String,
    val uid: String,
    val role: String = "Assaulter"
)

data class RegisteredTeam(
    val teamName: String,
    val captainIgn: String,
    val captainUid: String,
    val captainPhone: String,
    val slotNumber: Int,
    val members: List<TeamMember> = emptyList(),
    val registeredAt: String = "Today, 10:30 AM"
)

data class Tournament(
    val id: String,
    val title: String,
    val gameMode: GameMode,
    val map: String,
    val prizePoolPoints: Int,
    val matchDateTime: String,
    val totalSlots: Int,
    val filledSlots: Int,
    val status: TournamentStatus,
    val entryFee: String = "FREE",
    val organizer: String = "Elite Battles Community",
    val roomCredentials: RoomCredentials,
    val rules: List<String> = listOf(
        "Only Mobile Devices allowed (Zero Tolerance for Emulators & iPads)",
        "Gun Attributes: Strictly OFF for fair competitive play",
        "Character Skills: ALLOWED (Active & Passive)",
        "Cheating/Hacks/Scripts will result in permanent community ban",
        "Every team captain must take victory Booyah screenshot with kill count"
    ),
    val registeredTeams: List<RegisteredTeam> = emptyList()
)
