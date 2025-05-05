package com.tecknobit.refy.ui.screens.teams.presenter

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.refy.UPSERT_TEAM_SCREEN
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.EmptyTeams
import com.tecknobit.refy.ui.components.FirstPageProgressIndicator
import com.tecknobit.refy.ui.components.NewPageProgressIndicator
import com.tecknobit.refy.ui.screens.teams.components.TeamCard
import com.tecknobit.refy.ui.screens.teams.presentation.TeamsScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.ItemsScreen
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyVerticalStaggeredGrid
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.create
import refy.composeapp.generated.resources.teams

/**
 * The [TeamsScreen] class is useful to display the list of the teams where the user is a member
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see EquinoxScreen
 * @see RefyScreen
 * @see ItemsScreen
 */
class TeamsScreen : ItemsScreen<TeamsScreenViewModel>(
    title = Res.string.teams,
    viewModel = TeamsScreenViewModel()
) {

    /**
     * Custom component used to display the items list as grid
     */
    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    override fun ItemsGrid() {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PaginatedLazyVerticalStaggeredGrid(
                modifier = Modifier
                    // TODO: TO CHANGE
                    .widthIn(
                        max = 1280.dp
                    )
                    .animateContentSize(),
                paginationState = viewModel.teamsState,
                columns = StaggeredGridCells.Adaptive(
                    minSize = 325.dp
                ),
                verticalItemSpacing = 10.dp,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                firstPageProgressIndicator = { FirstPageProgressIndicator() },
                newPageProgressIndicator = { NewPageProgressIndicator() },
                firstPageEmptyIndicator = { EmptyTeams() }
            ) {
                items(
                    items = viewModel.teamsState.allItems!!,
                    key = { team -> team.id }
                ) { team ->
                    TeamCard(
                        viewModel = viewModel,
                        team = team
                    )
                }
            }
        }
    }

    /**
     * Custom component used to display the items list as custom [Column]
     */
    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    override fun ItemsList() {
        PaginatedLazyColumn(
            modifier = Modifier
                .animateContentSize(),
            paginationState = viewModel.teamsState,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            firstPageProgressIndicator = { FirstPageProgressIndicator() },
            newPageProgressIndicator = { NewPageProgressIndicator() },
            firstPageEmptyIndicator = { EmptyTeams() }
        ) {
            items(
                items = viewModel.teamsState.allItems!!,
                key = { team -> team.id }
            ) { team ->
                TeamCard(
                    viewModel = viewModel,
                    team = team
                )
            }
        }
    }

    /**
     * The representative icon of the upsert action
     */
    override fun upsertIcon(): ImageVector {
        return Icons.Default.GroupAdd
    }

    /**
     * The representative text of the upsert action
     */
    override fun upsertText(): StringResource {
        return Res.string.create
    }

    /**
     * The action to execute to update or insert an item
     */
    override fun upsertAction() {
        navigator.navigate(UPSERT_TEAM_SCREEN)
    }

}