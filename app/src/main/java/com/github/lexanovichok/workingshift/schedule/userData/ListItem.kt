package com.github.lexanovichok.workingshift.schedule.userData

sealed class ListItem {
    data class HeaderItem(val text : String) : ListItem()
    data class TaskItem(val task : Task) : ListItem()
}