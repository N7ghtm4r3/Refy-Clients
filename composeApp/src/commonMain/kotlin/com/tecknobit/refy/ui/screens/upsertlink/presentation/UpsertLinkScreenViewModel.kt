package com.tecknobit.refy.ui.screens.upsertlink.presentation

import androidx.compose.runtime.MutableState
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.refycore.helpers.RefyInputsValidator.isLinkResourceValid
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The `UpsertLinkScreenViewModel` class is the support class used by the [com.tecknobit.refy.ui.screens.upsertlink.presenter.UpsertLinkScreen]
 *
 * @param itemId The identifier of the item to update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see UpsertScreenViewModel
 *
 */
class UpsertLinkScreenViewModel(
    private val linkId: String?
) : UpsertScreenViewModel<RefyLinkImpl>(
    itemId = linkId
) {

    /**
     *`reference` the reference of the link
     */
    lateinit var reference: MutableState<String>

    /**
     * `referenceError` whether the [reference] field is not valid
     */
    lateinit var referenceError: MutableState<Boolean>

    /**
     * Method to retrieve the information of the item to display
     */
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

    /**
     * Method to check the validity of the form data to insert or update an item
     *
     * @return the validity of the form as [Boolean]
     */
    @RequiresSuperCall
    override fun validForm(): Boolean {
        if (!isLinkResourceValid(reference.value)) {
            referenceError.value = true
            return false
        }
        return super.validForm()
    }

    /**
     * Method to insert a new item
     *
     * @param onInsert The action to execute after the item inserted
     */
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

    /**
     * Method to update an existing item
     *
     * @param onUpdate The action to execute after the item updated
     */
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

}