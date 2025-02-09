package com.tecknobit.refy.ui.shared.data

import com.tecknobit.equinoxcore.helpers.PROFILE_PIC_KEY
import com.tecknobit.refycore.TAG_NAME_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface RefyUser {
    val id: String
    val name: String
    val surname: String
    val email: String
    val profilePic: String
    val tagName: String

    @Serializable
    data class RefyUserImpl(
        override val id: String,
        override val name: String,
        override val surname: String,
        override val email: String,
        @SerialName(PROFILE_PIC_KEY)
        override val profilePic: String,
        @SerialName(TAG_NAME_KEY)
        override val tagName: String
    ) : RefyUser

    fun completeName(): String {
        return "$name $surname"
    }

}