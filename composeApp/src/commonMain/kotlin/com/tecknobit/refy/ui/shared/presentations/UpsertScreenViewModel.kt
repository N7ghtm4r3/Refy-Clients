package com.tecknobit.refy.ui.shared.presentations

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.helpers.KReviewer
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refycore.helpers.RefyInputsValidator.isDescriptionValid
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

    fun upsert(
        onUpsertAction: () -> Unit
    ) {
        if (!validForm())
            return
        val kReviewer = KReviewer()
        if (itemId == null) {
            insert {
                kReviewer.reviewInApp {
                    onUpsertAction()
                }
            }
        } else {
            update {
                kReviewer.reviewInApp {
                    onUpsertAction()
                }
            }
        }
    }

    protected abstract fun insert(
        onInsert: () -> Unit
    )

    protected abstract fun update(
        onUpdate: () -> Unit
    )

    @RequiresSuperCall
    protected open fun validForm(): Boolean {
        return if (!isDescriptionValid(itemDescription.value)) {
            itemDescriptionError.value = true
            return false
        } else
            true
    }

}