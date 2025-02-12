package com.tecknobit.refy.ui.screens.upsertteam.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refy.ui.components.EmptyMembers
import com.tecknobit.refy.ui.components.FirstPageProgressIndicator
import com.tecknobit.refy.ui.components.NewPageProgressIndicator
import com.tecknobit.refy.ui.components.TeamLogo
import com.tecknobit.refy.ui.components.TeamMemberListItem
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.upsertteam.presentation.UpsertTeamScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.UpsertScreen
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTitleValid
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.create
import refy.composeapp.generated.resources.create_team
import refy.composeapp.generated.resources.logo
import refy.composeapp.generated.resources.name_not_valid
import refy.composeapp.generated.resources.team_members
import refy.composeapp.generated.resources.team_name
import refy.composeapp.generated.resources.update_team

class UpsertTeamScreen(
    teamId: String?
) : UpsertScreen<Team, UpsertTeamScreenViewModel>(
    itemId = teamId,
    insertTitle = Res.string.create_team,
    updateTitle = Res.string.update_team,
    insertButtonText = Res.string.create,
    viewModel = UpsertTeamScreenViewModel(
        teamId = teamId
    )
) {

    @Composable
    @NonRestartableComposable
    override fun ColumnScope.UpsertForm() {
        LogoPicker()
        TeamNameSection()
        ItemDescriptionSection()
        TeamMembersSection()
        UpsertButton()
    }

    @Composable
    @NonRestartableComposable
    private fun LogoPicker() {
        val launcher = rememberFilePickerLauncher(
            type = PickerType.Image,
            mode = PickerMode.Single
        ) { logoAsset ->
            viewModel.pickTeamLogo(
                logoAsset = logoAsset
            )
        }
        SectionTitle(
            title = Res.string.logo
        )
        TeamLogo(
            modifier = Modifier
                .border(
                    color = if (viewModel.logoPicError.value)
                        MaterialTheme.colorScheme.error
                    else
                        Color.Transparent,
                    width = 2.dp,
                    shape = CircleShape
                ),
            teamLogo = viewModel.logoPic.value,
            size = 85.dp,
            onClick = { launcher.launch() }
        )
    }

    @Composable
    @NonRestartableComposable
    private fun TeamNameSection() {
        SectionTitle(
            title = Res.string.team_name
        )
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            shape = inputFieldShape,
            value = viewModel.teamName,
            isError = viewModel.teamNameError,
            allowsBlankSpaces = false,
            validator = { isTitleValid(it) },
            errorText = Res.string.name_not_valid,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
    }

    @Composable
    @NonRestartableComposable
    private fun TeamMembersSection() {
        SectionTitle(
            title = Res.string.team_members
        )
        PaginatedLazyColumn(
            modifier = Modifier
                .heightIn(
                    max = 250.dp
                )
                .animateContentSize(),
            paginationState = viewModel.potentialMembers,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewPageProgressIndicator() },
            firstPageEmptyIndicator = { EmptyMembers() }
        ) {
            items(
                items = viewModel.potentialMembers.allItems!!,
                key = { member -> member.id }
            ) { member ->
                val memberAlreadyAdded = viewModel.teamMembers.find { addedMember ->
                    member.id == addedMember.id
                }
                val alreadyAdded = memberAlreadyAdded != null
                memberAlreadyAdded?.let {
                    member.role = memberAlreadyAdded.role
                }
                TeamMemberListItem(
                    iAmAnAuthorizedMember = member.isNotMe(),
                    member = member,
                    onRoleSelected = { expanded, roleState ->
                        expanded.value = false
                        member.role = roleState.value
                    },
                    trailingContent = {
                        var added by remember { mutableStateOf(alreadyAdded) }
                        Checkbox(
                            checked = added,
                            onCheckedChange = {
                                added = it
                                if (added)
                                    viewModel.teamMembers.remove(member)
                                else
                                    viewModel.teamMembers.add(member)
                            }
                        )
                    }
                )
            }
        }
    }

    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    override fun CollectStates() {
        super.CollectStates()
        viewModel.logoPicError = remember { mutableStateOf(false) }
        viewModel.teamNameError = remember { mutableStateOf(false) }
    }

    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    override fun CollectStatesAfterLoading() {
        super.CollectStatesAfterLoading()
        viewModel.logoPic = remember {
            mutableStateOf(
                if (isUpdating)
                    item.value!!.logoPic
                else
                    ""
            )
        }
        viewModel.teamName = remember {
            mutableStateOf(
                if (isUpdating)
                    item.value!!.title
                else
                    ""
            )
        }
        viewModel.teamMembers = remember { mutableStateListOf() }
        if (isUpdating) {
            LaunchedEffect(Unit) {
                viewModel.teamMembers.addAll(item.value!!.members)
            }
        }
    }

}