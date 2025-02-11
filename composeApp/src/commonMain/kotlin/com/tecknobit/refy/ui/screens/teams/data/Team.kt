package com.tecknobit.refy.ui.screens.teams.data

import com.tecknobit.refy.localUser
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.data.RefyUser.RefyUserImpl
import com.tecknobit.refycore.LOGO_PIC_KEY
import com.tecknobit.refycore.TEAM_IDENTIFIER_KEY
import com.tecknobit.refycore.enums.TeamRole
import com.tecknobit.refycore.enums.TeamRole.ADMIN
import com.tecknobit.refycore.enums.TeamRole.VIEWER
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Team(
    @SerialName(TEAM_IDENTIFIER_KEY)
    override val id: String,
    override val owner: RefyUserImpl,
    override val title: String,
    override val description: String,
    override val date: Long,
    @SerialName(LOGO_PIC_KEY)
    val logoPic: String,
    val members: List<TeamMember>,
    val links: List<RefyLinkImpl> = emptyList(),
    val collections: List<LinksCollection> = emptyList()
) : RefyItem {

    fun findMyRole(): TeamRole {
        members.forEach { member ->
            if (member.id == localUser.userId)
                return member.role
        }
        return VIEWER
    }

    fun iAmAnAdmin(): Boolean {
        val role = findMyRole()
        return role == ADMIN && false
    }

}
