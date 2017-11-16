package com.reactore.features

import akka.http.scaladsl.server.Route
import com.reactore.core._
import org.json4s.native.Serialization._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class VehicleCategoryService {
  self: VehicleCategoryFacadeComponent =>


  def insertDataOnlyForVehicelCategory(insertVehicleCategoryEntity: VehicleCategory): Future[Int] = {
    if (insertVehicleCategoryEntity.name.nonEmpty) {
      vehicleCategoryRepository.internalInsert(insertVehicleCategoryEntity)
    } else Future.failed(GenericException(message = "The Vehicle Category Name Is Empty", exception = new Exception))
  }


  def updateOperationForVehicleCategory(vehicleCategoryId: Long, updateVehicleCategoryEntity: VehicleCategory): Future[Int] = {
    for {
      vehicleCategoryTableWithOutFuture <- vehicleCategoryRepository.vehicleCategoryFuture
      finalResult <- if (vehicleCategoryTableWithOutFuture.nonEmpty) {
        if (updateVehicleCategoryEntity.name.nonEmpty) {
          val resultOfVehicleCategory: Seq[VehicleCategory] = vehicleCategoryTableWithOutFuture.filter(_.vehicleCategoryId == vehicleCategoryId)
          if (resultOfVehicleCategory.nonEmpty) {
            vehicleCategoryRepository.internalUpdate(vehicleCategoryId, updateVehicleCategoryEntity)
          } else Future.failed(InvalidIdException(message = "User Entered The Invalid Id For Update VehicleCategory", exception = new Exception))
        } else Future.failed(GenericException(message = "The Category Name is Not Defined", exception = new Exception))
      } else Future.failed(EmptyListException(message = "Vehicle Category Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

  def readDataForVehicelCategory(vehicleCategoryId: Long): Future[VehicleCategory] = {
    for {
      vehicleCategoryTableWithOutFuture <- vehicleCategoryRepository.vehicleCategoryFuture
      finalResult = if (vehicleCategoryTableWithOutFuture.nonEmpty) {
        val resultByFind = vehicleCategoryTableWithOutFuture.find(_.vehicleCategoryId == vehicleCategoryId)
        if (resultByFind.isDefined) {
          resultByFind.get
        } else throw InvalidIdException(message = "vehicleCategory Id Is Invalid And Not Match In The Database", exception = new Exception)
      } else throw EmptyListException(message = "Vehicle Category Table Is Empty In The Database", exception = new Exception)
    } yield finalResult
  }

  def deleteVehicleCategoryRowById(deleteVehicleCategoryId: Long): Future[Int] = {
    for {
      vehicleCategoryTableWithOutFuture <- vehicleCategoryRepository.vehicleCategoryFuture
      vehicleTypeTableWithOutFuture <- vehicleTypeRepository.vehicleTypeFuture

      finalResult <- if (vehicleCategoryTableWithOutFuture.nonEmpty) {
        val validVehicleCategoryId: Option[VehicleCategory] = vehicleCategoryTableWithOutFuture.find(_.vehicleCategoryId == deleteVehicleCategoryId)
        if (validVehicleCategoryId.isDefined) {
          val resOfSeqVehicleCategory: Seq[VehicleType] = vehicleTypeTableWithOutFuture.filter(_.vehicleCategoryId == deleteVehicleCategoryId)
          if (resOfSeqVehicleCategory.isEmpty) {
            vehicleCategoryRepository.internalDelete(deleteVehicleCategoryId)
          } else Future.failed(ForeignKeyException(message = "ForEign Key Relation Is Exists", exception = new Exception))
        } else Future.failed(InvalidIdException(message = "User Entered The Invalid Id For The Vehicle Category Table", exception = new Exception))
      } else Future.failed(EmptyListException(message = "Vehicle Category Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

  def getAllVehicleCategoryData: Future[Seq[VehicleCategory]] = {
    for {
      vehicleCategoryWithOutFuture <- vehicleCategoryRepository.vehicleCategoryFuture
      finalResult = vehicleCategoryWithOutFuture
    } yield finalResult
  }
}

class VehicleCategoryRoute(vehicleCategoryService: VehicleCategoryService) extends DirectiveWithGenericErrorHandling {
  val vehicleCategoryRoute: Route = pathPrefix("vehicleCategory") {
    path("readVehicleCategoryData" / LongNumber) { id =>
      get {
        complete(respond(vehicleCategoryService.readDataForVehicelCategory(id)))
      }
    } ~ path("deleteVehicleCategoryData" / LongNumber) { id =>
      delete {
        complete(respond(vehicleCategoryService.deleteVehicleCategoryRowById(id)))
      }
    } ~ path("insertDataForVehicleCategory") {
      post {
        entity(as[String]) { postBody =>
          val entity: VehicleCategory = read[VehicleCategory](postBody)
          complete(respond(vehicleCategoryService.insertDataOnlyForVehicelCategory(entity)))
        }
      }
    } ~ path("updateOperationForVehicleCategory" / LongNumber) { updatePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val entity: VehicleCategory = read[VehicleCategory](postBody)
          complete(respond(vehicleCategoryService.updateOperationForVehicleCategory(updatePrimaryId, entity)))
        }
      }
    } ~ path("getAllVehicleCategoryData") {
      get {
        complete(respond(vehicleCategoryService.getAllVehicleCategoryData))
      }
    }
  }
}
