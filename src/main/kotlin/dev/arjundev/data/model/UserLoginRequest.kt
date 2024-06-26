package dev.arjundev.data.model

import dev.arjundev.data.dao.user.IUserDao
import dev.arjundev.data.model.validation.accessors.email
import dev.arjundev.data.model.validation.accessors.password
import dev.nesk.akkurate.Validator
import dev.nesk.akkurate.annotations.Validate
import dev.nesk.akkurate.constraints.builders.hasLengthGreaterThanOrEqualTo
import dev.nesk.akkurate.constraints.builders.isMatching
import dev.nesk.akkurate.constraints.builders.isNotBlank
import dev.nesk.akkurate.constraints.constrain
import dev.nesk.akkurate.constraints.otherwise
import kotlinx.serialization.Serializable

@Serializable
@Validate
data class UserLoginRequest(val email: String, val password: String)


///Validation for user login request

val validateUserLoginRequest = Validator.suspendable<IUserDao, UserLoginRequest> { dao ->

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
            constrain { dao.isEmailExist(it) != null } otherwise {
                "This email is not registered yet"
            }
        }
    }

    password{
        isNotBlank()
        hasLengthGreaterThanOrEqualTo(8)

    }

}