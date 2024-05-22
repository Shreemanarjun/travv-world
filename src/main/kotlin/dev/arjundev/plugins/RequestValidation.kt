package dev.arjundev.plugins

import dev.arjundev.data.model.UserLoginRequest
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation(){
    install(RequestValidation) {
        validate<UserLoginRequest> { userrequest ->
            when {
                userrequest.email.isBlank() -> ValidationResult.Invalid("Username cannot be null")
                userrequest.password.isBlank() -> ValidationResult.Invalid("password cannot be null")
                else -> ValidationResult.Valid
            }
        }
      /*  validate<UserRegistrationRequest> { usersignuprequest ->
            when {
                usersignuprequest.userName.isBlank() -> ValidationResult.Invalid("Username cannot be null")
                usersignuprequest.password.isBlank() -> ValidationResult.Invalid("password cannot be null")
                else -> when {
                    userDao.getUser(username = usersignuprequest.userName)!=null -> {
                        ValidationResult.Invalid("Unable to signup due to username already reserved")
                    }
                    else -> {
                        ValidationResult.Valid
                    }
                }
            }
        }*/
        /*validate<BlogRequest>{
                blogRequest ->
            when{
                blogRequest.title.isBlank()-> ValidationResult.Invalid("Title cannot be null")
                blogRequest.description.isBlank()-> ValidationResult.Invalid("description cannot be null")
                else-> ValidationResult.Valid

            }
        }

        validate<BlogUpdateRequest>{
                blogUpdateRequest ->
            when{
                blogUpdateRequest.title.isBlank()-> ValidationResult.Invalid("title cannot be null")
                blogUpdateRequest.blogId<0-> ValidationResult.Invalid("Blog id cannot be less than 0")
                blogUpdateRequest.description.isBlank()-> ValidationResult.Invalid("description cannot be null")
                else-> ValidationResult.Valid
            }
        }*/
    }
}