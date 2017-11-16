package com.reactore.features

import akka.http.scaladsl.server.Route
import com.reactore.core.{Company, _}
import org.json4s.native.Serialization._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CompanyService {
  self: CompanyFacadeComponent =>


  def insertDataOnlyForCompany(insertCompanyEntity: Company): Future[Int] = {
    for {
      countryTableWithOutFuture <- countryRepository.countryFuture
      _ = if (insertCompanyEntity.name.isEmpty) throw GenericException(message = "The Name Is Empty", exception = new Exception)
      _ = if (insertCompanyEntity.licenseNumber.isEmpty) throw GenericException(message = "The License Number Is Empty", exception = new Exception)
      finalResult <- if (countryTableWithOutFuture.nonEmpty) {
        val resOfCountry: Seq[Country] = countryTableWithOutFuture.filter(_.countryId == insertCompanyEntity.countryId)
        if (resOfCountry.nonEmpty) {
          companyRepository.internalInsert(insertCompanyEntity)
        } else Future.failed(ForeignKeyException(message = "ForEign Key Exception For Country Number", exception = new Exception))
      } else Future.failed(EmptyListException(message = "The Country Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

  def updateOperationForCompany(companyId: Long, updateCompanyEntity: Company): Future[Int] = {
    for {
      companyTableWithOutFuture <- companyRepository.companyFuture
      countryTableWithOutFuture <- countryRepository.countryFuture
      _ = if (updateCompanyEntity.name.isEmpty) throw GenericException(message = "The Name Is Empty", exception = new Exception)
      _ = if (updateCompanyEntity.licenseNumber.isEmpty) throw GenericException(message = "The License Number Is Empty", exception = new Exception)
      finalResult <- if (companyTableWithOutFuture.nonEmpty) {
        if (countryTableWithOutFuture.nonEmpty) {
          val resOfCountry: Seq[Country] = countryTableWithOutFuture.filter(_.countryId == updateCompanyEntity.countryId)
          if (resOfCountry.nonEmpty) {
            val res: Seq[Company] = companyTableWithOutFuture.filter(_.companyId == companyId)
            if (res.nonEmpty) {
              companyRepository.internalUpdate(companyId, updateCompanyEntity)
            } else Future.failed(InvalidIdException(message = "User Entered The Invalid Id For Update Company", exception = new Exception))
          } else Future.failed(ForeignKeyException(message = "ForEign Key Exception For Country Number", exception = new Exception))
        } else Future.failed(EmptyListException(message = "The Country Table Is Empty In The Database", exception = new Exception))
      } else Future.failed(EmptyListException(message = "The Company Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

  def deleteCompanyRowById(deleteCompanyId: Long): Future[Int] = {
    for {
      companyTableWithOutFuture <- companyRepository.companyFuture
      vehicleTableWithOutFuture <- vehicleRepository.vehiclesFuture

      finalResult <- if (companyTableWithOutFuture.nonEmpty) {
        val validCompanyId: Option[Company] = companyTableWithOutFuture.find(_.companyId == deleteCompanyId)
        if (validCompanyId.isDefined) {
          val resOfSeqCompany: Seq[Vehicle] = vehicleTableWithOutFuture.filter(_.companyId == deleteCompanyId)
          if (resOfSeqCompany.isEmpty) {
            companyRepository.internalDelete(deleteCompanyId)
          } else Future.failed(ForeignKeyException(message = "ForEign Key Relation Is Exists", exception = new Exception))
        } else Future.failed(InvalidIdException(message = "User Entered The Invalid Id For The Company Table", exception = new Exception))
      } else Future.failed(EmptyListException(message = "Company Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

  def readDataForCompany(companyId: Long): Future[Company] = {
    for {
      companyTableWithOutFuture <- companyRepository.companyFuture
      finalResult = if (companyTableWithOutFuture.nonEmpty) {
        val optionOfCompanyById: Option[Company] = companyTableWithOutFuture.find(_.companyId == companyId)
        if (optionOfCompanyById.isDefined) {
          optionOfCompanyById.get
        } else throw InvalidIdException(message = "Company Id Is Invalid And Not Match In The Database", exception = new Exception)
      } else throw EmptyListException(message = "Company Table Is Empty In The Database", exception = new Exception)
    } yield finalResult
  }

  def getAllCompanyData: Future[Seq[Company]] = {
    for {
      companyTableWithOutFuture <- companyRepository.companyFuture
      finalResult = companyTableWithOutFuture
    } yield finalResult
  }

  /** 9. Create a method to delete company and delete related data
    * from vehicle category , vehicleType and remaining tables **/

  def deleteCompanyRowByIdWithRelation(deleteCompanyId: Long): Future[Int] = {
    for {
      companyTableWithOutFuture <- companyRepository.companyFuture
      vehicleWithOutFuture <- vehicleRepository.vehiclesFuture

      finalResult <- if (companyTableWithOutFuture.nonEmpty) {
        val resOfOptionCompany: Option[Company] = companyTableWithOutFuture.find(_.companyId == deleteCompanyId)
        if (resOfOptionCompany.isDefined) {
          val resOfCompany: Company = resOfOptionCompany.get
          val resOfVehicle: Seq[Vehicle] = vehicleWithOutFuture.filter(_.companyId == resOfCompany.companyId)
          if (resOfVehicle.nonEmpty) {
            val seqOfLongValuesForVehicleId: Seq[Long] = resOfVehicle.map(_.vehicleId)
            vehicleRepository.internalDeleteForListOfId(seqOfLongValuesForVehicleId)
          } else {
            companyRepository.internalDelete(deleteCompanyId)
          }
        } else Future.failed(InvalidIdException(message = "User Entered The Invalid Id For The Company Table", exception = new Exception))
      } else Future.failed(EmptyListException(message = "Company Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

}

class CompanyRoute(companyService: CompanyService) extends DirectiveWithGenericErrorHandling {
  val companyRoute: Route = pathPrefix("company") {
    path("readCompanyData" / LongNumber) { id =>
      get {
        complete(respond(companyService.readDataForCompany(id)))
      }
    } ~ path("deleteCompanyData" / LongNumber) { id =>
      delete {
        complete(respond(companyService.deleteCompanyRowById(id)))
      }
    } ~ path("insertDataForCompany") {
      post {
        entity(as[String]) { postBody =>
          val entity: Company = read[Company](postBody)
          complete(respond(companyService.insertDataOnlyForCompany(entity)))
        }
      }
    } ~ path("updateOperationForCompany" / LongNumber) { updatePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val entity: Company = read[Company](postBody)
          complete(respond(companyService.updateOperationForCompany(updatePrimaryId, entity)))
        }
      }
    } ~ path("getAllCompanyData") {
      get {
        complete(respond(companyService.getAllCompanyData))
      }
    } ~ path("deleteCompanyRowByIdWithRelation" / LongNumber) { companyId =>
      delete {
        complete(respond(companyService.deleteCompanyRowByIdWithRelation(companyId)))
      }
    }
  }
}