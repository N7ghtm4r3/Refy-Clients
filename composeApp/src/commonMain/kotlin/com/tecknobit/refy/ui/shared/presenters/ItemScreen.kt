package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.presentations.ItemScreenViewModel

@Structure
abstract class ItemScreen<I : RefyItem, V : ItemScreenViewModel<I>>(
    viewModel: V,
    private val itemName: String
) : RefyScreen<V>(
    viewModel = viewModel
) {

    protected lateinit var item: State<I?>

    @Composable
    @NonRestartableComposable
    override fun RowScope.NavBackButton() {
        IconButton(
            modifier = Modifier
                .size(25.dp),
            onClick = { navigator.goBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = null
            )
        }
    }

    @Composable
    @NonRestartableComposable
    override fun title(): String {
        return itemName
    }

    @Composable
    @NonRestartableComposable
    override fun Content() {
        ManagedContent(
            viewModel = viewModel,
            initialDelay = 500,
            loadingRoutine = { item.value != null },
            content = {
                ItemDetails()
            }
        )
    }

    @Composable
    @NonRestartableComposable
    protected abstract fun ItemDetails()

    override fun onStart() {
        super.onStart()
        viewModel.retrieveItem()
    }

    @Composable
    override fun CollectStates() {
        item = viewModel.item.collectAsState()
    }

}