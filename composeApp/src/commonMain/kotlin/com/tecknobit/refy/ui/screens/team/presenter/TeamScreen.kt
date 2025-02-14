@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.screens.team.presenter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refy.UPSERT_TEAM_SCREEN
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.AttachItemButton
import com.tecknobit.refy.ui.components.AttachTeam
import com.tecknobit.refy.ui.components.DeleteItemButton
import com.tecknobit.refy.ui.components.DeleteTeam
import com.tecknobit.refy.ui.components.LeaveTeam
import com.tecknobit.refy.ui.components.TeamLogo
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.team.components.TeamCollectionCard
import com.tecknobit.refy.ui.screens.team.components.TeamLinkCard
import com.tecknobit.refy.ui.screens.team.components.TeamMembers
import com.tecknobit.refy.ui.screens.team.presentation.TeamScreenViewModel
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.presenters.ItemScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyRow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collections
import refy.composeapp.generated.resources.delete
import refy.composeapp.generated.resources.edit
import refy.composeapp.generated.resources.leave_team
import refy.composeapp.generated.resources.share_with_the_team

class TeamScreen(
    teamId: String,
    teamName: String
) : ItemScreen<Team, TeamScreenViewModel>(
    viewModel = TeamScreenViewModel(
        teamId = teamId
    ),
    itemName = teamName
) {

    private lateinit var memberSheetState: SheetState

    private lateinit var scope: CoroutineScope

    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    override fun ColumnScope.TrailingContent() {
        awaitNullItemLoaded(
            itemToWait = item.value
        ) { team ->
            TeamLogo(
                team = team,
                size = 75.dp,
                onClick = {
                    scope.launch {
                        memberSheetState.show()
                    }
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    override fun ExtendedFAB() {
        awaitNullItemLoaded(
            itemToWait = item.value,
            extras = { team -> team.iAmTheOwner() }
        ) {
            super.ExtendedFAB()
        }
    }

    @Composable
    @NonRestartableComposable
    override fun SubTitleContent() {
        awaitNullItemLoaded(
            itemToWait = item.value
        ) { team ->
            Row(
                modifier = Modifier
                    .padding(
                        top = 5.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (team.iAmTheOwner()) {
                    IconButton(
                        modifier = Modifier
                            .size(32.dp),
                        onClick = { upsertAction() }
                    ) {
                        Icon(
                            imageVector = upsertIcon(),
                            contentDescription = null
                        )
                    }
                }
                if (team.iAmAnAdmin()) {
                    AttachItemButton { state, scope ->
                        AttachContent(
                            state = state,
                            scope = scope
                        )
                    }
                }
                if (!item.value!!.iAmTheOwner()) {
                    val leave = remember { mutableStateOf(false) }
                    IconButton(
                        modifier = Modifier
                            .size(32.dp),
                        onClick = { leave.value = true }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    LeaveTeam(
                        show = leave,
                        viewModel = viewModel,
                        team = item.value!!
                    )
                } else {
                    DeleteItemButton(
                        item = item.value!!,
                        deleteContent = { delete ->
                            DeleteItemContent(
                                delete = delete
                            )
                        }
                    )
                }
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun ColumnScope.RowItems() {
        ResponsiveContent(
            onExpandedSizeClass = {
                TeamInfoSection()
            },
            onMediumSizeClass = {
                TeamInfoSection()
            },
            onCompactSizeClass = {}
        )
        CollectionsSection()
        TeamMembers(
            viewModel = viewModel,
            state = memberSheetState,
            scope = scope,
            team = item.value!!
        )
    }

    @Composable
    @NonRestartableComposable
    private fun TeamInfoSection() {
        Row(
            modifier = Modifier
                .padding(
                    bottom = 10.dp
                ),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TeamLogo(
                team = item.value!!,
                size = 100.dp,
                onClick = {
                    scope.launch {
                        memberSheetState.show()
                    }
                }
            )
            Column {
                if (item.value!!.iAmTheOwner()) {
                    val delete = remember { mutableStateOf(false) }
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = contentColorFor(MaterialTheme.colorScheme.error)
                        ),
                        onClick = { delete.value = true }
                    ) {
                        Text(
                            text = stringResource(Res.string.delete)
                        )
                    }
                    DeleteItemContent(
                        delete = delete
                    )
                } else {
                    val leave = remember { mutableStateOf(false) }
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = contentColorFor(MaterialTheme.colorScheme.error)
                        ),
                        onClick = { leave.value = true }
                    ) {
                        Text(
                            text = stringResource(Res.string.leave_team)
                        )
                    }
                    LeaveTeam(
                        show = leave,
                        viewModel = viewModel,
                        team = item.value!!
                    )
                }
                val state = rememberModalBottomSheetState(
                    skipPartiallyExpanded = true
                )
                val scope = rememberCoroutineScope()
                Button(
                    onClick = {
                        scope.launch {
                            state.show()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.share_with_the_team)
                    )
                }
                AttachContent(
                    state = state,
                    scope = scope
                )
            }
        }
        AnimatedVisibility(
            visible = viewModel.teamCollections.allItems?.isNotEmpty() ?: false
        ) {
            SectionHeaderTitle(
                modifier = Modifier
                    .padding(
                        bottom = 10.dp
                    ),
                header = Res.string.collections
            )
        }
    }

    @Composable
    @NonRestartableComposable
    private fun CollectionsSection() {
        val iAmAnAdmin = item.value!!.iAmAnAdmin()
        PaginatedLazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            paginationState = viewModel.teamCollections,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = viewModel.teamCollections.allItems!!,
                key = { collection -> collection.id }
            ) { collection ->
                TeamCollectionCard(
                    viewModel = viewModel,
                    iAmAnAdmin = iAmAnAdmin,
                    collection = collection
                )
            }
        }
    }

    @Composable
    @NonRestartableComposable
    override fun ItemRelatedLinkCard(
        link: RefyLinkImpl
    ) {
        TeamLinkCard(
            viewModel = viewModel,
            team = item.value!!,
            link = link
        )
    }

    @Composable
    @NonRestartableComposable
    override fun AttachContent(
        state: SheetState,
        scope: CoroutineScope
    ) {
        AttachTeam(
            state = state,
            scope = scope,
            teamsManager = viewModel,
            team = item.value!!
        )
    }

    @Composable
    @NonRestartableComposable
    override fun DeleteItemContent(
        delete: MutableState<Boolean>
    ) {
        DeleteTeam(
            show = delete,
            teamsManager = viewModel,
            team = item.value!!,
            onDelete = {
                delete.value = false
                navigator.goBack()
            }
        )
    }

    override fun upsertIcon(): ImageVector {
        return Icons.Default.Edit
    }

    override fun upsertText(): StringResource {
        return Res.string.edit
    }

    override fun upsertAction() {
        navigator.navigate("$UPSERT_TEAM_SCREEN/${item.value!!.id}")
    }

    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    override fun CollectStates() {
        super.CollectStates()
        memberSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )
        scope = rememberCoroutineScope()
    }

}