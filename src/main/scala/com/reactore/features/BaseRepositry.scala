package com.reactore.features

import com.reactore.core._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

class CompanyRepository extends DbUrlAccessPropertiesForVehicleOfFeatures with BaseTables {
  def companyFuture: Future[Seq[Company]] = db.run(companyTable.result)

  def internalDelete(deleteCompanyId: Long): Future[Int] = {
    db.run(companyTable.filter(_.companyid === deleteCompanyId).delete)
  }

  def internalUpdate(id: Long, companyEntity: Company): Future[Int] = {
    db.run(companyTable.filter(_.companyid === id).update(companyEntity))
  }

  def internalInsert(companyEntity: Company): Future[Int] = {
    db.run(companyTable += companyEntity)
  }
}

object ImplCompanyRepository extends CompanyRepository


class CountryRepository extends DbUrlAccessPropertiesForVehicleOfFeatures with BaseTables {
  def countryFuture: Future[Seq[Country]] = db.run(countryTable.result)

  def internalDelete(id: Long): Future[Int] = {
    db.run(countryTable.filter(_.countryid === id).delete)
  }

  def internalUpdate(id: Long, countryEntity: Country): Future[Int] = {
    db.run(countryTable.filter(_.countryid === id).update(countryEntity))
  }

  def internalInsert(entity: Country): Future[Int] = {
    db.run(countryTable += entity)
  }

}

object ImplCountryRepository extends CountryRepository


class VehiclesRepository extends DbUrlAccessPropertiesForVehicleOfFeatures with BaseTables {
  def vehiclesFuture: Future[Seq[Vehicle]] = db.run(vehicleTable.result)

  def internalDelete(deleteVehicleId: Long): Future[Int] = {
    db.run(vehicleTable.filter(_.vehicleid === deleteVehicleId).delete)
  }

  def internalDeleteForListOfId(deleteVehicleListId: Seq[Long]): Future[Int] = {
    db.run(vehicleTable.filter(_.vehicleid inSetBind deleteVehicleListId).delete)
  }

  def internalUpdate(id: Long, vehicleEntity: Vehicle): Future[Int] = {
    db.run(vehicleTable.filter(_.vehicleid === id).update(vehicleEntity))
  }

  def internalInsert(vehicleEntity: Vehicle): Future[Int] = {
    db.run(vehicleTable += vehicleEntity)
  }
}

object ImplVehiclesRepository extends VehiclesRepository


class VehicleTypeRepository extends DbUrlAccessPropertiesForVehicleOfFeatures with BaseTables {
  def vehicleTypeFuture: Future[Seq[VehicleType]] = db.run(vehicleTypeTable.result)

  def internalDelete(id: Long): Future[Int] = {
    db.run(vehicleTypeTable.filter(_.vehicletypeid === id).delete)
  }

  def internalUpdate(id: Long, entity: VehicleType): Future[Int] = {
    db.run(vehicleTypeTable.filter(_.vehicletypeid === id).update(entity))
  }

  def internalInsert(entity: VehicleType): Future[Int] = {
    db.run(vehicleTypeTable += entity)
  }

}

object ImplVehicleTypeRepository extends VehicleTypeRepository


class VehicleCategoryRepository extends DbUrlAccessPropertiesForVehicleOfFeatures with BaseTables {
  def vehicleCategoryFuture: Future[Seq[VehicleCategory]] = db.run(vehicleCategoryTable.result)

  def internalDelete(id: Long): Future[Int] = {
    db.run(vehicleCategoryTable.filter(_.vehiclecategoryid === id).delete)
  }

  def internalUpdate(id: Long, entity: VehicleCategory): Future[Int] = {
    db.run(vehicleCategoryTable.filter(_.vehiclecategoryid === id).update(entity))
  }

  def internalInsert(entity: VehicleCategory): Future[Int] = {
    db.run(vehicleCategoryTable += entity)
  }

}

object ImplVehicleCategoryRepository extends VehicleCategoryRepository