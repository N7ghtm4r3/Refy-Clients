package com.tecknobit.refy.ui.screens.auth.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcompose.viewmodels.EquinoxAuthViewModel
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refy.HOME_SCREEN
import com.tecknobit.refy.localUser
import com.tecknobit.refy.navigator
import com.tecknobit.refycore.TAG_NAME_KEY
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTagNameValid
import kotlinx.serialization.json.JsonObject

class AuthScreenViewModel : EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = object : EquinoxRequester(
        host = "",
        connectionErrorMessage = ""
    ) {

    }, // TODO: USE THE REAL requester,
    localUser = localUser
) {

    /**
     * `tagName` the tag name of the user
     */
    lateinit var tagName: MutableState<String>

    /**
     * `tagNameError` whether the [tagName] field is not valid
     */
    lateinit var tagNameError: MutableState<Boolean>

    override fun signUpFormIsValid(): Boolean {
        val validation = super.signUpFormIsValid()
        if (!validation)
            return false
        if (!isTagNameValid(tagName.value)) {
            tagNameError.value = true
            return false
        }
        return true
    }

    @CustomParametersOrder(order = [TAG_NAME_KEY])
    override fun getSignUpCustomParameters(): Array<out Any?> {
        return arrayOf(tagName)
    }

    @RequiresSuperCall
    @CustomParametersOrder(order = [TAG_NAME_KEY])
    override fun launchApp(
        response: JsonObject,
        name: String,
        surname: String,
        language: String,
        vararg custom: Any?
    ) {
        super.launchApp(response, name, surname, language, custom)
        navigator.navigate(HOME_SCREEN)
    }

}