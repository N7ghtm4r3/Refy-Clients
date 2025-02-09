package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import kotlinx.coroutines.CoroutineScope

interface TeamsManager {

    val requestsScope: CoroutineScope

    fun attachLinks(
        team: Team,
        links: List<RefyLinkImpl>,
        afterAttached: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        refreshAfterLinksAttached()
        afterAttached()
    }

    fun refreshAfterLinksAttached()

    fun attachCollections(
        team: Team,
        collections: List<LinksCollection>,
        afterAttached: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        refreshAfterCollectionsAttached()
        afterAttached()
    }

    fun refreshAfterCollectionsAttached()

    fun deleteTeam(
        team: Team,
        onDelete: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onDelete()
    }

}