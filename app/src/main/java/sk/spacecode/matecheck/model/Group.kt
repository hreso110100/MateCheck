package sk.spacecode.matecheck.model

import java.io.Serializable

data class Group(
    var name: String = "",
    var creatorID: String = "",
    var dateOfCreation: Long = 0,
    var membersID: ArrayList<String> = arrayListOf()
): Serializable