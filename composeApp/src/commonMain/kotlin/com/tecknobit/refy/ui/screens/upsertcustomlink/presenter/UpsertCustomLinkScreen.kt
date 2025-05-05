@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.screens.upsertcustomlink.presenter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refy.ui.screens.customs.data.CustomRefyLink
import com.tecknobit.refy.ui.screens.upsertcustomlink.components.AuthForm
import com.tecknobit.refy.ui.screens.upsertcustomlink.components.ExpirationTimeCheckBox
import com.tecknobit.refy.ui.screens.upsertcustomlink.components.ResourcesForm
import com.tecknobit.refy.ui.screens.upsertcustomlink.components.UniqueAccessCheckBox
import com.tecknobit.refy.ui.screens.upsertcustomlink.presentation.UpsertCustomLinkScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.RefyScreen
import com.tecknobit.refy.ui.shared.presenters.UpsertScreen
import com.tecknobit.refycore.enums.ExpiredTime
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTitleValid
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.access_method
import refy.composeapp.generated.resources.auth
import refy.composeapp.generated.resources.create
import refy.composeapp.generated.resources.create_link
import refy.composeapp.generated.resources.name
import refy.composeapp.generated.resources.name_not_valid
import refy.composeapp.generated.resources.resources
import refy.composeapp.generated.resources.update_link

/**
 * The [UpsertCustomLinkScreen] class is useful to insert a new custom link or update an exiting one
 *
 * @param linkId The identifier of the link to update
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see RefyScreen
 * @see UpsertScreen
 */
class UpsertCustomLinkScreen(
    linkId: String?
) : UpsertScreen<CustomRefyLink, UpsertCustomLinkScreenViewModel>(
    itemId = linkId,
    insertTitle = Res.string.create_link,
    updateTitle = Res.string.update_link,
    insertButtonText = Res.string.create,
    viewModel = UpsertCustomLinkScreenViewModel(
        linkId = linkId
    )
) {

    /**
     * The form used to insert or update the item details
     */
    @Composable
    @NonRestartableComposable
    override fun ColumnScope.UpsertForm() {
        LinkNameSection()
        ItemDescriptionSection()
        AccessMethod()
        Auth()
        Resources()
        UpsertButton()
    }

    /**
     * Section where the user can insert the name of the link
     */
    @Composable
    @NonRestartableComposable
    private fun LinkNameSection() {
        SectionTitle(
            title = Res.string.name
        )
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            shape = inputFieldShape,
            value = viewModel.linkName,
            isError = viewModel.linkNameError,
            validator = { isTitleValid(it) },
            errorText = Res.string.name_not_valid,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
    }

    /**
     * Section where the user select the access method for the link
     */
    @Composable
    @NonRestartableComposable
    private fun AccessMethod() {
        SectionTitle(
            title = Res.string.access_method
        )
        ResponsiveContent(
            onExpandedSizeClass = { AccessMethodRow() },
            onMediumSizeClass = { AccessMethodRow() },
            onCompactSizeClass = { AccessMethodColumn() }
        )
    }

    /**
     * The [AccessMethod] displayed as [Row] layout
     */
    @Composable
    @NonRestartableComposable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    private fun AccessMethodRow() {
        Row {
            UniqueAccessCheckBox(
                viewModel = viewModel
            )
            ExpirationTimeCheckBox(
                viewModel = viewModel
            )
        }
    }

    /**
     * The [AccessMethod] displayed as [Column] layout
     */
    @Composable
    @CompactClassComponent
    @NonRestartableComposable
    private fun AccessMethodColumn() {
        Column {
            UniqueAccessCheckBox(
                viewModel = viewModel
            )
            ExpirationTimeCheckBox(
                viewModel = viewModel
            )
        }
    }

    /**
     * Section where the user can insert the authentication fields to protect the resources shared by the link
     */
    @Composable
    @NonRestartableComposable
    private fun Auth() {
        SectionTitle(
            title = Res.string.auth
        )
        AuthForm(
            viewModel = viewModel
        )
    }

    /**
     * Section where the user can insert the resources to share with the link
     */
    @Composable
    @NonRestartableComposable
    private fun Resources() {
        SectionTitle(
            title = Res.string.resources
        )
        ResourcesForm(
            viewModel = viewModel
        )
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    override fun CollectStates() {
        super.CollectStates()
        viewModel.linkNameError = remember { mutableStateOf(false) }
    }

    /**
     * Method to collect or instantiate the states of the screen after a loading required to correctly assign an
     * initial value to the states
     */
    @Composable
    @RequiresSuperCall
    @NonRestartableComposable
    override fun CollectStatesAfterLoading() {
        super.CollectStatesAfterLoading()
        viewModel.linkName = remember {
            mutableStateOf(
                if (isUpdating)
                    item.value!!.title
                else
                    ""
            )
        }
        viewModel.uniqueAccess = remember {
            mutableStateOf(
                if (isUpdating)
                    item.value!!.uniqueAccess
                else
                    false
            )
        }
        viewModel.expirationTime = remember {
            mutableStateOf(
                if (isUpdating)
                    item.value!!.expiredTime
                else
                    ExpiredTime.NO_EXPIRATION
            )
        }
        viewModel.authFields = remember {
            if (isUpdating)
                item.value!!.fields.toFormData()
            else
                mutableStateListOf()
        }
        viewModel.resources = remember {
            if (isUpdating)
                item.value!!.resources.toFormData()
            else
                mutableStateListOf()
        }
        LaunchedEffect(Unit) {
            if (viewModel.resources.isEmpty())
                viewModel.resources.add(Pair(mutableStateOf(""), mutableStateOf("")))
        }
    }

    /**
     * Method to convert a [Map] to a data structure as [SnapshotStateList] to allow its handling
     * during the upsert action
     *
     * @return the form data converted as [SnapshotStateList]
     */
    private fun Map<String, String>.toFormData(): SnapshotStateList<Pair<MutableState<String>, MutableState<String>>> {
        val formData = mutableStateListOf<Pair<MutableState<String>, MutableState<String>>>()
        this.forEach { entry ->
            formData.add(
                element = Pair(
                    first = mutableStateOf(entry.key),
                    second = mutableStateOf(entry.value)
                )
            )
        }
        return formData
    }

}