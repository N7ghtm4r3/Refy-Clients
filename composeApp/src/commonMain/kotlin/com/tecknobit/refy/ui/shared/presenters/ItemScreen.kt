package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.presentations.ItemScreenViewModel
import com.tecknobit.refy.ui.theme.AppTypography
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Structure
abstract class ItemScreen<I : RefyItem, V : ItemScreenViewModel<I>>(
    viewModel: V,
    private val itemName: String,
    snackbarHostStateBottomPadding: Dp = 100.dp,
    contentBottomPadding: Dp = 79.dp
) : RefyScreen<V>(
    viewModel = viewModel,
    snackbarHostStateBottomPadding = snackbarHostStateBottomPadding,
    contentBottomPadding = contentBottomPadding
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
    override fun RowScope.Filters() {
    }

    @Composable
    @NonRestartableComposable
    override fun Content() {
        ManagedContent(
            viewModel = viewModel,
            initialDelay = 500,
            loadingRoutine = { item.value != null },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .widthIn(
                                max = MAX_CONTAINER_WIDTH
                            )
                    ) {
                        ItemDetails()
                    }
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    protected abstract fun ColumnScope.ItemDetails()

    @Composable
    @NonRestartableComposable
    protected fun HeaderTitle(
        header: StringResource
    ) {
        Text(
            text = stringResource(header),
            style = AppTypography.headlineLarge
        )
    }

    override fun onStart() {
        super.onStart()
        viewModel.retrieveItem()
    }

    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        item = viewModel.item.collectAsState()
    }

}