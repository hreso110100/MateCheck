package sk.spacecode.matecheck.model

data class Group(var name: String, var creatorID: String, var dateOfCreation: Long, var membersID: ArrayList<String?>)