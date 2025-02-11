package com.tecknobit.refy.ui.screens.upsertlink.presenter

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.upsertlink.presentation.UpsertLinkScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.UpsertScreen
import com.tecknobit.refycore.helpers.RefyInputsValidator.isLinkResourceValid
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.insert_link
import refy.composeapp.generated.resources.link_reference
import refy.composeapp.generated.resources.link_reference_not_valid
import refy.composeapp.generated.resources.update_link

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

    @Composable
    @NonRestartableComposable
    override fun ColumnScope.UpsertForm() {
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
            shape = RoundedCornerShape(
                size = 10.dp
            ),
            value = viewModel.reference,
            isError = viewModel.referenceError,
            allowsBlankSpaces = false,
            validator = { isLinkResourceValid(it) },
            errorText = Res.string.link_reference_not_valid,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
        ItemDescriptionSection()
        UpsertButton()
    }

    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    override fun CollectStates() {
        super.CollectStates()
        viewModel.referenceError = remember { mutableStateOf(false) }
    }

    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    override fun CollectStatesAfterLoading() {
        super.CollectStatesAfterLoading()
        viewModel.reference = remember {
            mutableStateOf(
                if (item.value != null)
                    item.value!!.reference
                else
                    ""
            )
        }
    }

}