package hw_db

import org.jetbrains.exposed.dao.id.IntIdTable

object Universities : IntIdTable() {
    val name = varchar("name", 255)
    val country = varchar("country", 255)
    val webPages = text("web_pages")
    val alphaTwoCode = varchar("alpha_two_code", 2)
    val stateProvince = varchar("state_province", 255).nullable()
    val domains = text("domains")
}