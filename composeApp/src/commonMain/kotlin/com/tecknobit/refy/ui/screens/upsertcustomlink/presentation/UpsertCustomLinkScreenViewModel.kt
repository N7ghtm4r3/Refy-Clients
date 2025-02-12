package com.tecknobit.refy.ui.screens.upsertcustomlink.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refy.ui.screens.customs.data.CustomRefyLink
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.refycore.enums.ExpiredTime
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTitleValid

class UpsertCustomLinkScreenViewModel(
    private val linkId: String?
) : UpsertScreenViewModel<CustomRefyLink>(
    itemId = linkId
) {

    lateinit var linkName: MutableState<String>

    lateinit var linkNameError: MutableState<Boolean>

    lateinit var uniqueAccess: MutableState<Boolean>

    lateinit var expirationTime: MutableState<ExpiredTime>

    lateinit var authFields: SnapshotStateList<Pair<String, String>>

    lateinit var resources: SnapshotStateList<Pair<String, String>>

    override fun retrieveItem() {
        if (linkId == null)
            return
        // TODO: MAKE THE REQUEST THEN
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

    @RequiresSuperCall
    override fun validForm(): Boolean {
        if (!isTitleValid(linkName.value)) {
            linkNameError.value = true
            return false
        }
        val validity = super.validForm()
        return validity

    }

    private fun SnapshotStateList<Pair<String, String>>.validForm(): Boolean {
        this.forEach { row ->
            if (row.first.isBlank() || row.second.isBlank())
                return false
        }
        return true
    }

}