package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface CollectionsManager {

    val requestsScope: CoroutineScope

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

    fun refreshAfterLinksAttached()

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
                    refreshAfterTeamsAttached()
                    afterShared()
                },
                onFailure = {}
            )
        }
    }

    fun refreshAfterTeamsAttached()

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