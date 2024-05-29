package dev.arjundev.plugins


import dev.arjundev.data.dao.user.IUserDao
import dev.arjundev.data.model.UserLoginRequest
import dev.arjundev.data.model.UserRegistrationRequest
import dev.arjundev.data.model.validateUserLoginRequest
import dev.arjundev.data.model.validateUserRegistrationRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import org.koin.ktor.ext.inject
import dev.nesk.akkurate.ValidationResult.Failure as AkkurateFailure
import dev.nesk.akkurate.ValidationResult.Success as AkkurateSuccess

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<UserLoginRequest> { userrequest ->
            val userDao by inject<IUserDao>()
            when (val result = validateUserLoginRequest(userDao, userrequest)) {
                is AkkurateSuccess -> ValidationResult.Valid
                is AkkurateFailure -> {
                    val reasons = result.violations.map {
                        "${it.path.joinToString(".")}: ${it.message}"
                    }
                    ValidationResult.Invalid(reasons)
                }
            }
        }
        validate<UserRegistrationRequest> { userrequest ->
            val userDao by inject<IUserDao>()
            when (val result = validateUserRegistrationRequest(userDao, userrequest)) {
                is AkkurateSuccess -> ValidationResult.Valid
                is AkkurateFailure -> {
                    val reasons = result.violations.map {
                        "${it.path.joinToString(".")}: ${it.message}"
                    }
                    ValidationResult.Invalid(reasons)
                }
            }
        }

    }
}