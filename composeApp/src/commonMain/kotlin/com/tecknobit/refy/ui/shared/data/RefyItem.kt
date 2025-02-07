package com.tecknobit.refy.ui.shared.data

import com.tecknobit.refy.ui.shared.data.RefyUser.RefyUserImpl

interface RefyItem {
    val id: String
    val owner: RefyUserImpl
    val title: String
    val description: String
    val date: Long
}