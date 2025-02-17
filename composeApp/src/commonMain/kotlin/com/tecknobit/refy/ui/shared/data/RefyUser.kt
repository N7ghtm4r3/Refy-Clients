package com.tecknobit.refy.ui.shared.data

import com.tecknobit.equinoxcore.helpers.PROFILE_PIC_KEY
import com.tecknobit.refycore.TAG_NAME_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The `RefyUser` interface is useful to give the basic details about a Refy's user
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface RefyUser {

    /**
     *`id` the identifier of the user
     */
    val id: String

    /**
     *`name` the name of the user
     */
    val name: String

    /**
     *`surname` the surname of the user
     */
    val surname: String

    /**
     *`email` the email of the user
     */
    val email: String

    /**
     *`profilePic` the profile pic of the user
     */
    val profilePic: String

    /**
     *`tagName` the tagName of the user
     */
    val tagName: String

    /**
     * The `RefyUserImpl` class is the implementation of the [RefyUser] interface
     *
     * @property id The identifier of the user
     * @property name The name of the user
     * @property surname The surname of the user
     * @property email The email of the user
     * @property profilePic The profile pic of the user
     * @property tagName The tagName of the user
     *
     * @author N7ghtm4r3 - Tecknobit
     *
     * @see RefyUser
     */
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

    /**
     * Method used to assemble the complete name of the current user
     *
     * @return the complete name of the current user as [String]
     */
    fun completeName(): String {
        return "$name $surname"
    }

}