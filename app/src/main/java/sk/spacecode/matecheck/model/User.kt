package sk.spacecode.matecheck.model

data class User(
    var id: String? = null,
    var firstName: String? = null,
    var surname: String? = null,
    var photoPath: String? = null,
    var email: String? = null
)