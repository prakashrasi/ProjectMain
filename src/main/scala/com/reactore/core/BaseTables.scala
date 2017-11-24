package com.reactore.core

import slick.jdbc.PostgresProfile.api._
import slick.lifted.{Rep, Tag}
import slick.model.ForeignKeyAction

trait BaseTables {

  class CompanyTable(_tableTag: Tag) extends Table[Company](_tableTag, Some("myVehicle"), "Company") {
    def * = (companyid, name, description, licencenumber, country,startyear) <> (Company.tupled, Company.unapply)

    val companyid    : Rep[Long]               = column[Long]("companyId", O.AutoInc, O.PrimaryKey)
    val name         : Rep[String]             = column[String]("name", O.Length(50, varying = true))
    val description  : Rep[Option[String]]     = column[Option[String]]("description", O.Length(150, varying = true), O.Default(None))
    val licencenumber: Rep[String]             = column[String]("licenceNumber")
    val country      : Rep[Long]               = column[Long]("country")
    val startyear    : Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("startYear")


    lazy val countryFk = foreignKey("Fkey1", country, countryTable)(r => r.countryid, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  lazy val companyTable = new TableQuery(tag => new CompanyTable(tag))


  class CountryTable(_tableTag: Tag) extends Table[Country](_tableTag, Some("myVehicle"), "Country") {
    def * = (countryid, name, language, code) <> (Country.tupled, Country.unapply)

    val countryid: Rep[Long]   = column[Long]("countryId", O.AutoInc, O.PrimaryKey)
    val name     : Rep[String] = column[String]("name", O.Length(50, varying = true))
    val language : Rep[String] = column[String]("language", O.Length(50, varying = true))
    val code     : Rep[String] = column[String]("code", O.Length(30, varying = true))
  }

  lazy val countryTable = new TableQuery(tag => new CountryTable(tag))


  class VehicleTable(_tableTag: Tag) extends Table[Vehicle](_tableTag, Some("myVehicle"), "Vehicle") {
    def * = (vehicleid, name, description, modelnumber, vehicletype, company, quantity, weight) <> (Vehicle.tupled, Vehicle.unapply)

    val vehicleid  : Rep[Long]           = column[Long]("vehicleId", O.AutoInc, O.PrimaryKey)
    val name       : Rep[String]         = column[String]("name", O.Length(60, varying = true))
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(150, varying = true), O.Default(None))
    val modelnumber: Rep[String]         = column[String]("modelNumber")
    val vehicletype: Rep[Long]           = column[Long]("vehicleType")
    val company    : Rep[Long]           = column[Long]("company")
    val quantity   : Rep[Long]           = column[Long]("quantity")
    val weight     : Rep[Long]           = column[Long]("weight")

    lazy val companyFk     = foreignKey("Fkey2", company, companyTable)(r => r.companyid, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    lazy val vehicletypeFk = foreignKey("Fkey1", vehicletype, vehicleTypeTable)(r => r.vehicletypeid, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  lazy val vehicleTable = new TableQuery(tag => new VehicleTable(tag))


  class VehicleCategoryTable(_tableTag: Tag) extends Table[VehicleCategory](_tableTag, Some("myVehicle"), "VehicleCategory") {
    def * = (vehiclecategoryid, name, description, maxcapacity) <> (VehicleCategory.tupled, VehicleCategory.unapply)

    val vehiclecategoryid: Rep[Long]           = column[Long]("vehicleCategoryId", O.AutoInc, O.PrimaryKey)
    val name             : Rep[String]         = column[String]("name", O.Length(50, varying = true))
    val description      : Rep[Option[String]] = column[Option[String]]("description", O.Length(150, varying = true), O.Default(None))
    val maxcapacity      : Rep[Double]         = column[Double]("maxCapacity")
  }

  lazy val vehicleCategoryTable = new TableQuery(tag => new VehicleCategoryTable(tag))


  class VehicleTypeTable(_tableTag: Tag) extends Table[VehicleType](_tableTag, Some("myVehicle"), "VehicleType") {
    def * = (vehicletypeid, name, description, vehiclecategoryid) <> (VehicleType.tupled, VehicleType.unapply)

    val vehicletypeid    : Rep[Long]           = column[Long]("vehicleTypeId", O.AutoInc, O.PrimaryKey)
    val name             : Rep[String]         = column[String]("name", O.Length(50, varying = true))
    val description      : Rep[Option[String]] = column[Option[String]]("description", O.Length(150, varying = true), O.Default(None))
    val vehiclecategoryid: Rep[Long]           = column[Long]("vehicleCategoryId")
    lazy val vehiclecategoryFk = foreignKey("Fkey1", vehiclecategoryid, vehicleCategoryTable)(r => r.vehiclecategoryid, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  lazy val vehicleTypeTable = new TableQuery(tag => new VehicleTypeTable(tag))



}