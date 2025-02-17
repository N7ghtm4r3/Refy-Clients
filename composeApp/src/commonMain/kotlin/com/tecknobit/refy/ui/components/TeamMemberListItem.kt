package com.tecknobit.refy.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.screens.teams.data.TeamMember
import com.tecknobit.refy.ui.screens.teams.data.TeamMember.Companion.toColor
import com.tecknobit.refycore.enums.TeamRole

/**
 * Custom [ListItem] used to display the information of a [TeamMember]
 *
 * @param iAmAnAuthorizedMember Whether the [member] is an [TeamRole.ADMIN]
 * @param member The member to display
 * @param onRoleSelected The action to execute when the role has been selected
 * @param trailingContent Custom trailing content to display
 */
@Composable
@NonRestartableComposable
fun TeamMemberListItem(
    iAmAnAuthorizedMember: Boolean,
    member: TeamMember,
    onRoleSelected: (MutableState<Boolean>, MutableState<TeamRole>) -> Unit,
    trailingContent: @Composable () -> Unit
) {
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
                    roleState = role,
                    onRoleSelected = onRoleSelected
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
        trailingContent = trailingContent
    )
}

/**
 * The menu used to display and to select the role for a member
 *
 * @param expanded Whether the menu is visible
 * @param roleState The state used to display the role selected
 * @param onRoleSelected The action to execute when the role has been selected
 */
@Composable
@NonRestartableComposable
private fun RolesMenu(
    expanded: MutableState<Boolean>,
    roleState: MutableState<TeamRole>,
    onRoleSelected: (MutableState<Boolean>, MutableState<TeamRole>) -> Unit
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
                    roleState.value = role
                    onRoleSelected.invoke(expanded, roleState)
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