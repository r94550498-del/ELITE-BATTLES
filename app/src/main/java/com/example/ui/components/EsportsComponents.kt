package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun EsportsHeader(
    strings: Strings,
    currentLanguage: AppLanguage,
    unreadNotificationCount: Int,
    userRole: UserRole,
    onLanguageToggle: () -> Unit,
    onNotificationsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onToggleRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CyberDark,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand logo & title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(CrimsonRed, CrimsonDark)
                                )
                            )
                            .border(1.dp, CrimsonLight, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.LocalFireDepartment,
                            contentDescription = "Elite Battles Logo",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = strings.appTitle,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Role Badge (Clickable to switch between Player & Admin)
                            Surface(
                                color = if (userRole == UserRole.ADMIN) CrimsonDark else CyberSurfaceVariant,
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier
                                    .clickable { onToggleRole() }
                                    .testTag("role_toggle_badge")
                            ) {
                                Text(
                                    text = if (userRole == UserRole.ADMIN) "ADMIN" else "PLAYER",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (userRole == UserRole.ADMIN) AmberGold else TextSecondary,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = strings.appTagline,
                            fontSize = 11.sp,
                            color = TextMuted,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                // Action controls: Language switcher, Notifications, Help
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Language Switcher Chip
                    Surface(
                        color = CyberSurfaceVariant,
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                        modifier = Modifier
                            .clickable { onLanguageToggle() }
                            .testTag("language_toggle_button")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Translate,
                                contentDescription = "Language",
                                tint = AmberGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (currentLanguage == AppLanguage.ENGLISH) "ಕನ್ನಡ" else "ENG",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }
                    }

                    // Notification Button with badge
                    IconButton(
                        onClick = onNotificationsClick,
                        modifier = Modifier.testTag("notifications_button")
                    ) {
                        BadgedBox(
                            badge = {
                                if (unreadNotificationCount > 0) {
                                    Badge(containerColor = CrimsonRed) {
                                        Text(unreadNotificationCount.toString(), color = Color.White)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Notifications,
                                contentDescription = "Notifications",
                                tint = TextPrimary
                            )
                        }
                    }

                    // Help / Rules Button
                    IconButton(
                        onClick = onHelpClick,
                        modifier = Modifier.testTag("help_button")
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.HelpOutline,
                            contentDescription = "Help & Rules",
                            tint = TextPrimary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CyberCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberCardBorder,
    backgroundColor: Color = CyberSurface,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        color = backgroundColor,
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            content = content
        )
    }
}

@Composable
fun StatusBadge(
    status: TournamentStatus,
    modifier: Modifier = Modifier
) {
    val (bg, fg, text) = when (status) {
        TournamentStatus.LIVE -> Triple(LiveNeonGreen.copy(alpha = 0.2f), LiveNeonGreen, "● LIVE NOW")
        TournamentStatus.UPCOMING -> Triple(CrimsonRed.copy(alpha = 0.2f), CrimsonLight, "UPCOMING")
        TournamentStatus.COMPLETED -> Triple(Color.Gray.copy(alpha = 0.2f), Color.LightGray, "FINISHED")
    }

    Surface(
        color = bg,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, fg.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Text(
            text = text,
            color = fg,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun TournamentItemCard(
    tournament: Tournament,
    isRegistered: Boolean,
    strings: Strings,
    onViewDetails: () -> Unit,
    onRegister: () -> Unit,
    onViewCredentials: () -> Unit,
    modifier: Modifier = Modifier
) {
    CyberCard(
        modifier = modifier.testTag("tournament_card_${tournament.id}")
    ) {
        // Top tags row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Game Mode Pill
                Surface(
                    color = CyberSurfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = tournament.gameMode.label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AmberGold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Map Pill
                Surface(
                    color = CyberSurfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = tournament.map,
                        fontSize = 11.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Free Entry Badge
                Surface(
                    color = CrimsonRed.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = strings.freeEntry,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = CrimsonLight,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            StatusBadge(status = tournament.status)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Title
        Text(
            text = tournament.title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Schedule & Rewards info row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = tournament.matchDateTime,
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            // Reward
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.EmojiEvents,
                    contentDescription = null,
                    tint = AmberGold,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "${tournament.prizePoolPoints} Pts",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AmberGold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Slots progress
        val progress = if (tournament.totalSlots > 0) {
            (tournament.filledSlots.toFloat() / tournament.totalSlots).coerceIn(0f, 1f)
        } else 0f

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${strings.slotsFilled}: ${tournament.filledSlots}/${tournament.totalSlots}",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
                Text(
                    text = if (tournament.filledSlots >= tournament.totalSlots) "SLOTS FULL" else "${tournament.totalSlots - tournament.filledSlots} slots left",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (tournament.filledSlots >= tournament.totalSlots) CrimsonLight else LiveNeonGreen
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = if (tournament.filledSlots >= tournament.totalSlots) CrimsonRed else NeonOrange,
                trackColor = CyberSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onViewDetails,
                modifier = Modifier
                    .weight(1f)
                    .testTag("view_details_${tournament.id}"),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(strings.viewDetails, fontSize = 12.sp)
            }

            if (isRegistered) {
                Button(
                    onClick = onViewCredentials,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("view_credentials_${tournament.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tournament.roomCredentials.isReleased) LiveNeonGreen else BlueShield,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = if (tournament.roomCredentials.isReleased) Icons.Filled.Key else Icons.Filled.Lock,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (tournament.roomCredentials.isReleased) "Room Ready!" else "My Room",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else if (tournament.status == TournamentStatus.UPCOMING && tournament.filledSlots < tournament.totalSlots) {
                Button(
                    onClick = onRegister,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("register_button_${tournament.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CrimsonRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = strings.registerNow,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Surface(
                    color = CyberSurfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (tournament.status == TournamentStatus.LIVE) "Match In Progress" else "Registration Closed",
                        fontSize = 12.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RoomCredentialsCard(
    tournament: Tournament,
    registeredTeam: RegisteredTeam?,
    strings: Strings,
    onCopySuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val creds = tournament.roomCredentials

    CyberCard(
        modifier = modifier.testTag("room_credentials_card_${tournament.id}"),
        borderColor = if (creds.isReleased) LiveNeonGreen.copy(alpha = 0.5f) else CrimsonRed.copy(alpha = 0.4f),
        backgroundColor = CyberSurface
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = if (creds.isReleased) Icons.Filled.VpnKey else Icons.Filled.LockClock,
                    contentDescription = null,
                    tint = if (creds.isReleased) LiveNeonGreen else AmberGold,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = strings.roomCredentials,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            if (registeredTeam != null) {
                Surface(
                    color = CrimsonRed.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonLight)
                ) {
                    Text(
                        text = "${strings.slotNumber}: #${registeredTeam.slotNumber}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = CrimsonLight,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = tournament.title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (creds.isReleased && creds.roomId.isNotBlank()) {
            // Credentials revealed!
            Surface(
                color = CyberDark,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(strings.roomId, fontSize = 11.sp, color = TextMuted)
                            Text(
                                text = creds.roomId,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = LiveNeonGreen
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(strings.roomPassword, fontSize = 11.sp, color = TextMuted)
                            Text(
                                text = creds.password,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                fontFamily = FontFamily.Monospace,
                                color = AmberGold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Free Fire Room", "Room ID: ${creds.roomId}\nPassword: ${creds.password}\nSlot: #${registeredTeam?.slotNumber ?: 1}")
                            clipboard.setPrimaryClip(clip)
                            onCopySuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("copy_room_credentials_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LiveNeonGreen,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Icon(imageVector = Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.copyCredentials, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = creds.instructions,
                fontSize = 11.sp,
                color = TextSecondary
            )
        } else {
            // Locked Notice
            Surface(
                color = CyberDark,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null,
                        tint = AmberGold,
                        modifier = Modifier.size(24.dp)
                    )
                    Column {
                        Text(
                            text = "Room Credentials Locked",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = AmberGold
                        )
                        Text(
                            text = strings.roomLockedNotice,
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                }
            }
        }
    }
}
