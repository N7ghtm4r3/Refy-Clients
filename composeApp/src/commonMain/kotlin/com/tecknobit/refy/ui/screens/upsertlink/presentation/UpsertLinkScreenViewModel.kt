package com.tecknobit.refy.ui.screens.upsertlink.presentation

import androidx.compose.runtime.MutableState
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel

class UpsertLinkScreenViewModel(
    linkId: String?
) : UpsertScreenViewModel<RefyLinkImpl>(
    itemId = linkId
) {

    lateinit var reference: MutableState<String>

    lateinit var referenceError: MutableState<Boolean>

    override fun retrieveItem() {
        // TODO: MAKE THE REQUEST THEN
    }

    override fun insert(
        onInsert: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onInsert()
    }

    override fun add(
        onAdd: () -> Unit
    ) {
        // TODO: MAKE THE REQUEST THEN
        onAdd()
    }

}