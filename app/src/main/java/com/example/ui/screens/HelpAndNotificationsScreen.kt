package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppNotification
import com.example.model.NotificationType
import com.example.ui.components.CyberCard
import com.example.ui.localization.Strings
import com.example.ui.theme.*

@Composable
fun NotificationsDialog(
    notifications: List<AppNotification>,
    strings: Strings,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .fillMaxHeight(0.80f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
                .testTag("notifications_dialog"),
            color = CyberSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
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
                        Icon(Icons.Filled.NotificationsActive, contentDescription = null, tint = CrimsonLight)
                        Text(
                            text = strings.notifications,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_notifications_button")
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextPrimary)
                    }
                }

                HorizontalDivider(color = CyberCardBorder, modifier = Modifier.padding(vertical = 8.dp))

                if (notifications.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No notifications yet", color = TextSecondary)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(notifications, key = { it.id }) { notif ->
                            Surface(
                                color = CyberDark,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (notif.type == NotificationType.ROOM_RELEASED) LiveNeonGreen.copy(alpha = 0.5f) else CyberCardBorder.copy(alpha = 0.4f)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(CircleShape)
                                            .background(
                                                when (notif.type) {
                                                    NotificationType.ROOM_RELEASED -> LiveNeonGreen.copy(alpha = 0.2f)
                                                    NotificationType.MATCH_REMINDER -> AmberGold.copy(alpha = 0.2f)
                                                    NotificationType.TOURNAMENT_NEW -> CrimsonRed.copy(alpha = 0.2f)
                                                    NotificationType.COMMUNITY_ALERT -> BlueShield.copy(alpha = 0.2f)
                                                }
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = when (notif.type) {
                                                NotificationType.ROOM_RELEASED -> Icons.Filled.Key
                                                NotificationType.MATCH_REMINDER -> Icons.Filled.Schedule
                                                NotificationType.TOURNAMENT_NEW -> Icons.Filled.EmojiEvents
                                                NotificationType.COMMUNITY_ALERT -> Icons.Filled.Security
                                            },
                                            contentDescription = null,
                                            tint = when (notif.type) {
                                                NotificationType.ROOM_RELEASED -> LiveNeonGreen
                                                NotificationType.MATCH_REMINDER -> AmberGold
                                                NotificationType.TOURNAMENT_NEW -> CrimsonLight
                                                NotificationType.COMMUNITY_ALERT -> BlueShield
                                            },
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = notif.title,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = notif.timestamp,
                                                fontSize = 10.sp,
                                                color = TextMuted
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = notif.message,
                                            fontSize = 11.sp,
                                            color = TextSecondary,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HelpDialog(
    strings: Strings,
    onDismiss: () -> Unit,
    onReportSubmitted: () -> Unit
) {
    var showReportForm by remember { mutableStateOf(false) }
    var reportPlayerUid by remember { mutableStateOf("") }
    var reportReason by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp))
                .testTag("help_dialog"),
            color = CyberSurface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.helpAndRules,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_help_button")
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close", tint = TextPrimary)
                    }
                }

                HorizontalDivider(color = CyberCardBorder, modifier = Modifier.padding(vertical = 8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Fair Play Pledge
                    Surface(
                        color = CrimsonDark.copy(alpha = 0.25f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonLight),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Filled.Security, contentDescription = null, tint = CrimsonLight)
                                Text(strings.communityGuidelines, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = strings.fairPlayPledge,
                                fontSize = 11.sp,
                                color = TextSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }

                    // Community Links
                    CyberCard(backgroundColor = CyberDark) {
                        Text("Connect with Karnataka Community", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AmberGold)
                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = { /* simulated link */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.joinWhatsApp, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Button(
                            onClick = { /* simulated link */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5865F2), contentColor = Color.White),
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.HeadsetMic, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.joinDiscord, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // FAQs Accordion
                    Text(strings.faqs, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextPrimary)

                    FaqItem(
                        question = "Are entry fees or deposits required?",
                        answer = "No. ELITE BATTLES is completely free for all community players. There are no wallets, entry fees, or real money gambling involved."
                    )

                    FaqItem(
                        question = "When do I get my Custom Room ID & Password?",
                        answer = "Room credentials are automatically released 15 minutes prior to match schedule under 'My Rooms' tab for all registered team captains."
                    )

                    FaqItem(
                        question = "Are PC / Emulators or iPads allowed?",
                        answer = "Strictly NO. Only standard Android & iOS smartphones are permitted. Emulator detection triggers instant disqualified standing."
                    )

                    FaqItem(
                        question = "How do I submit match results as Captain?",
                        answer = "Take a clear screenshot of the end-game Booyah screen displaying team placement and kill counts, and submit it in our Discord/WhatsApp results channel."
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Report Cheater Form toggle
                    OutlinedButton(
                        onClick = { showReportForm = !showReportForm },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonLight),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonLight),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.fillMaxWidth().testTag("toggle_report_form_button")
                    ) {
                        Icon(Icons.Filled.ReportProblem, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.reportCheater, fontSize = 12.sp)
                    }

                    if (showReportForm) {
                        Surface(
                            color = CyberDark,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("Anti-Cheat Incident Report", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AmberGold)
                                OutlinedTextField(
                                    value = reportPlayerUid,
                                    onValueChange = { reportPlayerUid = it },
                                    label = { Text("Suspect Free Fire UID / IGN") },
                                    colors = esportsTextFieldColors(),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = reportReason,
                                    onValueChange = { reportReason = it },
                                    label = { Text("Reason (e.g. Wallhack, Auto-aim, Emulator)") },
                                    colors = esportsTextFieldColors(),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Button(
                                    onClick = {
                                        showReportForm = false
                                        onReportSubmitted()
                                    },
                                    enabled = reportPlayerUid.isNotBlank(),
                                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonRed),
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Submit Fair Play Report")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FaqItem(
    question: String,
    answer: String
) {
    var expanded by remember { mutableStateOf(false) }
    Surface(
        color = CyberDark,
        shape = RoundedCornerShape(6.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder.copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = question,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = answer,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
