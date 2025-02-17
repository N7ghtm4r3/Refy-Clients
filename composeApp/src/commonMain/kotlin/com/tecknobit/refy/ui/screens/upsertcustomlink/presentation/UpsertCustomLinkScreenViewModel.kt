package com.tecknobit.refy.ui.screens.upsertcustomlink.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.customs.data.CustomRefyLink
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.refycore.enums.ExpiredTime
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTitleValid
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.auth_form_not_valid
import refy.composeapp.generated.resources.resources_not_valid

/**
 * The `UpsertCustomLinkScreenViewModel` class is the support class used by the
 * [com.tecknobit.refy.ui.screens.upsertcustomlink.presenter.UpsertCustomLinkScreen]
 *
 * @param linkId The identifier of the link to update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see UpsertScreenViewModel
 *
 */
class UpsertCustomLinkScreenViewModel(
    private val linkId: String?
) : UpsertScreenViewModel<CustomRefyLink>(
    itemId = linkId
) {

    /**
     * `linkName` the name of the link
     */
    lateinit var linkName: MutableState<String>

    /**
     * `linkNameError` whether the [linkName] field is not valid
     */
    lateinit var linkNameError: MutableState<Boolean>

    /**
     * `uniqueAccess` the state container whether the link has the unique access
     */
    lateinit var uniqueAccess: MutableState<Boolean>

    /**
     * `expirationTime` the state container the expiration time value
     */
    lateinit var expirationTime: MutableState<ExpiredTime>

    /**
     * `authFields` the fields used to protect the resources shared by the link
     */
    lateinit var authFields: SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>

    /**
     * `resources` the resources shared by the link
     */
    lateinit var resources: SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>

    /**
     * Method to retrieve the information of the item to display
     */
    override fun retrieveItem() {
        if (linkId == null)
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getCustomLink(
                        linkId = linkId
                    )
                },
                onSuccess = {
                    setServerOfflineValue(false)
                    _item.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = { showSnackbarMessage(it) },
                onConnectionError = { setServerOfflineValue(true) }
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
        if (!isTitleValid(linkName.value)) {
            linkNameError.value = true
            return false
        }
        val validity = super.validForm()
        if (!validity)
            return false
        if (!authFields.isValid()) {
            showSnackbarMessage(
                message = Res.string.auth_form_not_valid
            )
            return false
        }
        if (!resources.isValid() || resources.isEmpty()) {
            showSnackbarMessage(
                message = Res.string.resources_not_valid
            )
            return false
        }
        return true
    }

    /**
     * Method to check the validity of a form value
     *
     * @return the validity of a form value as [Boolean]
     */
    private fun SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>.isValid(): Boolean {
        this.forEach { row ->
            if (row.first.value.isBlank() || row.second.value.isBlank())
                return false
        }
        return true
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
                    createCustomLink(
                        title = linkName.value,
                        description = itemDescription.value,
                        resources = resources.toPayloadMap(),
                        fields = authFields.toPayloadMap(),
                        hasUniqueAccess = uniqueAccess.value,
                        expiredTime = expirationTime.value
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
                    editCustomLink(
                        linkId = linkId!!,
                        title = linkName.value,
                        description = itemDescription.value,
                        resources = resources.toPayloadMap(),
                        fields = authFields.toPayloadMap(),
                        hasUniqueAccess = uniqueAccess.value,
                        expiredTime = expirationTime.value
                    )
                },
                onSuccess = { onUpdate() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    /**
     * Method to convert as [SnapshotStateList] to a [Map] for the requests payload
     *
     * @return the converted [SnapshotStateList] as [Map] of [String] and [String]
     */
    private fun SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>.toPayloadMap(): Map<String, String> {
        val mutableMap = mutableMapOf<String, String>()
        this.forEach { row ->
            mutableMap[row.first.value] = row.second.value
        }
        return mutableMap
    }

}