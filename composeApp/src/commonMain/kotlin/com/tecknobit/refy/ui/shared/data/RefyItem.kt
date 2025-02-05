package com.tecknobit.refy.ui.shared.data

import com.tecknobit.refy.ui.shared.data.RefyUser.RefyUserImpl

interface RefyItem {
    val owner: RefyUserImpl
    val title: String
    val description: String
}