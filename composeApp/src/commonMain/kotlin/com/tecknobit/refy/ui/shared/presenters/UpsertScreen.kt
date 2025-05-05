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

/**
 * The [UpsertScreen] class is useful to handle the insertion or updating of an item
 *
 * @param itemId The identifier of the item to update
 * @param insertTitle The title of the screen when the action is an insert action
 * @param updateTitle The title of the screen when the action is an update action
 * @param insertButtonText The text of the button used to upsert the item
 * @param viewModel The support viewmodel for the screen
 *
 * @param I The type of the item displayed
 * @param V The type of the viewmodel of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see RefyScreen
 */
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

        /**
         *`inputFieldShape` the shape to apply to the input fields component
         */
        val inputFieldShape = RoundedCornerShape(
            size = 10.dp
        )

    }

    /**
     *`isUpdating` whether the action is an updating action
     */
    protected val isUpdating = itemId != null

    /**
     *`item` the existing item to update
     */
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
                                    // TODO: TO CHANGE
                                    .widthIn(
                                        max = 1280.dp
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

    /**
     * Method to set the theme for the current screen
     *
     * @param content The content to display
     */
    @Composable
    @NonRestartableComposable
    protected open fun ScreenTheme(
        content: @Composable () -> Unit
    ) {
        RefyTheme(
            content = content
        )
    }

    /**
     * The form used to insert or update the item details
     */
    @Composable
    @NonRestartableComposable
    protected abstract fun ColumnScope.UpsertForm()

    /**
     * The section used to allow the user to insert or update the description of the item
     */
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

    /**
     * Custom component to indicate the start of a new section of the screen
     *
     * @param title The title of the section
     */
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

    /**
     * Custom [Button] used to execute the upsert action for the item
     */
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

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveItem()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        item = viewModel.item.collectAsState()
        viewModel.itemDescriptionError = remember { mutableStateOf(false) }
    }

    /**
     * Method to collect or instantiate the states of the screen after a loading required to correctly assign an
     * initial value to the states
     */
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