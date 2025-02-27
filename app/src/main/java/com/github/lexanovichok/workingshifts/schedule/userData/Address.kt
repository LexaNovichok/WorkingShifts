package com.github.lexanovichok.workingshifts.schedule.userData

data class Address(
    var id : String = "",
    val city : String = "",
    val street : String = "",
    val houseNum : String = "",
    val description : String = ""
)
