package sk.spacecode.matecheck.model

import sk.spacecode.matecheck.enums.TaskPriority

data class Task(
    var description: String = "",
    var creatorID: String = "",
    var dateOfCreation: Long = 0,
    var dateOfExpiration: Long = 0,
    var priority: TaskPriority = TaskPriority.LOW,
    var public: Boolean = false,
    var membersID: ArrayList<String> = arrayListOf()
)