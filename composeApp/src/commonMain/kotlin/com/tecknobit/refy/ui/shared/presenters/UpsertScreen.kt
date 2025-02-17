@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.ScreenTopBar
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.presentations.UpsertScreenViewModel
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refycore.helpers.RefyInputsValidator.isDescriptionValid
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.description
import refy.composeapp.generated.resources.description_not_valid
import refy.composeapp.generated.resources.insert
import refy.composeapp.generated.resources.update

@Structure
abstract class UpsertScreen<I : RefyItem, V : UpsertScreenViewModel<I>>(
    itemId: String? = null,
    private val insertTitle: StringResource,
    private val updateTitle: StringResource,
    private val insertButtonText: StringResource = Res.string.insert,
    viewModel: V
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    companion object {

        val inputFieldShape = RoundedCornerShape(
            size = 10.dp
        )

    }

    protected val isUpdating = itemId != null

    protected lateinit var item: State<I?>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        ScreenTheme {
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
                                Column(
                                    modifier = Modifier
                                        .verticalScroll(rememberScrollState()),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    UpsertForm()
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    @Composable
    @NonRestartableComposable
    protected open fun ScreenTheme(
        content: @Composable () -> Unit
    ) {
        RefyTheme(
            content = content
        )
    }

    @Composable
    @NonRestartableComposable
    protected abstract fun ColumnScope.UpsertForm()

    @Composable
    @NonRestartableComposable
    protected fun ItemDescriptionSection() {
        SectionTitle(
            title = Res.string.description
        )
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            shape = inputFieldShape,
            minLines = 10,
            maxLines = 10,
            value = viewModel.itemDescription,
            isError = viewModel.itemDescriptionError,
            validator = { isDescriptionValid(it) },
            errorText = Res.string.description_not_valid,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            )
        )
    }

    @Composable
    @NonRestartableComposable
    protected fun SectionTitle(
        title: StringResource
    ) {
        Text(
            text = stringResource(title),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }

    @Composable
    @NonRestartableComposable
    protected fun ColumnScope.UpsertButton() {
        Button(
            modifier = Modifier
                .navigationBarsPadding()
                .then(
                    responsiveAssignment(
                        onExpandedSizeClass = {
                            Modifier.align(Alignment.End)
                        },
                        onMediumSizeClass = {
                            Modifier.align(Alignment.End)
                        },
                        onCompactSizeClass = {
                            Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                        }
                    )
                ),
            shape = RoundedCornerShape(
                size = 10.dp
            ),
            onClick = {
                viewModel.upsert {
                    navigator.goBack()
                }
            }
        ) {
            Text(
                text = stringResource(
                    if (isUpdating)
                        Res.string.update
                    else
                        insertButtonText
                ),
                fontSize = 18.sp
            )
        }
    }

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
                if (isUpdating)
                    item.value!!.description
                else
                    ""
            )
        }
    }

}