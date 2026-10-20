package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.Tournament
import com.example.model.UserRole
import com.example.ui.components.EsportsHeader
import com.example.ui.localization.LocalizedContent
import com.example.ui.screens.*
import com.example.ui.theme.*

@Composable
fun MainScreen(
    viewModel: TournamentViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.language.collectAsStateWithLifecycle()
    val strings = remember(language) { LocalizedContent.get(language) }
    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val tournaments by viewModel.tournaments.collectAsStateWithLifecycle()
    val registeredIds by viewModel.registeredTournamentIds.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val allTimeLeaderboard by viewModel.allTimeLeaderboard.collectAsStateWithLifecycle()
    val recentMatchResult by viewModel.recentMatchResult.collectAsStateWithLifecycle()

    val selectedTournamentForDetails by viewModel.selectedTournamentForDetails.collectAsStateWithLifecycle()
    val selectedTournamentForRegistration by viewModel.selectedTournamentForRegistration.collectAsStateWithLifecycle()
    val showAuthDialog by viewModel.showAuthDialog.collectAsStateWithLifecycle()
    val showHelpDialog by viewModel.showHelpDialog.collectAsStateWithLifecycle()
    val showNotificationsSheet by viewModel.showNotificationsSheet.collectAsStateWithLifecycle()
    val showCreateTournamentDialog by viewModel.showCreateTournamentDialog.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbar()
        }
    }

    // Handle back button when sub-screens/dialogs are open
    if (selectedTournamentForDetails != null) {
        BackHandler {
            viewModel.dismissDetails()
        }
    } else if (currentTab != AppTab.TOURNAMENTS) {
        BackHandler {
            viewModel.setTab(AppTab.TOURNAMENTS)
        }
    }

    Scaffold(
        topBar = {
            if (selectedTournamentForDetails == null) {
                EsportsHeader(
                    strings = strings,
                    currentLanguage = language,
                    unreadNotificationCount = notifications.count { !it.isRead },
                    userRole = currentUser.role,
                    onLanguageToggle = { viewModel.toggleLanguage() },
                    onNotificationsClick = { viewModel.openNotifications() },
                    onHelpClick = { viewModel.openHelpDialog() },
                    onToggleRole = { viewModel.toggleUserRole() }
                )
            }
        },
        bottomBar = {
            if (selectedTournamentForDetails == null) {
                NavigationBar(
                    containerColor = CyberDark,
                    tonalElevation = 8.dp,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.testTag("bottom_nav_bar")
                ) {
                    val tabs = listOf(
                        AppTab.TOURNAMENTS to (strings.tabTournaments to Icons.Filled.SportsEsports to Icons.Outlined.SportsEsports),
                        AppTab.MY_MATCHES to (strings.tabMyMatches to Icons.Filled.Key to Icons.Outlined.Key),
                        AppTab.LEADERBOARD to (strings.tabLeaderboard to Icons.Filled.EmojiEvents to Icons.Outlined.EmojiEvents),
                        AppTab.PROFILE to (strings.tabProfile to Icons.Filled.Person to Icons.Outlined.Person),
                        AppTab.ADMIN to (strings.tabAdmin to Icons.Filled.AdminPanelSettings to Icons.Outlined.AdminPanelSettings)
                    )

                    tabs.forEach { (tab, details) ->
                        val (labelIcons, _) = details
                        val (label, selectedIcon) = labelIcons
                        val isSelected = currentTab == tab

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.setTab(tab) },
                            icon = {
                                Icon(
                                    imageVector = selectedIcon,
                                    contentDescription = label,
                                    tint = if (isSelected) CrimsonLight else TextMuted
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) TextPrimary else TextMuted
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = CrimsonDark.copy(alpha = 0.4f),
                                selectedIconColor = CrimsonLight,
                                unselectedIconColor = TextMuted
                            ),
                            modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.testTag("app_snackbar_host")
            )
        },
        containerColor = CyberBlack,
        modifier = modifier
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(CyberBlack),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp)
            ) {
                if (selectedTournamentForDetails != null) {
                    val targetTournament = selectedTournamentForDetails!!
                    TournamentDetailScreen(
                        tournament = targetTournament,
                        isRegistered = registeredIds.contains(targetTournament.id),
                        strings = strings,
                        onBack = { viewModel.dismissDetails() },
                        onRegister = {
                            viewModel.dismissDetails()
                            viewModel.openRegistration(targetTournament)
                        },
                        onCopySuccess = {
                            viewModel.showSnackbar(strings.credentialsCopied)
                        }
                    )
                } else {
                    when (currentTab) {
                        AppTab.TOURNAMENTS -> {
                            HomeScreen(
                                tournaments = tournaments,
                                registeredIds = registeredIds,
                                strings = strings,
                                onViewDetails = { viewModel.showDetails(it) },
                                onRegister = { viewModel.openRegistration(it) },
                                onViewCredentials = {
                                    viewModel.setTab(AppTab.MY_MATCHES)
                                }
                            )
                        }
                        AppTab.MY_MATCHES -> {
                            val registeredList = tournaments.filter { registeredIds.contains(it.id) }
                            MyMatchesScreen(
                                registeredTournaments = registeredList,
                                strings = strings,
                                onViewTournamentDetails = { viewModel.showDetails(it) },
                                onCopySuccess = { viewModel.showSnackbar(strings.credentialsCopied) },
                                onExploreTournaments = { viewModel.setTab(AppTab.TOURNAMENTS) }
                            )
                        }
                        AppTab.LEADERBOARD -> {
                            LeaderboardScreen(
                                allTimeEntries = allTimeLeaderboard,
                                recentResult = recentMatchResult,
                                strings = strings
                            )
                        }
                        AppTab.PROFILE -> {
                            ProfileScreen(
                                profile = currentUser,
                                strings = strings,
                                currentLanguage = language,
                                onLanguageSelect = { viewModel.setLanguage(it) },
                                onToggleRole = { viewModel.toggleUserRole() },
                                onOpenAuthDialog = { viewModel.openAuthDialog() },
                                onSaveProfile = { name, ign, uid, phone ->
                                    viewModel.updateProfile(name, ign, uid, phone)
                                },
                                onSaveRoster = { roster ->
                                    viewModel.updateSquadRoster(roster)
                                },
                                onOpenHelp = { viewModel.openHelpDialog() }
                            )
                        }
                        AppTab.ADMIN -> {
                            AdminDashboardScreen(
                                tournaments = tournaments,
                                userRole = currentUser.role,
                                strings = strings,
                                onOpenCreateTournament = { viewModel.openCreateTournament() },
                                onUpdateRoomCredentials = { tId, rId, pass, release ->
                                    viewModel.updateRoomCredentials(tId, rId, pass, release)
                                },
                                onUpdateStatus = { tId, status ->
                                    viewModel.updateTournamentStatus(tId, status)
                                },
                                onToggleRole = { viewModel.toggleUserRole() }
                            )
                        }
                    }
                }
            }
        }
    }

    // Registration Modal
    selectedTournamentForRegistration?.let { t ->
        RegistrationDialog(
            tournament = t,
            currentUser = currentUser,
            strings = strings,
            onDismiss = { viewModel.dismissRegistration() },
            onSubmit = { teamName, captainIgn, captainUid, phone, members ->
                viewModel.registerTeam(t.id, teamName, captainIgn, captainUid, phone, members)
            }
        )
    }

    // Notifications Dialog
    if (showNotificationsSheet) {
        NotificationsDialog(
            notifications = notifications,
            strings = strings,
            onDismiss = { viewModel.dismissNotifications() }
        )
    }

    // Help & FAQ Dialog
    if (showHelpDialog) {
        HelpDialog(
            strings = strings,
            onDismiss = { viewModel.dismissHelpDialog() },
            onReportSubmitted = {
                viewModel.dismissHelpDialog()
                viewModel.showSnackbar("Fair play report submitted to community anti-cheat moderators.")
            }
        )
    }

    // Auth Dialog (Google / Phone OTP)
    if (showAuthDialog) {
        AuthDialog(
            strings = strings,
            onDismiss = { viewModel.dismissAuthDialog() },
            onAuthSuccess = { method ->
                viewModel.showSnackbar("Authenticated successfully via $method")
            }
        )
    }

    // Create Tournament Dialog (Admin)
    if (showCreateTournamentDialog) {
        CreateTournamentDialog(
            strings = strings,
            onDismiss = { viewModel.dismissCreateTournament() },
            onCreate = { title, mode, map, prize, dateTime, slots ->
                viewModel.createTournament(title, mode, map, prize, dateTime, slots)
            }
        )
    }
}
