package com.reactore.bookcore

trait DbPropertiesForBook {
  val profile  = "slick.jdbc.PostgresProfile"
  val url      = "jdbc:postgresql://localhost:5432/book-database"
  val userName = "postgres"
  val pwd      = "admin"
  val driver   = "org.postgresql.Driver"
}

object CodeGenerationForBook extends App with DbPropertiesForBook {
  val path     : String = new java.io.File(".").getCanonicalPath
  val outputDir: String = path + "\\src\\main\\scala\\com\\reactore"
  val folder   : String = "generated"
  slick.codegen.SourceCodeGenerator.main(Array(profile, driver, url, outputDir, folder, userName, pwd))
}