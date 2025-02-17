package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * The `CollectionsManager` interface is used to manage the collections data list and the operations
 * such links attachments, or teams sharing
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface CollectionsManager {

    /**
     *`requestsScope` the [CoroutineScope] used to make the requests to the backend
     */
    val requestsScope: CoroutineScope

    /**
     * Method to request to attach links to the [collection]
     *
     * @param collection The collection where attach the links
     * @param links The links to attach
     * @param afterAttached The action to execute after the attachment of the links
     */
    fun attachLinks(
        collection: LinksCollection,
        links: List<RefyLinkImpl>,
        afterAttached: () -> Unit
    ) {
        requestsScope.launch {
            requester.sendRequest(
                request = {
                    attachLinksWithCollection(
                        collection = collection,
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
     * Method to request to share the [collection] with teams
     *
     * @param collection The collection to share
     * @param teams The teams where share the collection
     * @param afterShared The action to execute after shared the collection
     */
    fun shareWithTeams(
        collection: LinksCollection,
        teams: List<Team>,
        afterShared: () -> Unit
    ) {
        requestsScope.launch {
            requester.sendRequest(
                request = {
                    shareCollectionWithTeams(
                        collection = collection,
                        teams = teams.map { link -> link.id }
                    )
                },
                onSuccess = {
                    refreshAfterTeamsSharing()
                    afterShared()
                },
                onFailure = {}
            )
        }
    }

    /**
     * Method to refresh the data after the collection has been shared with teams
     */
    fun refreshAfterTeamsSharing()

    /**
     * Method to delete the collection
     *
     * @param collection The collection to delete
     * @param onDelete The action to execute when the collection has been deleted
     */
    fun deleteCollection(
        collection: LinksCollection,
        onDelete: () -> Unit
    ) {
        requestsScope.launch {
            requester.sendRequest(
                request = {
                    deleteCollection(
                        collection = collection
                    )
                },
                onSuccess = { onDelete() },
                onFailure = {}
            )
        }
    }

}