package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import kotlinx.coroutines.CoroutineScope

interface CollectionsManager {

    val requestsScope: CoroutineScope

    fun attachLinks(
        collection: LinksCollection,
        links: List<RefyLinkImpl>,
        afterAttached: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        refreshAfterLinksAttached()
        afterAttached()
    }

    fun refreshAfterLinksAttached()

    fun shareWithTeams(
        collection: LinksCollection,
        teams: List<Team>,
        afterShared: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        refreshAfterTeamsAttached()
        afterShared()
    }

    fun refreshAfterTeamsAttached()

    fun deleteCollection(
        collection: LinksCollection,
        onDelete: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onDelete()
    }

}