package com.github.lexanovichok.workingshift.schedule.userData

data class Task(
    var id: String = "",
    val worker : Worker = Worker(),
    val address : Address = Address(),
    val description : String = "",
    val date : String = ""
)
