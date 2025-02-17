package com.tecknobit.refy.ui.screens.auth.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxAuthViewModel
import com.tecknobit.equinoxcore.annotations.CustomParametersOrder
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.json.treatsAsString
import com.tecknobit.refy.HOME_SCREEN
import com.tecknobit.refy.localUser
import com.tecknobit.refy.navigator
import com.tecknobit.refy.requester
import com.tecknobit.refycore.TAG_NAME_KEY
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTagNameValid
import kotlinx.serialization.json.JsonObject

/**
 * The `AuthScreenViewModel` class is the support class used to execute the authentication requests
 * to the backend
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see EquinoxAuthViewModel
 */
class AuthScreenViewModel : EquinoxAuthViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
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

    /**
     * Method to validate the inputs for the [signUp] request
     *
     * @return whether the inputs are valid as [Boolean]
     */
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

    /**
     * Method to get the list of the custom parameters to use in the [signUp] request
     */
    @CustomParametersOrder(order = [TAG_NAME_KEY])
    override fun getSignUpCustomParameters(): Array<out Any?> {
        return arrayOf(tagName.value)
    }

    /**
     * Method to launch the application after the authentication request, will be instantiated with the user details
     * both the [requester] and the [localUser]
     *
     * @param response The response of the authentication request
     * @param name The name of the user
     * @param surname The surname of the user
     * @param language The language of the user
     * @param custom The custom parameters added in a customization of the equinox user
     */
    @RequiresSuperCall
    @CustomParametersOrder(order = [TAG_NAME_KEY])
    override fun launchApp(
        response: JsonObject,
        name: String,
        surname: String,
        language: String,
        vararg custom: Any?
    ) {
        val tagName = if (custom.isEmpty())
            response[TAG_NAME_KEY].treatsAsString()
        else
            custom[0]
        super.launchApp(response, name, surname, language, tagName)
        navigator.navigate(HOME_SCREEN)
    }

}