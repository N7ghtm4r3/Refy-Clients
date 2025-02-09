@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.team.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.localUser
import com.tecknobit.refy.ui.components.ProfilePic
import com.tecknobit.refy.ui.screens.team.presentation.TeamScreenViewModel
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.teams.data.TeamMember
import com.tecknobit.refy.ui.screens.teams.data.TeamMember.Companion.toColor
import com.tecknobit.refycore.enums.TeamRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.team_members
import kotlin.random.Random

@Composable
@NonRestartableComposable
fun TeamMembers(
    viewModel: TeamScreenViewModel,
    state: SheetState,
    scope: CoroutineScope,
    team: Team
) {
    if (state.isVisible) {
        val members = remember { mutableStateListOf<TeamMember>() }
        LaunchedEffect(Unit) {
            members.addAll(team.members)
        }
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        all = 10.dp
                    ),
                text = stringResource(Res.string.team_members),
                fontSize = 20.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider()
            LazyColumn(
                modifier = Modifier
                    .animateContentSize()
            ) {
                items(
                    items = members,
                    key = { member -> member.id }
                ) { member ->
                    TeamMember(
                        viewModel = viewModel,
                        team = team,
                        members = members,
                        member = member
                    )
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun TeamMember(
    viewModel: TeamScreenViewModel,
    team: Team,
    members: SnapshotStateList<TeamMember>,
    member: TeamMember
) {
    val iAmAnAuthorizedMember =
        team.iAmAnAdmin() && localUser.userId != member.id || Random.nextBoolean() // TODO: REMOVE THE RANDOM
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        overlineContent = {
            Column {
                val role = remember { mutableStateOf(member.role) }
                val expandsRolesMenu = remember { mutableStateOf(false) }
                Text(
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                size = 5.dp
                            )
                        )
                        .clickable(
                            enabled = iAmAnAuthorizedMember
                        ) {
                            expandsRolesMenu.value = true
                        }
                        .padding(
                            horizontal = 4.dp
                        ),
                    text = role.value.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = role.value.toColor()
                )
                RolesMenu(
                    expanded = expandsRolesMenu,
                    viewModel = viewModel,
                    member = member,
                    currentRole = role
                )
            }
        },
        leadingContent = {
            ProfilePic(
                profilePic = member.profilePic,
                size = 50.dp
            )
        },
        headlineContent = {
            Text(
                text = member.completeName(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        supportingContent = {
            Text(
                text = member.email,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            AnimatedVisibility(
                visible = iAmAnAuthorizedMember
            ) {
                IconButton(
                    onClick = {
                        viewModel.removeMember(
                            member = member,
                            onRemove = {
                                members.remove(member)
                            }
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonRemove,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@Composable
@NonRestartableComposable
private fun RolesMenu(
    expanded: MutableState<Boolean>,
    viewModel: TeamScreenViewModel,
    member: TeamMember,
    currentRole: MutableState<TeamRole>
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = {
            expanded.value = false
        }
    ) {
        TeamRole.entries.forEach { role ->
            DropdownMenuItem(
                onClick = {
                    viewModel.changeMemberRole(
                        member = member,
                        role = role,
                        onChange = {
                            expanded.value = false
                            currentRole.value = role
                        }
                    )
                },
                text = {
                    Text(
                        text = role.name,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = role.toColor()
                    )
                }
            )
        }
    }
}