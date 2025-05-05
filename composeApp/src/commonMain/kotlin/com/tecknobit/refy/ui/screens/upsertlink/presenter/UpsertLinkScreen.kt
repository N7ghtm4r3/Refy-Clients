package com.tecknobit.refy.ui.screens.upsertlink.presenter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.upsertlink.presentation.UpsertLinkScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
import com.tecknobit.refy.ui.shared.presenters.UpsertScreen
import com.tecknobit.refycore.helpers.RefyInputsValidator.isLinkResourceValid
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.insert_link
import refy.composeapp.generated.resources.link_reference
import refy.composeapp.generated.resources.link_reference_not_valid
import refy.composeapp.generated.resources.update_link

/**
 * The [UpsertLinkScreen] class is useful to insert a new link or edit an exiting ong
 *
 * @param linkId The identifier of the link to update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see RefyScreen
 * @see UpsertScreen
 */
class UpsertLinkScreen(
    linkId: String?
) : UpsertScreen<RefyLinkImpl, UpsertLinkScreenViewModel>(
    itemId = linkId,
    insertTitle = Res.string.insert_link,
    updateTitle = Res.string.update_link,
    viewModel = UpsertLinkScreenViewModel(
        linkId = linkId
    )
) {

    /**
     * The form used to insert or update the item details
     */
    @Composable
    @NonRestartableComposable
    override fun ColumnScope.UpsertForm() {
        LinkReferenceSection()
        ItemDescriptionSection()
        UpsertButton()
    }

    /**
     * Section where the user can insert the link reference
     */
    @Composable
    private fun LinkReferenceSection() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        SectionTitle(
            title = Res.string.link_reference
        )
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            shape = inputFieldShape,
            value = viewModel.reference,
            isError = viewModel.referenceError,
            allowsBlankSpaces = false,
            validator = { isLinkResourceValid(it) },
            errorText = Res.string.link_reference_not_valid,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        viewModel.referenceError = remember { mutableStateOf(false) }
    }

    /**
     * Method to collect or instantiate the states of the screen after a loading required to correctly assign an
     * initial value to the states
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStatesAfterLoading() {
        super.CollectStatesAfterLoading()
        viewModel.reference = remember {
            mutableStateOf(
                if (isUpdating)
                    item.value!!.reference
                else
                    ""
            )
        }
    }

}