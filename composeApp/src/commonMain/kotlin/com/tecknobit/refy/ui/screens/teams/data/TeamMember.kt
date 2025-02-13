package com.tecknobit.refy.ui.screens.teams.data

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecknobit.equinoxcore.helpers.PROFILE_PIC_KEY
import com.tecknobit.refy.localUser
import com.tecknobit.refy.ui.shared.data.RefyUser
import com.tecknobit.refycore.TAG_NAME_KEY
import com.tecknobit.refycore.TEAM_ROLE_KEY
import com.tecknobit.refycore.enums.TeamRole
import com.tecknobit.refycore.enums.TeamRole.ADMIN
import com.tecknobit.refycore.enums.TeamRole.VIEWER
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TeamMember(
    override val id: String,
    override val name: String,
    override val surname: String,
    override val email: String,
    @SerialName(PROFILE_PIC_KEY)
    override val profilePic: String,
    @SerialName(TAG_NAME_KEY)
    override val tagName: String,
    @SerialName(TEAM_ROLE_KEY)
    var role: TeamRole
) : RefyUser {

    companion object {

        @Composable
        fun TeamRole.toColor(): Color {
            return when (this) {
                ADMIN -> MaterialTheme.colorScheme.error
                VIEWER -> Color.Unspecified
            }
        }

    }

    fun isNotMe(): Boolean {
        return localUser.userId != id
    }

}