package com.reactore.features

import akka.http.scaladsl.server.Route
import com.reactore.core._
import org.json4s.native.Serialization._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class VehicleTypeService {
  self: VehicleTypeFacadeComponent =>

  def updateOperationForVehicleType(vehicleTypeId: Long, updateVehicleTypeEntity: VehicleType): Future[Int] = {
    for {
      vehicleTypeTableWithOutFuture <- vehicleTypeRepository.vehicleTypeFuture
      vehicleCategoryTableWithOutFuture <- vehicleCategoryRepository.vehicleCategoryFuture
      _ = if (updateVehicleTypeEntity.name.isEmpty) throw GenericException(message = "The Vehicle Type Name Is Empty", exception = new Exception)
      finalResult <- if (vehicleTypeTableWithOutFuture.nonEmpty) {
        val res: Seq[VehicleType] = vehicleTypeTableWithOutFuture.filter(_.vehicleTypeId == vehicleTypeId)
        if (res.nonEmpty) {
          if (vehicleCategoryTableWithOutFuture.nonEmpty) {
            val resOfVehicleCategory: Seq[VehicleCategory] = vehicleCategoryTableWithOutFuture.filter(_.vehicleCategoryId == updateVehicleTypeEntity.vehicleCategoryId)
            if (resOfVehicleCategory.nonEmpty) {
              vehicleTypeRepository.internalUpdate(vehicleTypeId, updateVehicleTypeEntity)
            } else Future.failed(ForeignKeyException(message = "ForEign Key Violation Message", exception = new Exception))
          } else Future.failed(EmptyListException(message = "The  vehicle Category Table Is Empty In The Database", exception = new Exception))
        } else Future.failed(InvalidIdException(message = "User Entered The Invalid Primary Id For Update Vehicle Type Table", exception = new Exception))
      } else Future.failed(EmptyListException(message = "The  vehicle Type Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

  def insertDataOnlyForVehicelType(insertVehicleTypeEntity: VehicleType): Future[Int] = {
    for {
      vehicleCategoryTableWithOutFuture <- vehicleCategoryRepository.vehicleCategoryFuture
      _ = if (insertVehicleTypeEntity.name.isEmpty) throw GenericException(message = "The Vehicle Type Name Is Empty", exception = new Exception)
      finalResult <- if (vehicleCategoryTableWithOutFuture.nonEmpty) {
        val resOfVehicleCategory: Seq[VehicleCategory] = vehicleCategoryTableWithOutFuture.filter(_.vehicleCategoryId == insertVehicleTypeEntity.vehicleCategoryId)
        if (resOfVehicleCategory.nonEmpty) {
          vehicleTypeRepository.internalInsert(insertVehicleTypeEntity)
        } else Future.failed(ForeignKeyException(message = "ForEign Key Violation Message", exception = new Exception))
      } else Future.failed(EmptyListException(message = "The  vehicle Category Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

  def deleteVehicleTypeRowById(deleteVehicleTypeId: Long): Future[Int] = {
    for {
      vehicleTypeTableWithOutFuture <- vehicleTypeRepository.vehicleTypeFuture
      vehicleTableWithOutFuture <- vehicleRepository.vehiclesFuture

      finalResult <- if (vehicleTypeTableWithOutFuture.nonEmpty) {
        val validVehicleTypeId: Option[VehicleType] = vehicleTypeTableWithOutFuture.find(_.vehicleTypeId == deleteVehicleTypeId)
        if (validVehicleTypeId.isDefined) {
          val resOfSeqVehicle: Seq[Vehicle] = vehicleTableWithOutFuture.filter(_.vehicleTypeId == deleteVehicleTypeId)
          if (resOfSeqVehicle.isEmpty) {
            vehicleTypeRepository.internalDelete(deleteVehicleTypeId)
          } else Future.failed(ForeignKeyException(message = "ForEign Key Relation Is Exists", exception = new Exception))
        } else Future.failed(InvalidIdException(message = "User Entered The Invalid Id For The Vehicle Type Table", exception = new Exception))
      } else Future.failed(EmptyListException(message = "Vehicle Type Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

  def readDataForVehicelType(vehicleTypeId: Long): Future[VehicleType] = {
    for {
      vehicleTypeTableWithOutFuture <- vehicleTypeRepository.vehicleTypeFuture
      finalResult = if (vehicleTypeTableWithOutFuture.nonEmpty) {
        val resultByFind = vehicleTypeTableWithOutFuture.find(_.vehicleTypeId == vehicleTypeId)
        if (resultByFind.isDefined) {
          resultByFind.get
        } else throw InvalidIdException(message = "vehicleType Id Is Invalid And Not Match In The Database", exception = new Exception)
      } else throw EmptyListException(message = "Vehicle Type Table Is Empty In The Database", exception = new Exception)
    } yield finalResult
  }

  def getAllVehicleTypeData: Future[Seq[VehicleType]] = {
    for {
      vehicleTypeWithOutFuture <- vehicleTypeRepository.vehicleTypeFuture
      finalResult = vehicleTypeWithOutFuture
    } yield finalResult
  }
}

class VehicleTypeRoute(vehicleTypeService: VehicleTypeService) extends DirectiveWithGenericErrorHandling {
  val route1: Route = pathPrefix("vehicleType") {
    path("readVehicleTypeData" / LongNumber) { id =>
      get {
        complete(respond(vehicleTypeService.readDataForVehicelType(id)))
      }
    } ~ path("deleteVehicleTypeData" / LongNumber) { id =>
      delete {
        complete(respond(vehicleTypeService.deleteVehicleTypeRowById(id)))
      }
    } ~ path("insertVehicleTypeData") {
      post {
        entity(as[String]) { postBody =>
          val entity: VehicleType = read[VehicleType](postBody)
          complete(respond(vehicleTypeService.insertDataOnlyForVehicelType(entity)))
        }
      }
    } ~ path("updateOperationForVehicleType" / LongNumber) { updatePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val entity: VehicleType = read[VehicleType](postBody)
          complete(respond(vehicleTypeService.updateOperationForVehicleType(updatePrimaryId, entity)))
        }
      }
    } ~ path("getAllVehicleTypeData") {
      get {
        complete(respond(vehicleTypeService.getAllVehicleTypeData))
      }
    }
  }
}