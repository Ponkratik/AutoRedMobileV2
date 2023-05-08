package com.ponkratov.autored.domain.repository

import com.ponkratov.autored.domain.model.response.JwtResponse

interface SharedPrefsRepository {

    fun getJwt(): JwtResponse

    fun putJwt(jwtResponse: JwtResponse)

    fun getIdToken(): String

    fun putIdToken(idToken: String)
}