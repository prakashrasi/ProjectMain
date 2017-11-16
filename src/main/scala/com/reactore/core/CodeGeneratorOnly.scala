package com.reactore.core

trait DbProperties {
  val profile  = "slick.jdbc.PostgresProfile"
  val url      = "jdbc:postgresql://localhost:5432/reactore-vehicle"
  val userName = "postgres"
  val pwd      = "admin"
  val driver   = "org.postgresql.Driver"
}

object CodeGeneration extends App with DbProperties {
  val path     : String = new java.io.File(".").getCanonicalPath
  val outputDir: String = path + "\\src\\main\\scala\\com\\reactore"
  val folder   : String = "generated"
  slick.codegen.SourceCodeGenerator.main(Array(profile, driver, url, outputDir, folder, userName, pwd))
}