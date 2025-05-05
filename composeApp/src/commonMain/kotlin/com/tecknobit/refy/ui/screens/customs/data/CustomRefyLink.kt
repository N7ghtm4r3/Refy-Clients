package com.tecknobit.refy.ui.screens.customs.data

import com.tecknobit.equinoxcore.network.EquinoxBaseEndpointsSet.Companion.BASE_EQUINOX_ENDPOINT
import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.refy.localUser
import com.tecknobit.refy.ui.shared.data.LinksCollection
import com.tecknobit.refy.ui.shared.data.RefyLink
import com.tecknobit.refy.ui.shared.data.RefyUser
import com.tecknobit.refy.ui.shared.data.Team
import com.tecknobit.refycore.EXPIRED_TIME_KEY
import com.tecknobit.refycore.PREVIEW_TOKEN_KEY
import com.tecknobit.refycore.REFERENCE_LINK_KEY
import com.tecknobit.refycore.UNIQUE_ACCESS_KEY
import com.tecknobit.refycore.enums.ExpiredTime
import com.tecknobit.refycore.enums.ExpiredTime.NO_EXPIRATION
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `CustomRefyLink` represent a custom link used to share, for example, informations
 *
 * @property id The identifier of the link
 * @property owner The owner of the link
 * @property title The title of the link
 * @property description The description of the link
 * @property date The date when the link has been inserted in the system
 * @property reference The reference of the link
 * @property teams The teams where the links is shared (always empty)
 * @property collections The collections where the links is shared (always empty)
 * @property uniqueAccess Whether the link is deleted after the first view
 * @property expiredTime The time when the link is considered expire and no more available
 * @property fields The fields used to protect the resource from unauthorised accesses
 * @property resources The resources shared by the link
 * @property previewToken The token used to enter and view the link in preview mode
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see RefyLink
 */
@Serializable
data class CustomRefyLink(
    override val id: String,
    override val owner: RefyUser.RefyUserImpl,
    override val title: String,
    override val description: String,
    override val date: Long,
    @SerialName(REFERENCE_LINK_KEY)
    private val _reference: String,
    override val teams: List<Team> = emptyList(),
    override val collections: List<LinksCollection> = emptyList(),
    @SerialName(UNIQUE_ACCESS_KEY)
    val uniqueAccess: Boolean,
    @SerialName(EXPIRED_TIME_KEY)
    val expiredTime: ExpiredTime = NO_EXPIRATION,
    val fields: Map<String, String> = emptyMap(),
    val resources: Map<String, String>,
    @SerialName(PREVIEW_TOKEN_KEY)
    val previewToken: String,
) : RefyLink {

    /**
     *`reference` the reference of the link
     */
    override val reference: String = localUser.hostAddress + BASE_EQUINOX_ENDPOINT +
            _reference.replaceFirstChar { "" }

    /**
     * Method to get the expiration date of the link
     *
     * @return the expiration date of the link as [Long]
     */
    fun expirationDate(): Long {
        return date + expiredTime.gap
    }

    /**
     * Method to check whether the link is expired
     *
     * @return whether the link is expired as [Boolean]
     */
    fun isExpired(): Boolean {
        if (expiredTime == NO_EXPIRATION)
            return false
        return TimeFormatter.currentTimestamp() >= expirationDate()
    }

    /**
     * Method to get the reference to access and view the link in preview mode
     *
     * @return the preview reference as [String]
     */
    fun previewReference(): String {
        return "$reference?$PREVIEW_TOKEN_KEY=$previewToken"
    }

}