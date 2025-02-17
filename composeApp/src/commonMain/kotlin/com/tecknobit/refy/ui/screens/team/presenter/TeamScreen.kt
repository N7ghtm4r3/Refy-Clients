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
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
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
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
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

/**
 * The [TeamScreen] class is useful to display the information of a [Team]
 *
 * @param teamId The identifier of the team
 * @param teamName The name of the team
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see RefyScreen
 * @see ItemScreen
 */
class TeamScreen(
    teamId: String,
    teamName: String
) : ItemScreen<Team, TeamScreenViewModel>(
    viewModel = TeamScreenViewModel(
        teamId = teamId,
        teamName = teamName
    ),
    name = teamName
) {

    /**
     *`memberSheetState` the state used to manage the [TeamMembers] component
     */
    private lateinit var memberSheetState: SheetState

    /**
     *`scope` the scope used to manage the [TeamMembers] component
     */
    private lateinit var scope: CoroutineScope

    /**
     * Custom trailing content to display in the [TopBar] component
     */
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

    /**
     * Custom [ExtendedFloatingActionButton] used to edit the item where needed
     */
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

    /**
     * The content of the [SubTitleSection]
     */
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
                        viewModel = viewModel
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

    /**
     * Custom section used to display the items with a row layout
     */
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

    /**
     * The section about the team information such logo and action buttons to manage that team
     */
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
                        viewModel = viewModel
                    )
                }
                if (item.value!!.iAmAnAdmin()) {
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

    /**
     * The section where are displayed the collections shared with the team
     */
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

    /**
     * Custom card used to display with a properly card the information of a link attached to the [item]
     *
     * @param link The link to display
     */
    @Composable
    @NonRestartableComposable
    override fun ItemRelatedLinkCard(
        link: RefyLinkImpl
    ) {
        TeamLinkCard(
            viewModel = viewModel,
            link = link
        )
    }

    /**
     * Content to allow the user to attach items to the current [item]
     *
     * @param state The state useful to manage the visibility of the [ModalBottomSheet]
     * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
     */
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

    /**
     * Content displayed when the user request to delete the [item]
     *
     * @param delete The state used to manage the visibility of this component
     */
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

    /**
     * The representative icon of the upsert action
     */
    override fun upsertIcon(): ImageVector {
        return Icons.Default.Edit
    }

    /**
     * The representative text of the upsert action
     */
    override fun upsertText(): StringResource {
        return Res.string.edit
    }

    /**
     * The action to execute to update or insert an item
     */
    override fun upsertAction() {
        navigator.navigate("$UPSERT_TEAM_SCREEN/${item.value!!.id}")
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
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