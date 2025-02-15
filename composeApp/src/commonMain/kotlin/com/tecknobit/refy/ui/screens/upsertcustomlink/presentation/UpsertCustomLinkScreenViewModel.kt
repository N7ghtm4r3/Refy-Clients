package com.tecknobit.refy.ui.screens.upsertcustomlink.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.session.setServerOfflineValue
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

class UpsertCustomLinkScreenViewModel(
    private val linkId: String?
) : UpsertScreenViewModel<CustomRefyLink>(
    itemId = linkId
) {

    lateinit var linkName: MutableState<String>

    lateinit var linkNameError: MutableState<Boolean>

    lateinit var uniqueAccess: MutableState<Boolean>

    lateinit var expirationTime: MutableState<ExpiredTime>

    lateinit var authFields: SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>

    lateinit var resources: SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>

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

    private fun SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>.toPayloadMap(): Map<String, String> {
        val mutableMap = mutableMapOf<String, String>()
        this.forEach { row ->
            mutableMap[row.first.value] = row.second.value
        }
        return mutableMap
    }

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

    private fun SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>.isValid(): Boolean {
        this.forEach { row ->
            if (row.first.value.isBlank() || row.second.value.isBlank())
                return false
        }
        return true
    }

}