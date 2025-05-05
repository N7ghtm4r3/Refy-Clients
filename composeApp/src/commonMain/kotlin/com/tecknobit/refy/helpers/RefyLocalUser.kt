package com.tecknobit.refy.helpers

import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refycore.TAG_NAME_KEY
import kotlinx.serialization.json.JsonObject

/**
 * The `RefyLocalUser` class is useful to represent a user in the client application
 *
 * @author N7ghtm4r3 - Tecknobit
 */
class RefyLocalUser : EquinoxLocalUser(
    localStoragePath = "Refy"
) {

    /**
     * `tagName` the tag name of the user
     */
    var tagName: String = ""
        set(value) {
            if (field != value) {
                setPreference(
                    key = TAG_NAME_KEY,
                    value = value
                )
                field = value
            }
        }

    /**
     * Method to init the local user session
     */
    @RequiresSuperCall
    override fun initLocalUser() {
        super.initLocalUser()
        tagName = getNullSafePreference(TAG_NAME_KEY)
    }

    /**
     * Method to insert and initialize a new local user.
     *
     * @param hostAddress The host address with which the user communicates
     * @param name The name of the user
     * @param surname The surname of the user
     * @param email The email address of the user
     * @param password The password of the user
     * @param language The preferred language of the user
     * @param response The payload response received from an authentication request
     * @param custom Custom parameters added during the customization of the equinox user
     */
    @RequiresSuperCall
    @CustomParametersOrder(order = [TAG_NAME_KEY])
    override fun insertNewUser(
        hostAddress: String,
        name: String,
        surname: String,
        email: String,
        password: String,
        language: String,
        response: JsonObject,
        vararg custom: Any?
    ) {
        super.insertNewUser(
            hostAddress,
            name,
            surname,
            email,
            password,
            language,
            response,
            *custom
        )
        tagName = custom.extractsCustomValue(
            itemPosition = 0
        )
    }

}