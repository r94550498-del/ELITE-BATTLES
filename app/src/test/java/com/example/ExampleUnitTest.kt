package com.example

import com.example.data.TournamentRepository
import com.example.model.GameMode
import com.example.model.TournamentStatus
import com.example.model.UserRole
import com.example.ui.localization.AppLanguage
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ExampleUnitTest {
    private lateinit var repository: TournamentRepository

    @Before
    fun setUp() {
        repository = TournamentRepository()
    }

    @Test
    fun testInitialTournamentsLoaded() {
        val tournaments = repository.tournaments.value
        assertTrue("Tournaments should not be empty", tournaments.isNotEmpty())
        val liveTournaments = tournaments.filter { it.status == TournamentStatus.LIVE }
        assertTrue("Should have live tournament", liveTournaments.isNotEmpty())
    }

    @Test
    fun testTeamRegistration() {
        val tournamentId = "EB-101"
        val success = repository.registerTeam(
            tournamentId = tournamentId,
            teamName = "ALPHA WARRIORS",
            captainIgn = "ALPHA_CAPTAIN",
            captainUid = "11223344",
            phone = "9845099999",
            members = emptyList()
        )
        assertTrue("Registration should succeed", success)
        assertTrue("Tournament should be in registered list", repository.registeredTournamentIds.value.contains(tournamentId))
    }

    @Test
    fun testRoomCredentialsRelease() {
        val tournamentId = "EB-101"
        repository.updateRoomCredentials(tournamentId, "998877", "SECRET123", true)
        val updated = repository.tournaments.value.find { it.id == tournamentId }
        assertNotNull(updated)
        assertEquals("998877", updated!!.roomCredentials.roomId)
        assertEquals("SECRET123", updated.roomCredentials.password)
        assertTrue(updated.roomCredentials.isReleased)
    }

    @Test
    fun testRoleToggle() {
        assertEquals(UserRole.PLAYER, repository.currentUser.value.role)
        repository.toggleRole()
        assertEquals(UserRole.ADMIN, repository.currentUser.value.role)
        repository.toggleRole()
        assertEquals(UserRole.PLAYER, repository.currentUser.value.role)
    }

    @Test
    fun testLanguageSelection() {
        assertEquals(AppLanguage.ENGLISH, repository.language.value)
        repository.setLanguage(AppLanguage.KANNADA)
        assertEquals(AppLanguage.KANNADA, repository.language.value)
    }
}
