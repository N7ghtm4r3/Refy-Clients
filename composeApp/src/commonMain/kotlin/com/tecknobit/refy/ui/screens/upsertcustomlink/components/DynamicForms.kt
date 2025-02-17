@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.refy.ui.screens.upsertcustomlink.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.refy.ui.screens.upsertcustomlink.presentation.UpsertCustomLinkScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.UpsertScreen.Companion.inputFieldShape
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.key
import refy.composeapp.generated.resources.not_valid
import refy.composeapp.generated.resources.value
import refy.composeapp.generated.resources.value_not_valid

/**
 * Custom component used to insert the authentication fields
 *
 * @param viewModel The support viewmodel for the screen
 */
@Composable
@NonRestartableComposable
fun AuthForm(
    viewModel: UpsertCustomLinkScreenViewModel
) {
    DynamicForm(
        data = viewModel.authFields
    )
}

/**
 * Custom component used to insert the resources shared by the link
 *
 * @param viewModel The support viewmodel for the screen
 */
@Composable
@NonRestartableComposable
fun ResourcesForm(
    viewModel: UpsertCustomLinkScreenViewModel
) {
    DynamicForm(
        data = viewModel.resources
    )
}

/**
 * Custom component used to manage the [data] such adding, editing or removing row
 *
 * @param data The data to display and to manage
 */
@Composable
@NonRestartableComposable
private fun DynamicForm(
    data: SnapshotStateList<Pair<MutableState<String>, MutableState<String>>>
) {
    LazyColumn(
        modifier = Modifier
            .heightIn(
                max = 250.dp
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stickyHeader {
            SmallFloatingActionButton(
                onClick = { data.add(Pair(mutableStateOf(""), mutableStateOf(""))) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null
                )
            }
        }
        itemsIndexed(
            items = data,
            key = { index, _ -> index }
        ) { index, pair ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FormInputField(
                    value = pair.first,
                    placeholder = Res.string.key,
                    errorText = Res.string.not_valid
                )
                FormInputField(
                    value = pair.second,
                    placeholder = Res.string.value,
                    errorText = Res.string.value_not_valid,
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (data.lastIndex == index)
                            ImeAction.Done
                        else
                            ImeAction.Next
                    )
                )
                SmallFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.error,
                    onClick = { data.remove(pair) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

/**
 * Custom [EquinoxOutlinedTextField] used to insert the key or the value requested by the user
 * via the [DynamicForm] component
 *
 * @param value The value inserted by the user
 * @param placeholder The placeholder to display
 * @param errorText The error text to display
 * @param keyboardOptions The options of the keyboards
 */
@Composable
@NonRestartableComposable
private fun RowScope.FormInputField(
    value: MutableState<String>,
    placeholder: StringResource,
    errorText: StringResource,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next
    )
) {
    val valueError = remember { mutableStateOf(false) }
    EquinoxOutlinedTextField(
        modifier = Modifier
            .weight(1f),
        shape = inputFieldShape,
        value = value,
        placeholder = placeholder,
        isError = valueError,
        validator = { it.isNotEmpty() },
        errorText = errorText,
        keyboardOptions = keyboardOptions
    )
}