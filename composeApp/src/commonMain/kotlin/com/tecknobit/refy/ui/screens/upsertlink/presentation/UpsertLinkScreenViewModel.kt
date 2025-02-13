package com.tecknobit.refy.ui.screens.upsertlink.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.refycore.helpers.RefyInputsValidator.isLinkResourceValid
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class UpsertLinkScreenViewModel(
    private val linkId: String?
) : UpsertScreenViewModel<RefyLinkImpl>(
    itemId = linkId
) {

    lateinit var reference: MutableState<String>

    lateinit var referenceError: MutableState<Boolean>

    override fun retrieveItem() {
        if (linkId == null)
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getLink(
                        linkId = linkId
                    )
                },
                onSuccess = {
                    _item.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    override fun insert(
        onInsert: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    createLink(
                        referenceLink = reference.value,
                        description = itemDescription.value
                    )
                },
                onSuccess = { onInsert() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    override fun update(
        onUpdate: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    editLink(
                        linkId = linkId!!,
                        referenceLink = reference.value,
                        description = itemDescription.value
                    )
                },
                onSuccess = { onUpdate() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    @RequiresSuperCall
    override fun validForm(): Boolean {
        if (!isLinkResourceValid(reference.value)) {
            referenceError.value = true
            return false
        }
        return super.validForm()
    }

}