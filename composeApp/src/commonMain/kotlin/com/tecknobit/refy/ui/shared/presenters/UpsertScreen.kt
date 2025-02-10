package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.ui.components.ScreenTopBar
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.refy.ui.theme.RefyTheme
import org.jetbrains.compose.resources.StringResource

@Structure
abstract class UpsertScreen<I : RefyItem, V : UpsertScreenViewModel<I>>(
    itemId: String? = null,
    private val insertTitle: StringResource,
    private val updateTitle: StringResource,
    viewModel: V
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    protected val isUpdating = itemId != null

    protected lateinit var item: State<I?>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        RefyTheme {
            ManagedContent(
                viewModel = viewModel,
                initialDelay = 500,
                loadingRoutine = if (isUpdating) {
                    {
                        item.value != null
                    }
                } else
                    null,
                content = {
                    CollectStatesAfterLoading()
                    Scaffold(
                        snackbarHost = {
                            SnackbarHost(
                                hostState = viewModel.snackbarHostState!!
                            )
                        }
                    ) {
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
                                    .padding(
                                        all = 16.dp
                                    ),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                ScreenTopBar(
                                    screenTitle = if (isUpdating)
                                        updateTitle
                                    else
                                        insertTitle
                                )
                                UpsertForm()
                            }
                        }
                    }
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    protected abstract fun UpsertForm()

    override fun onStart() {
        super.onStart()
        viewModel.retrieveItem()
    }

    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        item = viewModel.item.collectAsState()
        viewModel.itemDescriptionError = remember { mutableStateOf(false) }
    }

    @Composable
    @RequiresSuperCall
    override fun CollectStatesAfterLoading() {
        viewModel.itemDescription = remember {
            mutableStateOf(
                if (item.value != null)
                    item.value!!.description
                else
                    ""
            )
        }
    }

}