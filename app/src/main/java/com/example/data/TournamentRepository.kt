package com.example.data

import com.example.model.*
import com.example.ui.localization.AppLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TournamentRepository {

    private val _language = MutableStateFlow(AppLanguage.ENGLISH)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    private val _currentUser = MutableStateFlow(PlayerProfile(
        name = "Vishnu Kumar",
        ign = "KT_VISHNU_FF",
        freeFireUid = "829471928",
        tier = "Grandmaster V",
        matchesPlayed = 84,
        wins = 32,
        kills = 389,
        kdRatio = 4.63,
        role = UserRole.PLAYER
    ))
    val currentUser: StateFlow<PlayerProfile> = _currentUser.asStateFlow()

    private val _tournaments = MutableStateFlow<List<Tournament>>(listOf(
        Tournament(
            id = "EB-101",
            title = "KARNATAKA SQUAD CHAMPIONSHIP #12",
            gameMode = GameMode.SQUAD,
            map = "Bermuda",
            prizePoolPoints = 15000,
            matchDateTime = "Today, 8:00 PM IST",
            totalSlots = 48,
            filledSlots = 42,
            status = TournamentStatus.UPCOMING,
            organizer = "Elite Battles Karnataka",
            roomCredentials = RoomCredentials(
                roomId = "8392014",
                password = "FF800",
                isReleased = false,
                instructions = "Room ID and Password will unlock 15 minutes before match start. Strictly Mobile only!"
            ),
            registeredTeams = listOf(
                RegisteredTeam("ROYAL CHALLENGERS", "RC_ROHIT", "94829104", "9845000001", 1),
                RegisteredTeam("MYSORE WARRIORS", "MW_RAGHU", "81920394", "9845000002", 2),
                RegisteredTeam("BENGALURU KINGS", "BK_SANTHU", "73619204", "9845000003", 3),
                RegisteredTeam("COORG SNIPERS", "CS_DEEPAK", "62910482", "9845000004", 4)
            )
        ),
        Tournament(
            id = "EB-102",
            title = "BANGALORE BATTLE ROYALE - DUO",
            gameMode = GameMode.DUO,
            map = "Purgatory",
            prizePoolPoints = 10000,
            matchDateTime = "Live Now • Round 2",
            totalSlots = 24,
            filledSlots = 24,
            status = TournamentStatus.LIVE,
            organizer = "Bangalore Free Fire Guild",
            roomCredentials = RoomCredentials(
                roomId = "4729184",
                password = "DUO99",
                isReleased = true,
                instructions = "Join custom room immediately! Slot 7 is assigned to your team KARNATAKA TIGERS."
            ),
            registeredTeams = listOf(
                RegisteredTeam("KARNATAKA TIGERS", "KT_VISHNU_FF", "829471928", "9845012345", 7, listOf(
                    TeamMember("KT_VISHNU_FF", "829471928", "Captain"),
                    TeamMember("KT_CHETHAN", "738291048", "Partner")
                ))
            )
        ),
        Tournament(
            id = "EB-103",
            title = "CLASH SQUAD 4v4 PRO SCRIMS",
            gameMode = GameMode.CLASH_SQUAD,
            map = "Kalahari",
            prizePoolPoints = 8000,
            matchDateTime = "Tomorrow, 5:00 PM IST",
            totalSlots = 16,
            filledSlots = 11,
            status = TournamentStatus.UPCOMING,
            organizer = "Elite Battles Hub",
            roomCredentials = RoomCredentials(
                roomId = "5920148",
                password = "CS4V4",
                isReleased = false,
                instructions = "Best of 3 rounds. Limit grenades to 1 per round."
            )
        ),
        Tournament(
            id = "EB-104",
            title = "SOLO WARRIORS WEEKEND BASH",
            gameMode = GameMode.SOLO,
            map = "Alpine",
            prizePoolPoints = 6000,
            matchDateTime = "Saturday, 4:00 PM IST",
            totalSlots = 48,
            filledSlots = 35,
            status = TournamentStatus.UPCOMING,
            organizer = "Kannada Esports League",
            roomCredentials = RoomCredentials(
                roomId = "9201482",
                password = "SOLO1",
                isReleased = false,
                instructions = "Individual battle royale ranking rules apply."
            )
        ),
        Tournament(
            id = "EB-105",
            title = "MYSORE SQUAD GRAND SHOWDOWN",
            gameMode = GameMode.SQUAD,
            map = "Nexterra",
            prizePoolPoints = 20000,
            matchDateTime = "Completed Yesterday",
            totalSlots = 48,
            filledSlots = 48,
            status = TournamentStatus.COMPLETED,
            organizer = "Elite Battles Karnataka",
            roomCredentials = RoomCredentials(
                roomId = "3910284",
                password = "CLOSED",
                isReleased = true,
                instructions = "Match completed. Scores published."
            )
        )
    ))
    val tournaments: StateFlow<List<Tournament>> = _tournaments.asStateFlow()

    private val _registeredTournamentIds = MutableStateFlow<Set<String>>(setOf("EB-102"))
    val registeredTournamentIds: StateFlow<Set<String>> = _registeredTournamentIds.asStateFlow()

    private val _notifications = MutableStateFlow<List<AppNotification>>(listOf(
        AppNotification(
            id = "N1",
            title = "🚨 Custom Room Released!",
            message = "Room ID: 4729184 and Pass: DUO99 for BANGALORE BATTLE ROYALE is now live. Enter Slot 7 now!",
            timestamp = "5 mins ago",
            type = NotificationType.ROOM_RELEASED,
            relatedTournamentId = "EB-102"
        ),
        AppNotification(
            id = "N2",
            title = "🏆 Scrims Scheduled Tonight",
            message = "KARNATAKA SQUAD CHAMPIONSHIP starts at 8:00 PM IST. Get your squad ready.",
            timestamp = "1 hour ago",
            type = NotificationType.MATCH_REMINDER,
            relatedTournamentId = "EB-101"
        ),
        AppNotification(
            id = "N3",
            title = "🛡️ Fair Play & Anti-Cheat Alert",
            message = "Strict zero tolerance on PC emulators & modified APKs. Violators will face immediate ban.",
            timestamp = "Yesterday",
            type = NotificationType.COMMUNITY_ALERT
        )
    ))
    val notifications: StateFlow<List<AppNotification>> = _notifications.asStateFlow()

    private val _allTimeLeaderboard = MutableStateFlow<List<LeaderboardEntry>>(listOf(
        LeaderboardEntry(1, "KARNATAKA TIGERS", "KT_VISHNU_FF", kills = 186, placementPoints = 240, totalPoints = 426, booyahs = 8, isUserTeam = true, mvpPlayer = "KT_VISHNU_FF"),
        LeaderboardEntry(2, "ROYAL CHALLENGERS", "RC_ROHIT", kills = 162, placementPoints = 210, totalPoints = 372, booyahs = 6, mvpPlayer = "RC_NINJA"),
        LeaderboardEntry(3, "MYSORE WARRIORS", "MW_RAGHU", kills = 148, placementPoints = 195, totalPoints = 343, booyahs = 5),
        LeaderboardEntry(4, "BENGALURU KINGS", "BK_SANTHU", kills = 135, placementPoints = 180, totalPoints = 315, booyahs = 4),
        LeaderboardEntry(5, "COORG SNIPERS", "CS_DEEPAK", kills = 120, placementPoints = 160, totalPoints = 280, booyahs = 3),
        LeaderboardEntry(6, "HUBBALLI HEROES", "HH_AKASH", kills = 112, placementPoints = 150, totalPoints = 262, booyahs = 2),
        LeaderboardEntry(7, "MANGALURU SHARKS", "MS_KIRAN", kills = 98, placementPoints = 135, totalPoints = 233, booyahs = 2),
        LeaderboardEntry(8, "BELLARY RAIDERS", "BR_SURESH", kills = 85, placementPoints = 110, totalPoints = 195, booyahs = 1)
    ))
    val allTimeLeaderboard: StateFlow<List<LeaderboardEntry>> = _allTimeLeaderboard.asStateFlow()

    private val _recentMatchResult = MutableStateFlow<MatchResult>(
        MatchResult(
            tournamentId = "EB-105",
            tournamentTitle = "MYSORE SQUAD GRAND SHOWDOWN",
            date = "Oct 19, 2026",
            winningTeam = "KARNATAKA TIGERS",
            mvpName = "KT_VISHNU_FF",
            mvpKills = 14,
            entries = listOf(
                LeaderboardEntry(1, "KARNATAKA TIGERS", "KT_VISHNU_FF", kills = 28, placementPoints = 20, totalPoints = 48, booyahs = 2, isUserTeam = true, mvpPlayer = "KT_VISHNU_FF"),
                LeaderboardEntry(2, "MYSORE WARRIORS", "MW_RAGHU", kills = 22, placementPoints = 14, totalPoints = 36, booyahs = 1),
                LeaderboardEntry(3, "ROYAL CHALLENGERS", "RC_ROHIT", kills = 18, placementPoints = 10, totalPoints = 28, booyahs = 0),
                LeaderboardEntry(4, "COORG SNIPERS", "CS_DEEPAK", kills = 14, placementPoints = 8, totalPoints = 22, booyahs = 0),
                LeaderboardEntry(5, "BENGALURU KINGS", "BK_SANTHU", kills = 12, placementPoints = 6, totalPoints = 18, booyahs = 0)
            )
        )
    )
    val recentMatchResult: StateFlow<MatchResult> = _recentMatchResult.asStateFlow()

    fun setLanguage(lang: AppLanguage) {
        _language.value = lang
    }

    fun toggleRole() {
        _currentUser.update {
            it.copy(role = if (it.role == UserRole.PLAYER) UserRole.ADMIN else UserRole.PLAYER)
        }
    }

    fun updateProfile(name: String, ign: String, uid: String, phone: String) {
        _currentUser.update {
            it.copy(
                name = name,
                ign = ign,
                freeFireUid = uid,
                phoneNumber = phone
            )
        }
    }

    fun updateSquadRoster(roster: SquadRoster) {
        _currentUser.update {
            it.copy(squadRoster = roster)
        }
    }

    fun registerTeam(
        tournamentId: String,
        teamName: String,
        captainIgn: String,
        captainUid: String,
        phone: String,
        members: List<TeamMember>
    ): Boolean {
        val tournament = _tournaments.value.find { it.id == tournamentId } ?: return false
        if (tournament.filledSlots >= tournament.totalSlots) return false

        val nextSlot = tournament.filledSlots + 1
        val newRegistration = RegisteredTeam(
            teamName = teamName,
            captainIgn = captainIgn,
            captainUid = captainUid,
            captainPhone = phone,
            slotNumber = nextSlot,
            members = members
        )

        _tournaments.update { list ->
            list.map { t ->
                if (t.id == tournamentId) {
                    t.copy(
                        filledSlots = t.filledSlots + 1,
                        registeredTeams = t.registeredTeams + newRegistration
                    )
                } else t
            }
        }

        _registeredTournamentIds.update { it + tournamentId }

        // Add confirmation notification
        _notifications.update { current ->
            listOf(
                AppNotification(
                    id = "REG_${System.currentTimeMillis()}",
                    title = "🎟️ Registration Confirmed!",
                    message = "Your team '$teamName' is booked for ${tournament.title} at Slot #$nextSlot. Custom room details will appear in 'My Rooms'.",
                    timestamp = "Just now",
                    type = NotificationType.MATCH_REMINDER,
                    relatedTournamentId = tournamentId
                )
            ) + current
        }
        return true
    }

    // Admin Operations
    fun createTournament(
        title: String,
        gameMode: GameMode,
        map: String,
        prizePoints: Int,
        dateTime: String,
        slots: Int
    ) {
        val newId = "EB-${(106..999).random()}"
        val newTournament = Tournament(
            id = newId,
            title = title.ifBlank { "FREE FIRE COMMUNITY TOURNAMENT" },
            gameMode = gameMode,
            map = map.ifBlank { "Bermuda" },
            prizePoolPoints = prizePoints,
            matchDateTime = dateTime.ifBlank { "Upcoming Today" },
            totalSlots = slots,
            filledSlots = 0,
            status = TournamentStatus.UPCOMING,
            roomCredentials = RoomCredentials(
                roomId = "",
                password = "",
                isReleased = false,
                instructions = "Room details will be published by admin 15 minutes before match."
            )
        )
        _tournaments.update { listOf(newTournament) + it }

        _notifications.update { current ->
            listOf(
                AppNotification(
                    id = "NEW_${System.currentTimeMillis()}",
                    title = "🔥 New Tournament Announced!",
                    message = "$title ($map • ${gameMode.label}) is open for free registration. Limited to $slots slots!",
                    timestamp = "Just now",
                    type = NotificationType.TOURNAMENT_NEW,
                    relatedTournamentId = newId
                )
            ) + current
        }
    }

    fun updateRoomCredentials(
        tournamentId: String,
        roomId: String,
        password: String,
        releaseNow: Boolean
    ) {
        var updatedTitle = ""
        _tournaments.update { list ->
            list.map { t ->
                if (t.id == tournamentId) {
                    updatedTitle = t.title
                    t.copy(
                        roomCredentials = t.roomCredentials.copy(
                            roomId = roomId,
                            password = password,
                            isReleased = releaseNow
                        ),
                        status = if (releaseNow && t.status == TournamentStatus.UPCOMING) TournamentStatus.LIVE else t.status
                    )
                } else t
            }
        }

        if (releaseNow) {
            _notifications.update { current ->
                listOf(
                    AppNotification(
                        id = "ROOM_${System.currentTimeMillis()}",
                        title = "🚨 Custom Room Released: $roomId",
                        message = "Room ID: $roomId, Password: $password for $updatedTitle has been released! Join room immediately.",
                        timestamp = "Just now",
                        type = NotificationType.ROOM_RELEASED,
                        relatedTournamentId = tournamentId
                    )
                ) + current
            }
        }
    }

    fun updateTournamentStatus(tournamentId: String, status: TournamentStatus) {
        _tournaments.update { list ->
            list.map { if (it.id == tournamentId) it.copy(status = status) else it }
        }
    }

    fun markAllNotificationsAsRead() {
        _notifications.update { list -> list.map { it.copy(isRead = true) } }
    }
}
