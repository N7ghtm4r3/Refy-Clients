package com.tecknobit.refy.helpers

import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.helpers.THEME_KEY
import com.tecknobit.refy.RefyConfig.LOCAL_STORAGE_PATH
import com.tecknobit.refycore.TAG_NAME_KEY

/**
 * The `RefyLocalUser` class is useful to represent a user in the client application
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class RefyLocalUser : EquinoxLocalUser(
    localStoragePath = LOCAL_STORAGE_PATH,
    observableKeys = setOf(THEME_KEY)
) {

    /**
     * `tagName` the tag name of the user
     */
    var tagName: String = ""
        private set

    /**
     * Method to init the local user session
     */
    @RequiresSuperCall
    override fun initLocalUser() {
        super.initLocalUser()
        setNullSafePreference<String>(
            key = TAG_NAME_KEY,
            defPrefValue = "",
            prefInit = { tagName ->
                this.tagName = tagName
            }
        )
    }

    /**
     * Method used to insert a new user and save locally his/her properties
     *
     * @param hostAddress The host address with which the user communicates
     * @param userId The identifier of the user
     * @param userToken The token of the user
     * @param profilePic The profile picture of the user
     * @param name The name of the user
     * @param surname The surname of the user
     * @param email The email of the user
     * @param language The language of the user
     * @param custom The custom parameters added during the customization of the [EquinoxLocalUser]
     */
    @RequiresSuperCall
    @CustomParametersOrder(order = [TAG_NAME_KEY])
    override fun insertNewUser(
        hostAddress: String,
        userId: String,
        userToken: String,
        profilePic: String,
        name: String,
        surname: String,
        email: String,
        language: String,
        vararg custom: Any?,
    ) {
        super.insertNewUser(
            hostAddress,
            userId,
            userToken,
            profilePic,
            name,
            surname,
            email,
            language,
            custom
        )
        val tagNameRef: String = custom.extractsCustomValue(
            itemPosition = 0
        )
        initTagName(
            tagName = tagNameRef
        )
    }

    /**
     * Method to initialize the [tagName] property and locally save its value with the [savePreference] method
     *
     * @param tagName The tag name of the user
     *
     * @since 1.1.0
     */
    fun initTagName(
        tagName: String,
    ) {
        this.tagName = tagName
        savePreference(
            key = TAG_NAME_KEY,
            value = tagName
        )
    }

}