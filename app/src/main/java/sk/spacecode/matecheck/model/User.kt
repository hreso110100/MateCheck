package sk.spacecode.matecheck.model

data class User(
    var ID: String? = "",
    var firstName: String? = "",
    var surname: String? = "",
    var photoPath: String? = "",
    var email: String? = "",
    var favouriteGroups: ArrayList<String> = arrayListOf()
) : BaseModel