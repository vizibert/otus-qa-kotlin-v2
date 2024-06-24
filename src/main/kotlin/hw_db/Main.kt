package hw_db

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

suspend fun getUniversityList(): List<University> {
    print("Введите страну: ")

    val countryInput = readlnOrNull()?.encodeURLPath()

    val client = HttpClient(CIO) {
        install(Logging) {
            level = LogLevel.ALL
        }

        install(ContentNegotiation) {
            json()
        }

        defaultRequest {
            url("http://universities.hipolabs.com/")
        }
    }

    val response: HttpResponse = client.get("search?country=$countryInput")
    val responseBody = response.bodyAsText()
    val universityList = Json.decodeFromString<List<University>>(responseBody)


    client.close()

    return universityList
}

fun main() = runBlocking {
    Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")

    val universities = getUniversityList()

    transaction {
        SchemaUtils.create(Universities)

        universities.forEach { university ->
            Universities.insert {
                it[name] = university.name
                it[country] = university.country
                it[webPages] = university.web_pages.joinToString(",")
                it[alphaTwoCode] = university.alpha_two_code
                it[stateProvince] = university.state_province ?: ""
                it[domains] = university.domains.joinToString(",")
            }
        }

        val query = Universities.selectAll()

        query.forEach {
            println("Name: ${it[Universities.name]}")
        }
    }
}