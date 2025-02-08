package com.tecknobit.refy.ui.shared.data

import com.tecknobit.refy.ui.shared.data.RefyUser.RefyUserImpl
import kotlin.random.Random

interface RefyItem {
    val id: String
    val owner: RefyUserImpl
    val title: String
    val description: String
    val date: Long

    fun iAmTheOwner(): Boolean {
        // TODO: TO USE THIS
        // return localUser.userId == owner.id
        return Random.nextBoolean()
    }

}