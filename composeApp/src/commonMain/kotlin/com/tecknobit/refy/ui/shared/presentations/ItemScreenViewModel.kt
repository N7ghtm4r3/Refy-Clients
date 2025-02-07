package com.tecknobit.refy.ui.shared.presentations

import androidx.compose.material3.SnackbarHostState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.ui.shared.data.RefyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Structure
abstract class ItemScreenViewModel<I : RefyItem>(
    private val itemId: String
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    protected val _item = MutableStateFlow<I?>(
        value = null
    )
    val item: StateFlow<I?> = _item

    abstract fun retrieveItem()

}