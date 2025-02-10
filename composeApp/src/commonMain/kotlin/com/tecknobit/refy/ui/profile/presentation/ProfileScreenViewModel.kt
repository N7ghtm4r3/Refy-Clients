package com.tecknobit.refy.ui.profile.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.network.EquinoxRequester
import com.tecknobit.equinoxcompose.viewmodels.EquinoxProfileViewModel
import com.tecknobit.refy.localUser

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
     * `tagNameError` whether the [tagName] field is not valid
     */
    lateinit var tagNameError: MutableState<Boolean>

}