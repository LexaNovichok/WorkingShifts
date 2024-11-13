package com.github.lexanovichok.workingshifts.schedule.userData

data class Task(
    val id: String = "",
    val worker : Worker = Worker(),
    val adress : Adress = Adress(),
    val date : String = ""
)
