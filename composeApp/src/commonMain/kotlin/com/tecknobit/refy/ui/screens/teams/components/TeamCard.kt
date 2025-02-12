@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.tecknobit.refy.ui.screens.teams.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.refy.TEAM_SCREEN
import com.tecknobit.refy.UPSERT_TEAM_SCREEN
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.AttachItemButton
import com.tecknobit.refy.ui.components.AttachTeam
import com.tecknobit.refy.ui.components.DeleteItemButton
import com.tecknobit.refy.ui.components.DeleteTeam
import com.tecknobit.refy.ui.components.ExpandCardButton
import com.tecknobit.refy.ui.components.ItemCardDetails
import com.tecknobit.refy.ui.components.TeamLogo
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.teams.data.TeamMember.Companion.toColor
import com.tecknobit.refy.ui.screens.teams.presentation.TeamsScreenViewModel
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.team_created_on

@Composable
@NonRestartableComposable
fun TeamCard(
    viewModel: TeamsScreenViewModel,
    team: Team
) {
    val expanded = remember { mutableStateOf(false) }
    val descriptionLines = remember { mutableIntStateOf(0) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = {
                    navigator.navigate("$TEAM_SCREEN/${team.id}/${team.title}")
                },
                onLongClick = if (team.iAmTheOwner()) {
                    {
                        navigator.navigate("$UPSERT_TEAM_SCREEN/${team.id}")
                    }
                } else
                    null
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TeamLogo(
                    team = team,
                    size = 45.dp
                )
                val role = team.findMyRole()
                Text(
                    text = role.name,
                    color = role.toColor(),
                    fontSize = 14.sp
                )
            }
            ItemCardDetails(
                modifier = Modifier
                    .padding(
                        bottom = 5.dp
                    ),
                expanded = expanded,
                item = team,
                info = Res.string.team_created_on,
                descriptionLines = descriptionLines
            )
            TeamBottomBar(
                expanded = expanded,
                viewModel = viewModel,
                team = team,
                descriptionLines = descriptionLines
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun TeamBottomBar(
    expanded: MutableState<Boolean>,
    viewModel: TeamsScreenViewModel,
    team: Team,
    descriptionLines: MutableState<Int>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExpandCardButton(
            descriptionLines = descriptionLines,
            expanded = expanded
        )
        AttachItemButton(
            attachItemContent = { state, scope ->
                AttachTeam(
                    state = state,
                    scope = scope,
                    teamsManager = viewModel,
                    team = team
                )
            }
        )
        DeleteItemButton(
            modifier = Modifier
                .weight(1f),
            item = team,
            deleteContent = { delete ->
                DeleteTeam(
                    show = delete,
                    teamsManager = viewModel,
                    team = team,
                    onDelete = {
                        delete.value = false
                        viewModel.refresh()
                    }
                )
            }
        )
    }
}