package com.tecknobit.refy.ui.screens.upsertcollection.presentation

import androidx.compose.runtime.MutableState
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel

class UpsertCollectionScreenViewModel(
    collectionId: String?
) : UpsertScreenViewModel<LinksCollection>(
    itemId = collectionId
) {

    lateinit var collectionTitle: MutableState<String>

    lateinit var collectionTitleError: MutableState<Boolean>

    override fun retrieveItem() {
    }

    override fun insert(
        onInsert: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onInsert()
    }

    override fun update(
        onUpdate: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onUpdate()
    }

}