package com.tecknobit.refy.ui.screens.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.refy.localUser
import com.tecknobit.refy.requester
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTagNameValid
import kotlinx.coroutines.launch

class ProfileScreenViewModel : EquinoxProfileViewModel(
    snackbarHostState = SnackbarHostState(),
    requester = requester,
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
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    changeTagName(
                        tagName = newTagName.value
                    )
                },
                onSuccess = {
                    localUser.tagName = newTagName.value
                    onSuccess.invoke()
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}