package com.reactore.bookcore

import slick.jdbc.PostgresProfile

object MyPostgresDriver extends PostgresProfile

trait DBFiles {

  import MyPostgresDriver.api._

  val url                                            = "jdbc:postgresql://localhost:5432/book-database"
  val database: MyPostgresDriver.backend.DatabaseDef = Database.forURL(url,
    user = "postgres", password = "admin", driver = "org.postgresql.Driver")
}