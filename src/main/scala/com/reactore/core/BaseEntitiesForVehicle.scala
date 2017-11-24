package com.reactore.core

case class Company(companyId: Long, name: String, description: Option[String] = None, licenseNumber: String, countryId: Long, startYear: java.sql.Timestamp)

case class VehicleCategory(vehicleCategoryId: Long, name: String, description: Option[String] = None, maxCapacity: Double)

case class VehicleType(vehicleTypeId: Long, name: String, description: Option[String] = None, vehicleCategoryId: Long)

case class Country(countryId: Long, name: String, language: String, code: String)

case class Vehicle(vehicleId: Long, name: String, description: Option[String] = None, modelNumber: String, vehicleTypeId: Long, companyId: Long, quantity: Long, weight: Long)

case class VehicleContainerGroupByCompany(id: Long, seqVehicle: Seq[Vehicle])

case class VehicleContainerGroupByCategoryId(id: Long, seqVehicle: Seq[Vehicle])

case class NewDetailsForVehicle(description: Option[String] = None, quantity: Long, weight: Long)