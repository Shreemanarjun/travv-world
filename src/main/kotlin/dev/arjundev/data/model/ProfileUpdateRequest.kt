package dev.arjundev.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ProfileUpdateRequest(val username: String, val email: String)


/*val profilevalidator = Validator.suspendable<IUserDao, ProfileUpdateRequest> { dao ->

    username {
        val (usernamevalidated) = isNotBlank()
        if (usernamevalidated) {
            constrain { it?.let { it1 -> dao.isUserNameExist(it1) } == null } otherwise {
                "This username is already registered"
            }
        }
    }

    email {
        isNotBlank()
        val emailpattern =
            """
            (([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))
            """.trimIndent();
        val (isValidEmail) = isMatching(Regex(emailpattern)) otherwise {
            "Must be a valid email"
        }

        if (isValidEmail) {
            constrain { it?.let { it1 -> dao.isEmailExist(it1) } == null } otherwise {
                "This email is already registered"
            }
        }
    }*/


