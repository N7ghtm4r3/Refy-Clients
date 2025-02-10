package com.tecknobit.refy.ui.screens.customs.data

import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.refy.localUser
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.data.RefyUser
import com.tecknobit.refycore.EXPIRED_TIME_KEY
import com.tecknobit.refycore.LINK_IDENTIFIER_KEY
import com.tecknobit.refycore.PREVIEW_TOKEN_KEY
import com.tecknobit.refycore.REFERENCE_LINK_KEY
import com.tecknobit.refycore.UNIQUE_ACCESS_KEY
import com.tecknobit.refycore.enums.ExpiredTime
import com.tecknobit.refycore.enums.ExpiredTime.NO_EXPIRATION
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomRefyLink(
    @SerialName(LINK_IDENTIFIER_KEY)
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
    val resources: Map<String, String>,
    val fields: Map<String, String> = emptyMap(),
    @SerialName(PREVIEW_TOKEN_KEY)
    val previewToken: String
) : RefyLink {

    override val reference: String = localUser.hostAddress + _reference

    fun expirationDate(): Long {
        return date + expiredTime.gap
    }

    fun isExpired(): Boolean {
        if (expiredTime == NO_EXPIRATION)
            return false
        return TimeFormatter.currentTimestamp() >= expirationDate()
    }

    fun previewReference(): String {
        return "$reference?$PREVIEW_TOKEN_KEY=$previewToken"
    }

}