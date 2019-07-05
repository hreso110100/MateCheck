package sk.spacecode.matecheck.model

import sk.spacecode.matecheck.enums.TaskPriority
import sk.spacecode.matecheck.enums.TaskState
import java.sql.Timestamp

data class Task(
    var name: String = "",
    var status: TaskState = TaskState.IN_PROGRESS,
    var description: String = "",
    var creatorID: String = "",
    var groupID: String = "",
    var dateOfCreation: Long = Timestamp(System.currentTimeMillis()).time,
    var dateOfExpiration: Long = 0,
    var priority: TaskPriority = TaskPriority.MEDIUM,
    var public: Boolean = false,
    var membersID: ArrayList<String> = arrayListOf()
) : BaseModel