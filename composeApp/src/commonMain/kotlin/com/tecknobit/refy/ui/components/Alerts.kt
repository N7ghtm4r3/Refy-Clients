package com.tecknobit.refy.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxAlertDialog
import com.tecknobit.refy.SPLASHSCREEN
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink
import com.tecknobit.refy.ui.screens.profile.presentation.ProfileScreenViewModel
import com.tecknobit.refy.ui.screens.team.presentation.TeamScreenViewModel
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.presentations.BaseLinksScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.CollectionsManager
import com.tecknobit.refy.ui.shared.presentations.TeamsManager
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.account_deletion
import refy.composeapp.generated.resources.delete_collection
import refy.composeapp.generated.resources.delete_collection_message
import refy.composeapp.generated.resources.delete_link
import refy.composeapp.generated.resources.delete_link_message
import refy.composeapp.generated.resources.delete_message
import refy.composeapp.generated.resources.delete_team
import refy.composeapp.generated.resources.delete_team_message
import refy.composeapp.generated.resources.leave_team
import refy.composeapp.generated.resources.leave_team_message
import refy.composeapp.generated.resources.logout
import refy.composeapp.generated.resources.logout_message

/**
 * `titleStyle` the style to apply to the title of the [EquinoxAlertDialog]
 */
val titleStyle = TextStyle(
    fontFamily = displayFontFamily,
    fontSize = 20.sp
)

/**
 * Custom [EquinoxAlertDialog] used to warn the user about the deletion of a [RefyLink]
 *
 * @param show Whether the dialog is shown
 * @param viewModel The support viewmodel for the screen
 * @param link The link to delete
 */
@Composable
@NonRestartableComposable
fun <T : RefyLink> DeleteLink(
    show: MutableState<Boolean>,
    viewModel: BaseLinksScreenViewModel<T>,
    link: T
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

/**
 * Custom [EquinoxAlertDialog] used to warn the user about the deletion of a [LinksCollection]
 *
 * @param show Whether the dialog is shown
 * @param collectionsManager The manager of the collections list data
 * @param collection The collection to delete
 * @param onDelete The action to execute when the collection has been deleted
 */
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

/**
 * Custom [EquinoxAlertDialog] used to warn the user about the deletion of a [Team]
 *
 * @param show Whether the dialog is shown
 * @param teamsManager The manager of the teams list data
 * @param team The team to delete
 * @param onDelete The action to execute when the team has been deleted
 */
@Composable
@NonRestartableComposable
fun DeleteTeam(
    show: MutableState<Boolean>,
    teamsManager: TeamsManager,
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
            teamsManager.deleteTeam(
                team = team,
                onDelete = onDelete
            )
        }
    )
}

/**
 * Alert to warn about the leaving from a team action
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
@NonRestartableComposable
fun LeaveTeam(
    show: MutableState<Boolean>,
    viewModel: TeamScreenViewModel
) {
    EquinoxAlertDialog(
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        show = show,
        title = Res.string.leave_team,
        titleStyle = titleStyle,
        text = Res.string.leave_team_message,
        confirmAction = {
            viewModel.leaveTeam {
                show.value = false
                navigator.goBack()
            }
        }
    )
}

/**
 * Alert to warn about the logout action
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
@NonRestartableComposable
fun Logout(
    viewModel: ProfileScreenViewModel,
    show: MutableState<Boolean>
) {
    EquinoxAlertDialog(
        icon = Icons.AutoMirrored.Filled.Logout,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.logout,
        titleStyle = titleStyle,
        text = Res.string.logout_message,
        confirmAction = {
            viewModel.clearSession {
                show.value = false
                navigator.navigate(SPLASHSCREEN)
            }
        }
    )
}

/**
 * Alert to warn about the account deletion
 *
 * @param viewModel The support viewmodel for the screen
 * @param show Whether the alert is shown
 */
@Composable
@NonRestartableComposable
fun DeleteAccount(
    viewModel: ProfileScreenViewModel,
    show: MutableState<Boolean>
) {
    EquinoxAlertDialog(
        icon = Icons.Default.Delete,
        modifier = Modifier
            .widthIn(
                max = 400.dp
            ),
        viewModel = viewModel,
        show = show,
        title = Res.string.account_deletion,
        titleStyle = titleStyle,
        text = Res.string.delete_message,
        confirmAction = {
            viewModel.deleteAccount(
                onDelete = {
                    show.value = false
                    navigator.navigate(SPLASHSCREEN)
                }
            )
        }
    )
}