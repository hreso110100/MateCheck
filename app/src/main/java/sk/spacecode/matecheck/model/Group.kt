package sk.spacecode.matecheck.model

import java.sql.Timestamp

data class Group(
    var ID: String = "",
    var name: String = "",
    var creatorID: String = "",
    var dateOfCreation: Long = Timestamp(System.currentTimeMillis()).time,
    var membersID: ArrayList<String> = arrayListOf(),
    var color: String = "#000000"
) : BaseModel