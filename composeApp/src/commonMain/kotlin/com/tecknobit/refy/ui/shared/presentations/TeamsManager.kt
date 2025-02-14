package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

interface TeamsManager {

    val requestsScope: CoroutineScope

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

    fun refreshAfterLinksAttached()

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

    fun refreshAfterCollectionsAttached()

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