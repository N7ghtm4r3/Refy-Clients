package com.tecknobit.refy.ui.screens.collections.data

import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.data.RefyUser.RefyUserImpl
import com.tecknobit.refycore.COLLECTION_COLOR_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LinksCollection(
    override val id: String,
    override val owner: RefyUserImpl,
    override val title: String,
    override val description: String,
    override val date: Long,
    @SerialName(COLLECTION_COLOR_KEY)
    val color: String,
    val links: List<RefyLinkImpl>,
    val teams: List<Team> = emptyList()
) : RefyItem {

    fun isShared(): Boolean {
        return teams.isNotEmpty()
    }

}
