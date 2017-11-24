package com.reactore.bookcore

import slick.jdbc.PostgresProfile.api._

trait DbUrlAccessPropertiesForBook {
  val url      = "jdbc:postgresql://localhost:5432/book-database"
  val userName = "postgres"
  val pwd      = "admin"
  val driver   = "org.postgresql.Driver"
  lazy val db = Database.forURL(url, user = userName, password = pwd, driver = driver)
}