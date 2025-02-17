package com.tecknobit.refy.ui.shared.data

import com.tecknobit.refy.localUser
import com.tecknobit.refy.ui.shared.data.RefyUser.RefyUserImpl

/**
 * The `RefyItem` interface is useful to give the basic details about a Refy's item
 *
 * @author N7ghtm4r3 - Tecknobit
 */
interface RefyItem {

    /**
     *`id` the identifier of the item
     */
    val id: String

    /**
     *`owner` the owner of the item
     */
    val owner: RefyUserImpl

    /**
     *`title` the title of the item
     */
    val title: String

    /**
     *`description` the description of the item
     */
    val description: String

    /**
     *`date` the date when the item has been inserted in the system
     */
    val date: Long

    /**
     * Method used to determine if the current [localUser] is the owner of the item
     *
     * @return whether the [localUser] is the owner of the item as [Boolean]
     */
    fun iAmTheOwner(): Boolean {
        return localUser.userId == owner.id
    }

}