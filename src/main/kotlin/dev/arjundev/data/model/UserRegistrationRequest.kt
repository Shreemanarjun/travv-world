package dev.arjundev.data.model

import dev.arjundev.data.dao.user.IUserDao
import dev.arjundev.data.model.validation.accessors.email
import dev.arjundev.data.model.validation.accessors.password
import dev.arjundev.data.model.validation.accessors.userName
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
data class UserRegistrationRequest(val userName: String, val email: String, val password: String)


///Validation for user login request

val validateUserRegistrationRequest = Validator.suspendable<IUserDao, UserRegistrationRequest> { dao ->


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
            constrain { dao.isEmailExist(it) == null } otherwise {
                "This email is already registered"
            }
        }
    }

    userName {
      val isNotBlank=  isNotBlank()
      if (isNotBlank.satisfied)
          constrain {
              val isUserExist=  dao.isUserNameExist(it) == null
              println("Isuser exist $isUserExist")
              isUserExist
          } otherwise { "Username already taken" }
    }

    password {
        isNotBlank()
        hasLengthGreaterThanOrEqualTo(8)

    }

}