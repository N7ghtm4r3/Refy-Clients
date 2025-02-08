package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.ui.shared.data.RefyItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Structure
abstract class ItemScreenViewModel<I : RefyItem>(
    private val itemId: String
) : RefyScreenViewModel() {

    protected val _item = MutableStateFlow<I?>(
        value = null
    )
    val item: StateFlow<I?> = _item

    abstract fun retrieveItem()

}