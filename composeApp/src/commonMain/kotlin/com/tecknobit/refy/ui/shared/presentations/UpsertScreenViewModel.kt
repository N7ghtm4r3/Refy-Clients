package com.tecknobit.refy.ui.shared.presentations

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.ui.shared.data.RefyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Structure
abstract class UpsertScreenViewModel<I : RefyItem>(
    private val itemId: String?
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    protected val _item = MutableStateFlow<I?>(
        value = null
    )
    val item: StateFlow<I?> = _item

    lateinit var itemDescription: MutableState<String>

    lateinit var itemDescriptionError: MutableState<Boolean>

    abstract fun retrieveItem()

    abstract fun insert(
        onInsert: () -> Unit
    )

    abstract fun add(
        onAdd: () -> Unit
    )

}