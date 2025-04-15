package ru.prod.application.utils.profile

import ru.prod.application.utils.Book

data class ProfileModel(
    val username: String,
    val books: List<Book>
)
