package com.tecknobit.refy.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.teams.presentation.TeamsScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.CollectionsManager
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.delete_collection
import refy.composeapp.generated.resources.delete_collection_message
import refy.composeapp.generated.resources.delete_link
import refy.composeapp.generated.resources.delete_link_message
import refy.composeapp.generated.resources.delete_team
import refy.composeapp.generated.resources.delete_team_message

/**
 * `titleStyle` the style to apply to the title of the [EquinoxAlertDialog]
 */
val titleStyle = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 20.sp
)

@Composable
@NonRestartableComposable
fun DeleteLink(
    show: MutableState<Boolean>,
    viewModel: LinksScreenViewModel,
    link: RefyLinkImpl
) {
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        show = show,
        viewModel = viewModel,
        title = Res.string.delete_link,
        titleStyle = titleStyle,
        text = Res.string.delete_link_message,
        confirmAction = {
            viewModel.deleteLink(
                link = link,
                onDelete = {
                    show.value = false
                }
            )
        }
    )
}

@Composable
@NonRestartableComposable
fun DeleteCollection(
    show: MutableState<Boolean>,
    collectionsManager: CollectionsManager,
    collection: LinksCollection,
    onDelete: () -> Unit
) {
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        show = show,
        title = Res.string.delete_collection,
        titleStyle = titleStyle,
        text = Res.string.delete_collection_message,
        confirmAction = {
            collectionsManager.deleteCollection(
                collection = collection,
                onDelete = onDelete
            )
        }
    )
}

@Composable
@NonRestartableComposable
fun DeleteTeam(
    show: MutableState<Boolean>,
    viewModel: TeamsScreenViewModel,
    team: Team,
    onDelete: () -> Unit
) {
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        show = show,
        title = Res.string.delete_team,
        titleStyle = titleStyle,
        text = Res.string.delete_team_message,
        confirmAction = {
            viewModel.deleteTeam(
                team = team,
                onDelete = onDelete
            )
        }
    )
}