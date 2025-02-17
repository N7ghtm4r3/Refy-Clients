package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The `TeamsManager` interface is used to manage the teams data list and the operations
 * such sharing or team deletion
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface TeamsManager {

    /**
     *`requestsScope` the [CoroutineScope] used to make the requests to the backend
     */
    val requestsScope: CoroutineScope

    /**
     * Method to request to attach links to the [team]
     *
     * @param team The team where attach the links
     * @param links The links to attach
     * @param afterAttached The action to execute after the attachment of the links
     */
    fun attachLinks(
        team: Team,
        links: List<RefyLinkImpl>,
        afterAttached: () -> Unit
    ) {
        requestsScope.launch {
            requester.sendRequest(
                request = {
                    shareLinksWithTeam(
                        team = team,
                        links = links.map { link -> link.id }
                    )
                },
                onSuccess = {
                    refreshAfterLinksAttached()
                    afterAttached()
                },
                onFailure = {}
            )
        }
    }

    /**
     * Method to refresh the data after the links have been attached
     */
    fun refreshAfterLinksAttached()

    /**
     * Method to request to attach collections to the [team]
     *
     * @param team The team where attach the links
     * @param collections The collections to attach
     * @param afterAttached The action to execute after the attachment of the collections
     */
    fun attachCollections(
        team: Team,
        collections: List<LinksCollection>,
        afterAttached: () -> Unit
    ) {
        requestsScope.launch {
            requester.sendRequest(
                request = {
                    shareCollectionsWithTeam(
                        team = team,
                        collections = collections.map { collection -> collection.id }
                    )
                },
                onSuccess = {
                    refreshAfterCollectionsAttached()
                    afterAttached()
                },
                onFailure = {}
            )
        }
    }

    /**
     * Method to refresh the data after the collections have been attached
     */
    fun refreshAfterCollectionsAttached()

    /**
     * Method to delete the team
     *
     * @param team The team to delete
     * @param onDelete The action to execute when the team has been deleted
     */
    fun deleteTeam(
        team: Team,
        onDelete: () -> Unit
    ) {
        requestsScope.launch {
            requester.sendRequest(
                request = {
                    deleteTeam(
                        team = team
                    )
                },
                onSuccess = { onDelete() },
                onFailure = {}
            )
        }
    }

}