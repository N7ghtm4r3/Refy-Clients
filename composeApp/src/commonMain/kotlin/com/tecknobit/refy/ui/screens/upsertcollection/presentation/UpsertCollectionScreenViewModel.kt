package com.tecknobit.refy.ui.screens.upsertcollection.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.utilities.toHex
import com.tecknobit.equinoxcore.network.Requester.Companion.sendRequest
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTitleValid
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

class UpsertCollectionScreenViewModel(
    private val collectionId: String?
) : UpsertScreenViewModel<LinksCollection>(
    itemId = collectionId
) {

    lateinit var color: MutableState<Color>

    lateinit var collectionTitle: MutableState<String>

    lateinit var collectionTitleError: MutableState<Boolean>

    val collectionLinks = mutableStateListOf<RefyLinkImpl>()

    override fun retrieveItem() {
        if (collectionId == null)
            return
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    getCollection(
                        collectionId = collectionId
                    )
                },
                onSuccess = {
                    _item.value = Json.decodeFromJsonElement(it.toResponseData())
                },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

    override fun validForm(): Boolean {
        if (!isTitleValid(collectionTitle.value)) {
            collectionTitleError.value = true
            return false
        }
        return super.validForm()
    }

    override fun insert(
        onInsert: () -> Unit
    ) {
        viewModelScope.launch {
            requester.sendRequest(
                request = {
                    createCollection(
                        color = color.value.toHex(),
                        title = collectionTitle.value,
                        description = itemDescription.value,
                        links = collectionLinks.map { collection -> collection.id }
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
                    editCollection(
                        collectionId = collectionId!!,
                        color = color.value.toHex(),
                        title = collectionTitle.value,
                        description = itemDescription.value,
                        links = collectionLinks.map { collection -> collection.id }
                    )
                },
                onSuccess = { onUpdate() },
                onFailure = { showSnackbarMessage(it) }
            )
        }
    }

}