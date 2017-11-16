package com.reactore.core

import slick.jdbc.PostgresProfile.api._

trait DbUrlAccessPropertiesForVehicleOfFeatures {
  val url      = "jdbc:postgresql://localhost:5432/reactore-vehicle"
  val userName = "postgres"
  val pwd      = "admin"
  val driver   = "org.postgresql.Driver"
  lazy val db = Database.forURL(url, user = userName, password = pwd, driver = driver)
}