package com.tecknobit.refy.ui.screens.teams.data

import com.tecknobit.equinoxcore.helpers.PROFILE_PIC_KEY
import com.tecknobit.refy.ui.shared.data.RefyUser
import com.tecknobit.refycore.MEMBER_IDENTIFIER_KEY
import com.tecknobit.refycore.TAG_NAME_KEY
import com.tecknobit.refycore.TEAM_ROLE_KEY
import com.tecknobit.refycore.enums.TeamRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMember(
    @SerialName(MEMBER_IDENTIFIER_KEY)
    override val id: String,
    override val name: String,
    override val surname: String,
    override val email: String,
    @SerialName(PROFILE_PIC_KEY)
    override val profilePic: String,
    @SerialName(TAG_NAME_KEY)
    override val tagName: String,
    @SerialName(TEAM_ROLE_KEY)
    val role: TeamRole
) : RefyUser