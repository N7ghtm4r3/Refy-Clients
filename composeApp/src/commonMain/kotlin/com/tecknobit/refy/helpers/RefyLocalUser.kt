package com.tecknobit.refy.helpers

import com.tecknobit.equinoxcompose.session.EquinoxLocalUser
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refycore.TAG_NAME_KEY
import kotlinx.serialization.json.JsonObject

class RefyLocalUser : EquinoxLocalUser(
    localStoragePath = "Refy"
) {

    /**
     * `tagName` the tag name of the user
     */
    var tagName: String = ""
        set(value) {
            //if (field != value) { // TODO: FIX THE ISSUE HERE OR REMOVE JUST THE COMMENT AND SEE THE PRODUCTION WEB BINARY
                setPreference(
                    key = TAG_NAME_KEY,
                    value = value
                )
                field = value
            //}
        }

    @RequiresSuperCall
    override fun initLocalUser() {
        super.initLocalUser()
        tagName = getPreference(TAG_NAME_KEY)
    }

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
        tagName = custom[0].toString()
    }

}