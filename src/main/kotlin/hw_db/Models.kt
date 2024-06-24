package hw_db

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class University(
    val name: String,
    val alpha_two_code: String,
    val country: String,
    val web_pages: List<String>,
    @SerialName("state-province") val state_province: String?,
    val domains: List<String>
)