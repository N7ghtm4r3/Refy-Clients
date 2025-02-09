@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.screens.team.presenter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.AttachTeam
import com.tecknobit.refy.ui.components.DeleteTeam
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.team.components.TeamCollectionCard
import com.tecknobit.refy.ui.screens.team.components.TeamLinkCard
import com.tecknobit.refy.ui.screens.team.presentation.TeamScreenViewModel
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.presenters.ItemScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyRow
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.edit

class TeamScreen(
    teamId: String,
    teamName: String
) : ItemScreen<Team, TeamScreenViewModel>(
    viewModel = TeamScreenViewModel(
        teamId = teamId
    ),
    itemName = teamName
) {

    @Composable
    @NonRestartableComposable
    override fun ColumnScope.RowItems() {
        CollectionsSection()
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
        // TODO: NAV TO EDIT
    }

}