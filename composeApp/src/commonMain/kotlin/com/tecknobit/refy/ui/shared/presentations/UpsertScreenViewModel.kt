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

/**
 * The `UpsertScreenViewModel` class is the support class used by the screens which insert new items
 * or updated the existing ones
 *
 * @param itemId The identifier of the item to update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 *
 * @param I The type of the item updated
 *
 */
@Structure
abstract class UpsertScreenViewModel<I : RefyItem>(
    private val itemId: String?
) : EquinoxViewModel(
    snackbarHostState = SnackbarHostState()
) {

    /**
     *`_item` the item to update
     */
    protected val _item = MutableStateFlow<I?>(
        value = null
    )
    val item: StateFlow<I?> = _item

    /**
     * `itemDescription` the description of the item
     */
    lateinit var itemDescription: MutableState<String>

    /**
     * `itemDescriptionError` whether the [itemDescription] field is not valid
     */
    lateinit var itemDescriptionError: MutableState<Boolean>

    /**
     * Method to retrieve the information of the item to display
     */
    abstract fun retrieveItem()

    /**
     * Method to insert or update an item
     *
     * @param onUpsertAction The action to execute after the item inserted or updated
     */
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

    /**
     * Method to insert a new item
     *
     * @param onInsert The action to execute after the item inserted
     */
    protected abstract fun insert(
        onInsert: () -> Unit,
    )

    /**
     * Method to update an existing item
     *
     * @param onUpdate The action to execute after the item updated
     */
    protected abstract fun update(
        onUpdate: () -> Unit,
    )

    /**
     * Method to check the validity of the form data to insert or update an item
     *
     * @return the validity of the form as [Boolean]
     */
    @RequiresSuperCall
    protected open fun validForm(): Boolean {
        return if (!isDescriptionValid(itemDescription.value)) {
            itemDescriptionError.value = true
            return false
        } else
            true
    }

}