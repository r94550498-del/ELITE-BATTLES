package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TournamentRepository
import com.example.model.*
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.LocalizedContent
import com.example.ui.localization.Strings
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class AppTab {
    TOURNAMENTS,
    MY_MATCHES,
    LEADERBOARD,
    PROFILE,
    ADMIN
}

class TournamentViewModel(
    private val repository: TournamentRepository = TournamentRepository()
) : ViewModel() {

    val language = repository.language
    val currentUser = repository.currentUser
    val tournaments = repository.tournaments
    val registeredTournamentIds = repository.registeredTournamentIds
    val notifications = repository.notifications
    val allTimeLeaderboard = repository.allTimeLeaderboard
    val recentMatchResult = repository.recentMatchResult

    private val _currentTab = MutableStateFlow(AppTab.TOURNAMENTS)
    val currentTab = _currentTab.asStateFlow()

    private val _selectedTournamentForDetails = MutableStateFlow<Tournament?>(null)
    val selectedTournamentForDetails = _selectedTournamentForDetails.asStateFlow()

    private val _selectedTournamentForRegistration = MutableStateFlow<Tournament?>(null)
    val selectedTournamentForRegistration = _selectedTournamentForRegistration.asStateFlow()

    private val _showAuthDialog = MutableStateFlow(false)
    val showAuthDialog = _showAuthDialog.asStateFlow()

    private val _showHelpDialog = MutableStateFlow(false)
    val showHelpDialog = _showHelpDialog.asStateFlow()

    private val _showNotificationsSheet = MutableStateFlow(false)
    val showNotificationsSheet = _showNotificationsSheet.asStateFlow()

    private val _showCreateTournamentDialog = MutableStateFlow(false)
    val showCreateTournamentDialog = _showCreateTournamentDialog.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun setLanguage(lang: AppLanguage) {
        repository.setLanguage(lang)
    }

    fun toggleLanguage() {
        val next = if (language.value == AppLanguage.ENGLISH) AppLanguage.KANNADA else AppLanguage.ENGLISH
        repository.setLanguage(next)
    }

    fun showDetails(tournament: Tournament) {
        _selectedTournamentForDetails.value = tournament
    }

    fun dismissDetails() {
        _selectedTournamentForDetails.value = null
    }

    fun openRegistration(tournament: Tournament) {
        _selectedTournamentForRegistration.value = tournament
    }

    fun dismissRegistration() {
        _selectedTournamentForRegistration.value = null
    }

    fun openAuthDialog() {
        _showAuthDialog.value = true
    }

    fun dismissAuthDialog() {
        _showAuthDialog.value = false
    }

    fun openHelpDialog() {
        _showHelpDialog.value = true
    }

    fun dismissHelpDialog() {
        _showHelpDialog.value = false
    }

    fun openNotifications() {
        _showNotificationsSheet.value = true
        repository.markAllNotificationsAsRead()
    }

    fun dismissNotifications() {
        _showNotificationsSheet.value = false
    }

    fun openCreateTournament() {
        _showCreateTournamentDialog.value = true
    }

    fun dismissCreateTournament() {
        _showCreateTournamentDialog.value = false
    }

    fun toggleUserRole() {
        repository.toggleRole()
        val roleName = if (currentUser.value.role == UserRole.PLAYER) "Admin Mode Activated" else "Switched to Player Mode"
        showSnackbar(roleName)
    }

    fun updateProfile(name: String, ign: String, uid: String, phone: String) {
        repository.updateProfile(name, ign, uid, phone)
        showSnackbar("Profile updated successfully")
    }

    fun updateSquadRoster(roster: SquadRoster) {
        repository.updateSquadRoster(roster)
        showSnackbar("Squad Roster saved")
    }

    fun registerTeam(
        tournamentId: String,
        teamName: String,
        captainIgn: String,
        captainUid: String,
        phone: String,
        members: List<TeamMember>
    ) {
        val success = repository.registerTeam(
            tournamentId = tournamentId,
            teamName = teamName,
            captainIgn = captainIgn,
            captainUid = captainUid,
            phone = phone,
            members = members
        )
        if (success) {
            val strings = LocalizedContent.get(language.value)
            showSnackbar(strings.registrationSuccessful)
            dismissRegistration()
        } else {
            showSnackbar("Registration failed or slots are full.")
        }
    }

    fun createTournament(
        title: String,
        gameMode: GameMode,
        map: String,
        prizePoints: Int,
        dateTime: String,
        slots: Int
    ) {
        repository.createTournament(title, gameMode, map, prizePoints, dateTime, slots)
        showSnackbar("Tournament created and published!")
        dismissCreateTournament()
    }

    fun updateRoomCredentials(
        tournamentId: String,
        roomId: String,
        password: String,
        releaseNow: Boolean
    ) {
        repository.updateRoomCredentials(tournamentId, roomId, password, releaseNow)
        showSnackbar(if (releaseNow) "Room ID & Password broadcasted to players!" else "Room credentials updated")
    }

    fun updateTournamentStatus(tournamentId: String, status: TournamentStatus) {
        repository.updateTournamentStatus(tournamentId, status)
        showSnackbar("Tournament status updated to ${status.label}")
    }

    fun showSnackbar(msg: String) {
        _snackbarMessage.value = msg
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
