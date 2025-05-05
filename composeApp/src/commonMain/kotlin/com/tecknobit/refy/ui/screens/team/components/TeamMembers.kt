@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.team.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonRemove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.ui.components.TeamMemberListItem
import com.tecknobit.refy.ui.screens.team.presentation.TeamScreenViewModel
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.teams.data.TeamMember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.team_members

/**
 * Custom component used to display the members joined in the team
 *
 * @param viewModel The support viewmodel for the screen
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param team The team to display
 */
@Composable
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
                    val iAmAnAuthorizedMember = team.iAmAnAdmin() && member.isNotMe() &&
                            team.owner.id != member.id
                    TeamMemberListItem(
                        iAmAnAuthorizedMember = iAmAnAuthorizedMember,
                        member = member,
                        onRoleSelected = { expanded, roleState ->
                            viewModel.changeMemberRole(
                                member = member,
                                role = roleState.value,
                                onChange = {
                                    expanded.value = false
                                }
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
            }
        }
    }
}