package com.tecknobit.refy.ui.screens.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.refy.localUser
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTagNameValid

class ProfileScreenViewModel : EquinoxProfileViewModel(
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
     * `newTagName` the value of the new tag name to set
     */
    lateinit var newTagName: MutableState<String>

    /**
     * `newTagNameError` whether the [newTagName] field is not valid
     */
    lateinit var newTagNameError: MutableState<Boolean>

    fun changeTagName(
        onSuccess: () -> Unit
    ) {
        if (!isTagNameValid(newTagName.value)) {
            newTagNameError.value = true
            return
        }
        // TODO: TO MAKE REQUEST THEN
        localUser.tagName = newTagName.value
        onSuccess.invoke()
    }

}