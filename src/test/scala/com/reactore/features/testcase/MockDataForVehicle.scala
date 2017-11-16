package com.reactore.features.testcase

import java.sql.Timestamp

import com.reactore.core._

trait MockDataForGlobalTestFeatures {
  val company1    : Company      = Company(1, "TVS", None, "clnTVS151", 1, Timestamp.valueOf("1990-11-16 06:55:40.11"))
  val company2    : Company      = Company(2, "Benz", None, "clnBENZ152", 2, Timestamp.valueOf("1992-11-16 06:55:40.11"))
  val company3    : Company      = Company(3, "Honda", None, "clmHonda153", 3, Timestamp.valueOf("1994-11-16 06:55:40.11"))
  val company4    : Company      = Company(4, "Nissan", None, "clnNISSAN154", 4, Timestamp.valueOf("1996-11-16 06:55:40.11"))
  val company5    : Company      = Company(5, "BMW", None, "clnBMW155", 5, Timestamp.valueOf("1998-11-16 06:55:40.11"))
  val company6    : Company      = Company(6, "Suzuki", None, "clnSuzuki156", 5, Timestamp.valueOf("2002-11-16 06:55:40.11"))
  val seqOfCompany: Seq[Company] = Seq(company1, company2, company3, company4, company5, company6)

  val country1     = Country(1, "India", "Hindi", "091")
  val country2     = Country(2, "Australia", "English", "092")
  val country3     = Country(3, "America", "American English", "093")
  val country4     = Country(4, "Switzerland", "Swiss Language", "094")
  val country5     = Country(5, "China", "Chinese Language", "095")
  val country6     = Country(6, "Canada", "Canada Language", "096")
  val seqOfCountry = Seq(country1, country2, country3, country4, country5, country6)

  val vehicle1     = Vehicle(1, "RAJAN BUS SERVICE", None, "mnbus501", 2, 1, 3, 3000)
  val vehicle2     = Vehicle(2, "Nandu BUS SERVICE", None, "mnbus502", 2, 1, 4, 4000)
  val seqOfVehicle = Seq(vehicle1, vehicle2)

  val vehicleCategory1     = VehicleCategory(1, "2Wheeler", None, 400)
  val vehicleCategory2     = VehicleCategory(2, "3Wheeler", None, 600)
  val vehicleCategory3     = VehicleCategory(3, "4Wheeler", None, 800)
  val vehicleCategory4     = VehicleCategory(4, "6Wheeler", None, 950)
  val vehicleCategory5     = VehicleCategory(5, "8Wheeler", None, 1250)
  val seqOfVehicleCategory = Seq(vehicleCategory1, vehicleCategory2, vehicleCategory3, vehicleCategory4, vehicleCategory5)

  val vehicleType1     = VehicleType(1, "Scooter", None, 1)
  val vehicleType2     = VehicleType(2, "Bus", None, 3)
  val vehicleType3     = VehicleType(3, "auto-rikshaw", None, 2)
  val vehicleType4     = VehicleType(4, "truck", None, 4)
  val vehicleType5     = VehicleType(5, "DUMPER", None, 3)
  val seqOfVehicleType = Seq(vehicleType1, vehicleType2, vehicleType3, vehicleType4, vehicleType5)
}