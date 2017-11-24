package com.reactore.features

import java.util.Date

import akka.http.scaladsl.server.Route
import com.reactore.core._
import org.json4s.native.Serialization._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class VehicleService {
  self: VehicleFacadeComponent =>

  def insertDataOnlyForVehicel(insertVehicleEntity: Vehicle): Future[Int] = {
    for {
      companyTableWithOutFuture <- companyRepository.companyFuture
      vehicleTypeTableWithOutFuture <- vehicleTypeRepository.vehicleTypeFuture
      vehicleTableWithOutFuture <- vehiclesRepository.vehiclesFuture
      _ = if (insertVehicleEntity.name.isEmpty) throw GenericException(message = "The Vehicle Name Is Empty", exception = new Exception)
      finalResult <- if (insertVehicleEntity.modelNumber.nonEmpty) {
        val resultsOfFilteredVehicleForModel: Seq[Vehicle] = vehicleTableWithOutFuture.filter(_.modelNumber == insertVehicleEntity.modelNumber)
        if (resultsOfFilteredVehicleForModel.isEmpty) {
          if (vehicleTypeTableWithOutFuture.nonEmpty) {
            val resOfVehicleType: Seq[VehicleType] = vehicleTypeTableWithOutFuture.filter(_.vehicleTypeId == insertVehicleEntity.vehicleTypeId)
            if (resOfVehicleType.nonEmpty) {
              if (companyTableWithOutFuture.nonEmpty) {
                val resOfCompany: Seq[Company] = companyTableWithOutFuture.filter(_.companyId == insertVehicleEntity.companyId)
                if (resOfCompany.nonEmpty) {
                  vehiclesRepository.internalInsert(insertVehicleEntity)
                } else Future.failed(ForeignKeyException(message = "ForEign Key Violation For Company Id", exception = new Exception))
              } else Future.failed(EmptyListException(message = "The Company Table Is Empty In The Database", exception = new Exception))
            } else Future.failed(ForeignKeyException(message = "ForEign Key Violation", exception = new Exception))
          } else Future.failed(EmptyListException(message = "The  vehicle Type Table Is Empty In The Database", exception = new Exception))
        } else Future.failed(UniqueKeyViolationException(message = "User Entered Model number Is Not Unique In The DataBase", exception = new Exception))
      } else Future.failed(GenericException(message = "The Vehicle Model Number Is Empty", exception = new Exception))
    } yield finalResult
  }

  def updateOperationForVehicle(vehicleId: Long, updateVehicleEntity: Vehicle): Future[Int] = {
    for {
      vehicleTableWithOutFuture <- vehiclesRepository.vehiclesFuture
      companyTableWithOutFuture <- companyRepository.companyFuture
      vehicleTypeTableWithOutFuture <- vehicleTypeRepository.vehicleTypeFuture

      finalResult <- if (vehicleTableWithOutFuture.nonEmpty) {
        val resOfValidId: Seq[Vehicle] = vehicleTableWithOutFuture.filter(_.vehicleId == vehicleId)
        if (resOfValidId.nonEmpty) {
          if (updateVehicleEntity.name.nonEmpty) {
            if (updateVehicleEntity.modelNumber.nonEmpty) {
              val uniqueCheckForVehicleModelNumber: Seq[Vehicle] = vehicleTableWithOutFuture.filter(_.modelNumber.toLowerCase == updateVehicleEntity.modelNumber.toLowerCase)
              if (uniqueCheckForVehicleModelNumber.size == 1) {
                if (vehicleTypeTableWithOutFuture.nonEmpty) {
                  val resOfVehicleType: Seq[VehicleType] = vehicleTypeTableWithOutFuture.filter(_.vehicleTypeId == updateVehicleEntity.vehicleTypeId)
                  if (resOfVehicleType.nonEmpty) {
                    if (companyTableWithOutFuture.nonEmpty) {
                      val resOfCompany: Seq[Company] = companyTableWithOutFuture.filter(_.companyId == updateVehicleEntity.companyId)
                      if (resOfCompany.nonEmpty) {
                        vehiclesRepository.internalUpdate(vehicleId, updateVehicleEntity)
                      } else Future.failed(ForeignKeyException(message = "ForEign Key Violation For Company Id", exception = new Exception))
                    } else Future.failed(EmptyListException(message = "The Company Table Is Empty In The Database", exception = new Exception))
                  } else Future.failed(ForeignKeyException(message = "ForEign Key Violation", exception = new Exception))
                } else Future.failed(EmptyListException(message = "The  vehicle Type Table Is Empty In The Database", exception = new Exception))
              } else Future.failed(UniqueKeyViolationException(message = "The Vehicle Model Number is Not Unique", exception = new Exception))
            } else Future.failed(GenericException(message = "The Vehicle Model Number Is Empty", exception = new Exception))
          } else Future.failed(GenericException(message = "The Vehicle Name Is Empty", exception = new Exception))
        } else Future.failed(InvalidIdException(message = "User Entered The Invalid Primary Id For Update Vehicle Table", exception = new Exception))
      } else Future.failed(EmptyListException(message = "The  vehicle Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }


  def readDataForVehicle(vehicleId: Long): Future[Vehicle] = {
    for {
      vehicleTableWithOutFuture <- vehiclesRepository.vehiclesFuture
      finalResult = if (vehicleTableWithOutFuture.nonEmpty) {
        val optionOfVehicle: Option[Vehicle] = vehicleTableWithOutFuture.find(_.vehicleId == vehicleId)
        if (optionOfVehicle.isDefined) {
          optionOfVehicle.get
        } else throw InvalidIdException(message = "vehicle Id Is Invalid And Not Match In The Database", exception = new Exception)
      } else throw EmptyListException(message = "Vehicle  Table Is Empty In The Database", exception = new Exception)
    } yield finalResult
  }

  def deleteVehicleRowById(deleteVehicleId: Long): Future[Int] = {
    for {
      vehicleTableWithOutFuture <- vehiclesRepository.vehiclesFuture
      finalResult <- if (vehicleTableWithOutFuture.nonEmpty) {
        val validVehicleId: Option[Vehicle] = vehicleTableWithOutFuture.find(_.vehicleId == deleteVehicleId)
        if (validVehicleId.isDefined) {
          vehiclesRepository.internalDelete(deleteVehicleId)
        } else Future.failed(InvalidIdException(message = "User Entered The Invalid Id For The Vehicle Table", exception = new Exception))
      } else Future.failed(EmptyListException(message = "Vehicle  Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

  def getAllVehicleData: Future[Seq[Vehicle]] = {
    for {
      vehicleWithOutFuture <- vehiclesRepository.vehiclesFuture
      _ = if (vehicleWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Table Is Empty In The Database", exception = new Exception)
      finalResult = vehicleWithOutFuture
    } yield finalResult
  }

  /** 1. Create a method to group vehicles based on company and return the details **/
  def groupVehiclesBasedOnCompany: Future[List[VehicleContainerGroupByCompany]] = {
    for {
      vehicleWithOutFuture <- vehiclesRepository.vehiclesFuture
      _ = if (vehicleWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Table Is Empty In The Database", exception = new Exception)
      finalResult = vehicleWithOutFuture.groupBy(_.companyId).map { case (company, vehicle) =>
        VehicleContainerGroupByCompany(company, vehicle)
      }.toList
    } yield finalResult
  }

  /** 2)) Create a method to give vehicles based on vehicleCategory * */
  def giveVehiclesBasedOnVehicleCategoryByCategoryId(categoryId: Long): Future[Seq[Vehicle]] = {
    for {
      vehicleWithOutFuture <- vehiclesRepository.vehiclesFuture
      vehicleTypeTableWithOutFuture <- vehicleTypeRepository.vehicleTypeFuture

      _ = if (vehicleWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Table Is Empty In The Database", exception = new Exception)
      _ = if (vehicleTypeTableWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Type  Table Is Empty In The Database", exception = new Exception)

      seqOfId = vehicleTypeTableWithOutFuture.filter(_.vehicleCategoryId == categoryId).map(_.vehicleTypeId)

      finalResult = if (seqOfId.nonEmpty) {
        val resOfSeqVehicle: Seq[Vehicle] = vehicleWithOutFuture.filter(vehicle => seqOfId.contains(vehicle.vehicleTypeId))
        if (resOfSeqVehicle.nonEmpty) {
          resOfSeqVehicle
        } else throw NoSuchEntityException(message = "No Such Entity Exception For This Id", exception = new Exception)
      } else throw InvalidIdException(message = "User Entered The Invalid Id For The Vehicle Type Table", exception = new Exception)
    } yield finalResult
  }

  /** 4)) Create a method to group vehicles based on vehiclecategory and return the details **/
  def groupVehiclesBasedOnVehicleCategory: Future[List[VehicleContainerGroupByCategoryId]] = {
    for {
      vehicleWithOutFuture <- vehiclesRepository.vehiclesFuture
      vehicleTypeTableWithOutFuture <- vehicleTypeRepository.vehicleTypeFuture

      _ = if (vehicleWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Table Is Empty In The Database", exception = new Exception)
      _ = if (vehicleTypeTableWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Type  Table Is Empty In The Database", exception = new Exception)

      finalResult = vehicleTypeTableWithOutFuture.groupBy(_.vehicleCategoryId).map { case (categoryId, vehicleCategoryValue) =>
        val seqOfLong: Seq[Long] = vehicleCategoryValue.map(_.vehicleTypeId)
        val seqOfVehicle: Seq[Vehicle] = vehicleWithOutFuture.filter(vehicle => seqOfLong.contains(vehicle.vehicleTypeId))
        VehicleContainerGroupByCategoryId(categoryId, seqOfVehicle)
      }.toList
    } yield finalResult
  }

  /** 5)) Create a method to get all the vehicles with maxCapacity greater than given value **/
  def getAllVehiclesWithMaxCapacity(userValue: Double): Future[Seq[Vehicle]] = {
    for {
      vehicleWithOutFuture <- vehiclesRepository.vehiclesFuture
      vehicleTypeTableWithOutFuture <- vehicleTypeRepository.vehicleTypeFuture
      vehicleCategoryWithOutFuture <- vehicleCategoryRepository.vehicleCategoryFuture

      _ = if (vehicleWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Table Is Empty In The Database", exception = new Exception)
      _ = if (vehicleTypeTableWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Type  Table Is Empty In The Database", exception = new Exception)
      _ = if (vehicleCategoryWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Category  Table Is Empty In The Database", exception = new Exception)

      resOfVehicleCategory = vehicleCategoryWithOutFuture.filter(_.maxCapacity > userValue)
      finalResult = if (resOfVehicleCategory.nonEmpty) {
        val seqOfLongForCategoryId: Seq[Long] = resOfVehicleCategory.map(_.vehicleCategoryId)
        val resOfVehicleType: Seq[VehicleType] = vehicleTypeTableWithOutFuture.filter(vehicleType => seqOfLongForCategoryId.contains(vehicleType.vehicleCategoryId))
        if (resOfVehicleType.nonEmpty) {
          val seqOfLongValues: Seq[Long] = resOfVehicleType.map(_.vehicleTypeId)
          val resOfVehicle: Seq[Vehicle] = vehicleWithOutFuture.filter(vehicle => seqOfLongValues.contains(vehicle.vehicleTypeId))
          if (resOfVehicle.nonEmpty) {
            resOfVehicle
          } else throw NoSuchEntityException(message = "Vehicle  Table Is Empty For This User Value", exception = new Exception)
        } else throw NoSuchEntityException(message = "Vehicle Type  Table Is Empty For This User Value", exception = new Exception)
      } else throw NoSuchEntityException(message = "Vehicle Category  Table Is Empty For This User Value", exception = new Exception)
    } yield finalResult
  }

  /** 7)) Create a method to get number of vehicles for given country  **/
  def getNumberOfVehiclesForFivenCountry(userCountryId: Long): Future[Int] = {
    for {
      vehicleWithOutFuture <- vehiclesRepository.vehiclesFuture
      companyTableWithOutFuture <- companyRepository.companyFuture

      _ = if (vehicleWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Table Is Empty In The Database", exception = new Exception)
      _ = if (companyTableWithOutFuture.isEmpty) throw EmptyListException(message = "Company Table Is Empty In The Database", exception = new Exception)
      seqOfCompanyId = companyTableWithOutFuture.filter(_.countryId == userCountryId).map(_.companyId)

      finalResult = if (seqOfCompanyId.nonEmpty) {
        val numberOfVehicles: Int = vehicleWithOutFuture.count(vehicle => seqOfCompanyId.contains(vehicle.companyId))
        if (numberOfVehicles > 0) {
          numberOfVehicles
        } else throw NoSuchEntityException(message = "There Is No Entity For This Country Id", exception = new Exception)
      } else throw InvalidIdException(message = "User Entered The Invalid Country Id For The Company Table", exception = new Exception)
    } yield finalResult
  }

  /** 8)) Create a method to update quantity of a particular vehicle , weight and
    * description by taking these values as input **/
  def updateVehicleDetailsBasedOnInput(vehiclePrimaryId: Long, newDetailsForVehicle: NewDetailsForVehicle): Future[Int] = {
    for {
      vehicleWithOutFuture <- vehiclesRepository.vehiclesFuture
      _ = if (vehicleWithOutFuture.isEmpty) throw EmptyListException(message = "Vehicle Table Is Empty In The Database", exception = new Exception)
      optionOfVehicle = vehicleWithOutFuture.find(_.vehicleId == vehiclePrimaryId)

      finalResult <- if (optionOfVehicle.isDefined) {
        val resOfVehicle: Vehicle = optionOfVehicle.get
        val resultOfVehicleWithCopied: Vehicle = resOfVehicle.copy(description = newDetailsForVehicle.description, quantity = newDetailsForVehicle.quantity, weight = newDetailsForVehicle.weight)
        vehiclesRepository.internalUpdate(vehiclePrimaryId, resultOfVehicleWithCopied)
      } else Future.failed(InvalidIdException(message = "User Entered The Invalid Id For The Vehicle Table", exception = new Exception))
    } yield finalResult
  }

  /** 6)) Create a method to get all the vehicles which belong to a company older than given years **/
  def getAllVehiclesBasedOnYears(givenYears: Long): Future[Seq[Vehicle]] = {
    val currentDate: Date = new java.util.Date()
    for {
      vehicleWithOutFuture <- vehiclesRepository.vehiclesFuture
      companyTableWithOutFuture <- companyRepository.companyFuture

      finalResult = if (companyTableWithOutFuture.nonEmpty) {
        val seqOfCompanyId: Seq[Long] = companyTableWithOutFuture.filter(company => (currentDate.getYear - company.startYear.getYear) > givenYears).map(_.companyId)
        if (vehicleWithOutFuture.nonEmpty) {
          val resultOfVehicle: Seq[Vehicle] = vehicleWithOutFuture.filter(vehicle => seqOfCompanyId.contains(vehicle.companyId))
          if (resultOfVehicle.nonEmpty) {
            resultOfVehicle
          } else throw NoSuchEntityException(message = "There Is No Vehicle For This Company Based On Years In The Database", exception = new Exception)
        } else throw EmptyListException(message = "Vehicle  Table Is Empty In The Database", exception = new Exception)
      } else throw EmptyListException(message = "Company  Table Is Empty In The Database", exception = new Exception)
    } yield finalResult
  }
}


class VehicleRoute(vehicleService: VehicleService) extends DirectiveWithGenericErrorHandling {
  val route1: Route = pathPrefix("vehicle") {
    path("readVehicleData" / LongNumber) { id =>
      get {
        complete(respond(vehicleService.readDataForVehicle(id)))
      }
    } ~ path("deleteVehicleData" / LongNumber) { id =>
      delete {
        complete(respond(vehicleService.deleteVehicleRowById(id)))
      }
    } ~ path("insertDataForVehicle") {
      post {
        entity(as[String]) { postBody =>
          val entity: Vehicle = read[Vehicle](postBody)
          complete(respond(vehicleService.insertDataOnlyForVehicel(entity)))
        }
      }
    } ~ path("updateOperationForVehicle" / LongNumber) { updatePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val entity: Vehicle = read[Vehicle](postBody)
          complete(respond(vehicleService.updateOperationForVehicle(updatePrimaryId, entity)))
        }
      }
    } ~ path("getAllVehicleData") {
      get {
        complete(respond(vehicleService.getAllVehicleData))
      }
    } ~ path("groupVehiclesBasedOnCompany") {
      get {
        complete(respond(vehicleService.groupVehiclesBasedOnCompany))
      }
    } ~ path("giveVehiclesBasedOnVehicleCategoryByCategoryId" / LongNumber) { categoryId =>
      get {
        complete(respond(vehicleService.giveVehiclesBasedOnVehicleCategoryByCategoryId(categoryId)))
      }
    } ~ path("groupVehiclesBasedOnVehicleCategory") {
      get {
        complete(respond(vehicleService.groupVehiclesBasedOnVehicleCategory))
      }
    } ~ path("getAllVehiclesWithMaxCapacity" / DoubleNumber) { doubleNumberCapacity =>
      get {
        complete(respond(vehicleService.getAllVehiclesWithMaxCapacity(doubleNumberCapacity)))
      }
    } ~ path("getNumberOfVehiclesForFivenCountry" / LongNumber) { userCountryId =>
      get {
        complete(respond(vehicleService.getNumberOfVehiclesForFivenCountry(userCountryId)))
      }
    } ~ path("updateVehicleDetailsBasedOnInput" / LongNumber) { vehiclePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val entity: NewDetailsForVehicle = read[NewDetailsForVehicle](postBody)
          complete(respond(vehicleService.updateVehicleDetailsBasedOnInput(vehiclePrimaryId, entity)))
        }
      }
    } ~ path("getAllVehiclesBasedOnYears" / LongNumber) { age =>
      get {
        complete(respond(vehicleService.getAllVehiclesBasedOnYears(age)))
      }
    }
  } //endOfPathPrefix
}