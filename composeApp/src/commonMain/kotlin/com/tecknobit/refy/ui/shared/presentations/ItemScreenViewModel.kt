package com.tecknobit.refy.ui.shared.presentations

import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.data.RefyLink.RefyLinkImpl
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * The `ItemScreenViewModel` class is the support class used by the screens which have to display
 * the details of a [RefyItem] or manage it
 *
 * @param itemId The identifier of the item
 * @param name The name of the item
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see androidx.lifecycle.ViewModel
 * @see com.tecknobit.equinoxcompose.session.Retriever
 * @see EquinoxViewModel
 * @see RefyScreenViewModel
 * @see LinksRetriever
 *
 * @param I The type of the item displayed
 *
 */
@Structure
abstract class ItemScreenViewModel<I : RefyItem>(
    private val itemId: String,
    name: String,
) : RefyScreenViewModel(), LinksRetriever<RefyLinkImpl> {

    /**
     *`_item` the item to display
     */
    protected val _item = MutableStateFlow<I?>(
        value = null
    )
    val item: StateFlow<I?> = _item

    /**
     *`_itemName` the name of the item displayed
     */
    protected val _itemName = MutableStateFlow(
        value = name
    )
    val itemName: StateFlow<String> = _itemName

    /**
     * Method to retrieve the information of the item to display
     */
    abstract fun retrieveItem()

    /**
     *`linksState` the state used to handle the pagination of the links list
     */
    override val linksState: PaginationState<Int, RefyLinkImpl> = PaginationState(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadLinks(
                page = page
            )
        }
    )

    /**
     * Method used to refresh the data displayed by the screen
     */
    override fun refresh() {
        linksState.refresh()
    }

    /**
     * Method to remove a link contained by the [_item]
     *
     * @param link The link to remove
     */
    abstract fun removeLink(
        link: RefyLinkImpl
    )

}