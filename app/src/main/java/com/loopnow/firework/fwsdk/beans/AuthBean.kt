package com.loopnow.firework.fwsdk.beans

data class AuthBean(
    val access_token: String,
    val created_at: String,
    val id_token: String,
    val refresh_token: String,
    val refresh_token_expires_in: Int=5184000,
    val scope: String,
    val token_expires_in: Int=1800,
    val token_type: String
)