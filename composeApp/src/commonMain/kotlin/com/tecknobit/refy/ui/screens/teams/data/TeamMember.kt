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

/**
 * The `TeamMember` represent a member of a [Team]
 *
 * @property id The identifier of the member
 * @property name The name of the member
 * @property surname The surname of the member
 * @property email The email of the member
 * @property profilePic The profile pic of the member
 * @property tagName The tagName of the member
 * @property role The role of the member in that team
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see RefyUser
 */
@Serializable
data class TeamMember(
    override val id: String,
    override val name: String,
    override val surname: String,
    override val email: String,
    @SerialName(PROFILE_PIC_KEY)
    private val _profilePic: String,
    @SerialName(TAG_NAME_KEY)
    override val tagName: String,
    @SerialName(TEAM_ROLE_KEY)
    var role: TeamRole
) : RefyUser {

    /**
     *`profilePic` the profile pic of the user
     */
    override val profilePic: String
        get() = localUser.hostAddress + "/" + _profilePic

    companion object {

        /**
         * Method to get a representative color for the role of the member
         *
         * @return the color of the role as [Color]
         */
        @Composable
        fun TeamRole.toColor(): Color {
            return when (this) {
                ADMIN -> MaterialTheme.colorScheme.error
                VIEWER -> Color.Unspecified
            }
        }

    }

    /**
     * Method to check whether the [localUser] is not the current member
     *
     * @return whether the [localUser] is not the current member as [Boolean]
     */
    fun isNotMe(): Boolean {
        return localUser.userId != id
    }

}