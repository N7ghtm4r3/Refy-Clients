package com.tecknobit.refy.ui.screens.teams.data

import com.tecknobit.refy.localUser
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.data.RefyUser.RefyUserImpl
import com.tecknobit.refycore.LOGO_PIC_KEY
import com.tecknobit.refycore.enums.TeamRole
import com.tecknobit.refycore.enums.TeamRole.ADMIN
import com.tecknobit.refycore.enums.TeamRole.VIEWER
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `Team` data class represent a team
 *
 * @property id The identifier of the team
 * @property owner The owner of the team
 * @property title The title of the team
 * @property description The description of the team
 * @property date The date when the collection has been inserted in the system
 * @property members The members joined in the team
 * @property links The links shared with team
 * @property collections The collections shared with team
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see RefyItem
 */
@Serializable
data class Team(
    override val id: String,
    override val owner: RefyUserImpl,
    override val title: String,
    override val description: String,
    override val date: Long,
    @SerialName(LOGO_PIC_KEY)
    private val _logoPic: String,
    val members: List<TeamMember>,
    val links: List<RefyLinkImpl> = emptyList(),
    val collections: List<LinksCollection> = emptyList()
) : RefyItem {

    /**
     *`logoPic` the logo pic of the team
     */
    val logoPic: String
        get() = localUser.hostAddress + "/" + _logoPic

    /**
     * Method to find the role of the [localUser] inside the team
     *
     * @return the role of the [localUser] as [TeamRole]
     */
    fun findMyRole(): TeamRole {
        members.forEach { member ->
            if (member.id == localUser.userId)
                return member.role
        }
        return VIEWER
    }

    /**
     * Method to check whether the [localUser] is an [TeamRole.ADMIN] of the team
     *
     * @return whether the [localUser] is an [TeamRole.ADMIN] of the team as [Boolean]
     */
    fun iAmAnAdmin(): Boolean {
        if (iAmTheOwner())
            return true
        val role = findMyRole()
        return role == ADMIN
    }

}
