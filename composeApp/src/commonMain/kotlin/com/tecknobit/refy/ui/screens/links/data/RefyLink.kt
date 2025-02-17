package com.tecknobit.refy.ui.screens.links.data

import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.data.RefyUser
import com.tecknobit.refycore.REFERENCE_LINK_KEY
import com.tecknobit.refycore.THUMBNAIL_PREVIEW_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `RefyLink` interface is useful to give the basic details about a Refy's link
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see RefyItem
 */
interface RefyLink : RefyItem {

    /**
     *`reference` the reference of the link
     */
    val reference: String

    /**
     *`teams` the teams where the links is shared
     */
    val teams: List<Team>

    /**
     *`collections` the collections where the links is shared
     */
    val collections: List<LinksCollection>

    /**
     * The `RefyLinkImpl` class is the implementation of the [RefyLink] interface
     *
     * @property id The identifier of the link
     * @property owner The owner of the link
     * @property title The title of the link
     * @property description The description of the link
     * @property date The date when the link has been inserted in the system
     * @property reference The reference of the link
     * @property teams The teams where the links is shared
     * @property collections The collections where the links is shared
     * @property thumbnailPreview The **og:image** metadata
     *
     * @author N7ghtm4r3 - Tecknobit
     *
     * @see RefyLink
     */
    @Serializable
    data class RefyLinkImpl(
        override val id: String,
        override val owner: RefyUser.RefyUserImpl,
        override val title: String,
        override val description: String,
        override val date: Long,
        @SerialName(REFERENCE_LINK_KEY)
        override val reference: String,
        override val teams: List<Team> = emptyList(),
        override val collections: List<LinksCollection> = emptyList(),
        @SerialName(THUMBNAIL_PREVIEW_KEY)
        val thumbnailPreview: String? = null
    ) : RefyLink

}
