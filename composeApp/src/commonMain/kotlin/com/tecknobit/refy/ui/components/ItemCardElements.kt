package com.tecknobit.refy.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.localUser
import com.tecknobit.refy.ui.icons.CollapseAll
import com.tecknobit.refy.ui.icons.ExpandAll
import com.tecknobit.refy.ui.shared.data.RefyItem

@Composable
@NonRestartableComposable
fun ItemCardDetails(
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean>,
    item: RefyItem
) {
    Column(
        modifier = modifier
            .heightIn(
                max = 200.dp
            )
    ) {
        Text(
            modifier = Modifier
                .then(
                    if (expanded.value) {
                        Modifier.padding(
                            bottom = 5.dp
                        )
                    } else
                        Modifier
                ),
            text = item.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
            fontFamily = displayFontFamily
        )
        Text(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .animateContentSize(),
            text = item.description,
            minLines = 3,
            maxLines = if (expanded.value)
                Int.MAX_VALUE
            else
                3,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp
        )
    }
}

@Composable
@NonRestartableComposable
fun ExpandCardButton(
    expanded: MutableState<Boolean>
) {
    IconButton(
        modifier = Modifier
            .size(30.dp),
        onClick = { expanded.value = !expanded.value }
    ) {
        Icon(
            imageVector = if (expanded.value)
                CollapseAll
            else
                ExpandAll,
            contentDescription = null
        )
    }
}

@Composable
@NonRestartableComposable
fun RowScope.DeleteItemButton(
    item: RefyItem,
    deleteContent: @Composable() (MutableState<Boolean>) -> Unit
) {
    if (item.owner.id == localUser.userId) {
        val deleteItem = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                modifier = Modifier
                    .size(32.dp),
                onClick = { deleteItem.value = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        deleteContent(deleteItem)
    }
}