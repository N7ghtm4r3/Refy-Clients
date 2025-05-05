package com.tecknobit.refy.ui.screens.upsertcollection.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import com.tecknobit.equinoxcompose.utilities.toHex
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTitleValid
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/**
 * The `UpsertCollectionScreenViewModel` class is the support class used by the
 * [com.tecknobit.refy.ui.screens.upsertcollection.presenter.UpsertCollectionScreen]
 *
 * @param collectionId The identifier of the collection to update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see UpsertScreenViewModel
 *
 */
class UpsertCollectionScreenViewModel(
    private val collectionId: String?
) : UpsertScreenViewModel<LinksCollection>(
    itemId = collectionId
) {

    /**
     *`color` the color of the collection
     */
    lateinit var color: MutableState<Color>

    /**
     * `collectionTitle` the title of the collection
     */
    lateinit var collectionTitle: MutableState<String>

    /**
     * `collectionTitleError` whether the [collectionTitle] field is not valid
     */
    lateinit var collectionTitleError: MutableState<Boolean>

    /**
     *`collectionLinks` the links shared in the collection
     */
    val collectionLinks = mutableStateListOf<RefyLinkImpl>()

    /**
     * Method to retrieve the information of the item to display
     */
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

    /**
     * Method to check the validity of the form data to insert or update an item
     *
     * @return the validity of the form as [Boolean]
     */
    override fun validForm(): Boolean {
        if (!isTitleValid(collectionTitle.value)) {
            collectionTitleError.value = true
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