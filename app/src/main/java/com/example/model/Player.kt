package com.example.model

enum class UserRole {
    PLAYER,
    ADMIN
}

data class SquadRoster(
    val teamName: String,
    val captainIgn: String,
    val captainUid: String,
    val player2: TeamMember,
    val player3: TeamMember,
    val player4: TeamMember
)

data class PlayerProfile(
    val name: String,
    val ign: String,
    val freeFireUid: String,
    val tier: String = "Grandmaster V",
    val matchesPlayed: Int = 84,
    val wins: Int = 32,
    val kills: Int = 389,
    val kdRatio: Double = 4.63,
    val role: UserRole = UserRole.PLAYER,
    val phoneNumber: String = "+91 98450 12345",
    val email: String = "kannadagamer@elitebattles.in",
    val squadRoster: SquadRoster = SquadRoster(
        teamName = "KARNATAKA TIGERS",
        captainIgn = "KT_VISHNU_FF",
        captainUid = "829471928",
        player2 = TeamMember("KT_CHETHAN", "738291048", "Sniper"),
        player3 = TeamMember("KT_MOHAN_99", "649102847", "Rusher"),
        player4 = TeamMember("KT_PRADEEP", "551928471", "Support")
    )
)
