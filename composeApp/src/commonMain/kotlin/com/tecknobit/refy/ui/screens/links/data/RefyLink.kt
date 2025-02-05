package com.tecknobit.refy.ui.screens.links.data

import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.data.RefyUser
import kotlinx.serialization.Serializable

interface RefyLink : RefyItem {
    val reference: String
    val teams: List<Team>
    val collections: List<LinksCollection>

    @Serializable
    data class RefyLinkImpl(
        override val owner: RefyUser.RefyUserImpl,
        override val title: String,
        override val description: String,
        override val reference: String,
        override val teams: List<Team>,
        override val collections: List<LinksCollection>
    ) : RefyLink

}
