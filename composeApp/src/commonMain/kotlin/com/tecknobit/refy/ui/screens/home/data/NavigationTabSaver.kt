package com.tecknobit.refy.ui.screens.home.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.vector.ImageVector
import org.jetbrains.compose.resources.StringResource

class NavigationTabSaver : Saver<MutableState<NavigationTab>, Array<Any>> {

    /**
     * Convert the restored value back to the original Class. If null is returned the value will
     * not be restored and would be initialized again instead.
     */
    override fun restore(
        value: Array<Any>
    ): MutableState<NavigationTab> {
        return mutableStateOf(
            NavigationTab(
                title = value[0] as StringResource,
                icon = value[1] as ImageVector
            )
        )
    }

    /**
     * Convert the value into a saveable one. If null is returned the value will not be saved.
     */
    override fun SaverScope.save(
        value: MutableState<NavigationTab>
    ): Array<Any> {
        val tab = value.value
        return arrayOf(tab.title, tab.icon)
    }

}