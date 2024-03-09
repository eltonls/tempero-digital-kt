package com.eltonls.temperodigital.models

import com.google.gson.annotations.SerializedName

data class MenuList(@SerializedName("sections") val sections: MutableList<MenuListSection>)
