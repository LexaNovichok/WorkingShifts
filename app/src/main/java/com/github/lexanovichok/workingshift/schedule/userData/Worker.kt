package com.github.lexanovichok.workingshift.schedule.userData

data class Worker(
    var id : String = "",
    val name : String = "",
    val contacts : String = "",
    val description : String = "",
    val icon : String = "",
    var order : Int = 0
)
