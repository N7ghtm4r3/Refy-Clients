package com.tecknobit.refy.ui.shared.data

import com.tecknobit.refy.ui.shared.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.RefyUser.RefyUserImpl
import com.tecknobit.refycore.COLLECTION_COLOR_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `LinksCollection` data class represent a collection of links
 *
 * @property id The identifier of the collection
 * @property owner The owner of the collection
 * @property title The title of the collection
 * @property description The description of the collection
 * @property date The date when the collection has been inserted in the system
 * @property color The reference of the collection
 * @property links The links shared with collection
 * @property teams The teams where the collections is shared
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see RefyItem
 */
@Serializable
data class LinksCollection(
    override val id: String,
    override val owner: RefyUserImpl,
    override val title: String,
    override val description: String,
    override val date: Long,
    @SerialName(COLLECTION_COLOR_KEY)
    val color: String,
    val links: List<RefyLinkImpl> = emptyList(),
    val teams: List<Team> = emptyList()
) : RefyItem {

    /**
     * Method to check whether the collection is shared with any team
     *
     * @return whether the collection is shared with any team as [Boolean]
     */
    fun isShared(): Boolean {
        return teams.isNotEmpty()
    }

}
