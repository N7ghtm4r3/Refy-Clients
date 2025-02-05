package com.tecknobit.refy.ui.screens.links.data

import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.data.RefyUser
import com.tecknobit.refycore.LINK_IDENTIFIER_KEY
import com.tecknobit.refycore.THUMBNAIL_PREVIEW_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface RefyLink : RefyItem {
    val reference: String
    val teams: List<Team>
    val collections: List<LinksCollection>

    @Serializable
    data class RefyLinkImpl(
        @SerialName(LINK_IDENTIFIER_KEY)
        override val id: String,
        override val owner: RefyUser.RefyUserImpl,
        override val title: String,
        override val description: String,
        override val reference: String,
        override val teams: List<Team> = emptyList(),
        override val collections: List<LinksCollection> = emptyList(),
        @SerialName(THUMBNAIL_PREVIEW_KEY)
        val thumbnailPreview: String
    ) : RefyLink

}
