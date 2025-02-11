package com.tecknobit.refy.ui.screens.upsertcollection.presenter

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.materialkolor.rememberDynamicColorScheme
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Auto
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Dark
import com.tecknobit.equinoxcompose.session.EquinoxLocalUser.ApplicationTheme.Light
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.refy.localUser
import com.tecknobit.refy.ui.screens.collection.helpers.adaptStatusBarToCollectionTheme
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.upsertcollection.presentation.UpsertCollectionScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.UpsertScreen
import com.tecknobit.refycore.helpers.RefyInputsValidator.isTitleValid
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collection_name
import refy.composeapp.generated.resources.insert_collection
import refy.composeapp.generated.resources.name_not_valid
import refy.composeapp.generated.resources.update_collection

class UpsertCollectionScreen(
    collectionId: String?,
    private val collectionColor: String
) : UpsertScreen<LinksCollection, UpsertCollectionScreenViewModel>(
    itemId = collectionId,
    viewModel = UpsertCollectionScreenViewModel(
        collectionId = collectionId
    ),
    insertTitle = Res.string.insert_collection,
    updateTitle = Res.string.update_collection
) {

    private lateinit var color: MutableState<Color>

    @Composable
    @NonRestartableComposable
    override fun ScreenTheme(
        content: @Composable() () -> Unit
    ) {
        val colorScheme = rememberDynamicColorScheme(
            primary = color.value,
            isDark = when (localUser.theme) {
                Dark -> true
                Light -> false
                Auto -> isSystemInDarkTheme()
            },
            isAmoled = true
        )
        MaterialTheme(
            colorScheme = colorScheme
        ) {
            adaptStatusBarToCollectionTheme()
            content()
        }
    }

    @Composable
    @NonRestartableComposable
    override fun ColumnScope.UpsertForm() {
        val focusRequester = remember { FocusRequester() }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
        SectionTitle(
            title = Res.string.collection_name
        )
        EquinoxOutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(
                size = 10.dp
            ),
            value = viewModel.collectionTitle,
            isError = viewModel.collectionTitleError,
            allowsBlankSpaces = false,
            validator = { isTitleValid(it) },
            errorText = Res.string.name_not_valid,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            )
        )
        ItemDescriptionSection()
        UpsertButton()
    }

    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        color = remember { mutableStateOf(collectionColor.toColor()) }
        viewModel.collectionTitle = remember { mutableStateOf("") }
        viewModel.collectionTitleError = remember { mutableStateOf(false) }
    }

}