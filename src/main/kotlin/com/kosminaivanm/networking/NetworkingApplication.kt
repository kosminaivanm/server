package com.kosminaivanm.networking

import com.github.javafaker.Faker
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@SpringBootApplication
class NetworkingApplication

fun main(args: Array<String>) {
    runApplication<NetworkingApplication>(*args)
}

@RestController
class TasksController {
    val eventRepository = mutableListOf(
        Event(
            id = 1,
            name = "Music Festival",
            startDate = 1660512000000L,
            endDate = 1660771199000L,
            description = "Enjoy live performances from popular bands!",
            status = 1,
            photos = listOf("photo1.jpg", "photo2.jpg", "photo3.jpg"),
            category = listOf("1", "2"),
            createAt = 1660272000000L,
            phone = "1234567890",
            address = "123 Main Street, City",
            organisation = "Music Events Inc."
        ), Event(
            id = 2,
            name = "Art Exhibition",
            startDate = 1660512000000L,
            endDate = 1660771199000L,
            description = "Explore stunning artworks by renowned artists!",
            status = 1,
            photos = listOf("art1.jpg", "art2.jpg", "art3.jpg"),
            category = listOf("2", "3"),
            createAt = 1660272000000L,
            phone = "0987654321",
            address = "456 Elm Street, City",
            organisation = "Art Gallery Ltd."
        ), Event(
            id = 3,
            name = "Food Festival",
            startDate = 1660512000000L,
            endDate = 1660771199000L,
            description = "Savor delicious dishes from local restaurants!",
            status = 1,
            photos = listOf("food1.jpg", "food2.jpg", "food3.jpg"),
            category = listOf("3", "4"),
            createAt = 1660272000000L,
            phone = "9876543210",
            address = "789 Oak Street, City",
            organisation = "Food Events Co."
        ), Event(
            id = 4,
            name = "Sports Tournament",
            startDate = 1660512000000L,
            endDate = 1660771199000L,
            description = "Witness intense competitions and cheer for your favorite teams!",
            status = 1,
            photos = listOf("sports1.jpg", "sports2.jpg", "sports3.jpg"),
            category = listOf("4", "5"),
            createAt = 1660272000000L,
            phone = "0123456789",
            address = "987 Maple Street, City",
            organisation = "Sports Association"
        ), Event(
            id = 5,
            name = "Tech Conference",
            startDate = 1660512000000L,
            endDate = 1660771199000L,
            description = "Explore the latest trends in technology and network with industry experts!",
            status = 1,
            photos = listOf("tech1.jpg", "tech2.jpg", "tech3.jpg"),
            category = listOf("5", "6"),
            createAt = 1660272000000L,
            phone = "4567890123",
            address = "321 Pine Street, City",
            organisation = "Tech Events Corp."
        )
    )
    val categoryRepositories = mutableListOf(
        Category(
            id = 1,
            nameEn = "Sunflower",
            name = "Подсолнух",
            image = "sunflower.jpg"
        ), Category(
            id = 2,
            nameEn = "Rose",
            name = "Роза",
            image = "rose.jpg"
        ), Category(
            id = 3,
            nameEn = "Tulip",
            name = "Тюльпан",
            image = "tulip.jpg"
        ), Category(
            id = 4,
            nameEn = "Lily",
            name = "Лилия",
            image = "lily.jpg"
        ), Category(
            id = 5,
            nameEn = "Daisy",
            name = "Маргаритка",
            image = "daisy.jpg"
        )
    )

    val faker: Faker = Faker()

    @GetMapping("/categories")
    fun getCategories(): Iterable<Category> {
        return categoryRepositories
    }

    data class EventRequestBody(val id: String?)
    @PostMapping("/events")
    fun postEvent(@RequestBody eventRequestBody: EventRequestBody): Iterable<Event> {
        val id = eventRequestBody.id ?: return eventRepository
        return eventRepository.filter { it.category.contains(id) }
    }



    @DeleteMapping("/task/{id}")
    fun deleteTask(@PathVariable id: Long) {
        //eventRepository.deleteById(id)
    }

    @PostMapping("/categories/add")
    fun postCategories(): Category {
        val category = Category(
            0,
            faker.name().firstName(),
            faker.name().username(),
            faker.address().firstName()
        )
        categoryRepositories.add(category)
        return category
    }



    @ExceptionHandler
    fun handleTaskNotFoundException(ex: EventNotFoundException): ResponseEntity<ErrorMessage> {
        val errorMessage = ErrorMessage(
            ex.message, HttpStatus.NOT_FOUND.toString(),
        )
        return ResponseEntity(errorMessage, HttpStatus.NOT_FOUND)
    }
}

class ErrorMessage(
    var message: String?,
    var status: String
)

class EventNotFoundException(id: Long) :
    Exception("Event with id = $id not found")

@Entity
data class Event(
    @Id @GeneratedValue val id: Long,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val description: String,
    val status: Long,
    @ElementCollection val photos: List<String>,
    @ElementCollection val category: List<String>,
    val createAt: Long,
    val phone: String,
    val address: String,
    val organisation: String
)

@Entity
data class Category(
    @Id @GeneratedValue val id: Long,
    val nameEn: String,
    val name: String,
    val image: String
)
