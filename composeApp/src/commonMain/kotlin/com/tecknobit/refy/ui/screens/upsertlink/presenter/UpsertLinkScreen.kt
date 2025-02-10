package com.tecknobit.refy.ui.screens.upsertlink.presenter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.upsertlink.presentation.UpsertLinkScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.UpsertScreen
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.insert_link
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
    override fun UpsertForm() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
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