package com.reactore.features

import akka.http.scaladsl.server.Route
import com.reactore.core._
import org.json4s.native.Serialization._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CountryService {
  self: CountryFacadeComponent =>

  def insertDataOnlyForCountry(insertCountryEntity: Country): Future[Int] = {
    for {
      countryTableWithOutFuture <- countryRepository.countryFuture
      _ = if (insertCountryEntity.name.isEmpty) throw GenericException(message = "The Name Is Empty", exception = new Exception)
      _ = if (insertCountryEntity.language.isEmpty) throw GenericException(message = "The Language Is Empty", exception = new Exception)
      finalResult <- if (insertCountryEntity.code.nonEmpty) {
        val resOfCountryUnique: Seq[Country] = countryTableWithOutFuture.filter(_.code.toLowerCase == insertCountryEntity.code.toLowerCase)
        if (resOfCountryUnique.isEmpty) {
          countryRepository.internalInsert(insertCountryEntity)
        } else Future.failed(UniqueKeyViolationException(message = "The Country Code Is Not Unique", exception = new Exception))
      } else Future.failed(GenericException(message = "The Country Code  Is Empty", exception = new Exception))
    } yield finalResult
  }

  def readDataForCountry(countryId: Long): Future[Country] = {
    for {
      countryTableWithOutFuture <- countryRepository.countryFuture
      finalResult = if (countryTableWithOutFuture.nonEmpty) {
        val resultByFind = countryTableWithOutFuture.find(_.countryId == countryId)
        if (resultByFind.isDefined) {
          resultByFind.get
        } else throw InvalidIdException(message = "Country Id Is Invalid And Not Match In The Database", exception = new Exception)
      } else throw EmptyListException(message = "The Country Table Is Empty In The Database", exception = new Exception)
    } yield finalResult
  }

  def updateOperationForCountry(countryId: Long, updateCountryEntity: Country): Future[Int] = {
    for {
      countryTableWithOutFuture <- countryRepository.countryFuture
      _ = if (updateCountryEntity.name.isEmpty) throw GenericException(message = "The Name Is Empty", exception = new Exception)
      _ = if (updateCountryEntity.language.isEmpty) throw GenericException(message = "The Language Is Empty", exception = new Exception)
      finalResult <- if (updateCountryEntity.code.nonEmpty) {
        if (countryTableWithOutFuture.nonEmpty) {
          val res: Seq[Country] = countryTableWithOutFuture.filter(_.countryId == countryId)
          if (res.nonEmpty) {
            val uniqueCheckForCountryCode: Seq[Country] = countryTableWithOutFuture.filter(_.code.toLowerCase == updateCountryEntity.code.toLowerCase)
            if (uniqueCheckForCountryCode.size == 1) {
              countryRepository.internalUpdate(countryId, updateCountryEntity)
            } else Future.failed(UniqueKeyViolationException(message = "The Country Code Is Not Unique", exception = new Exception))
          } else Future.failed(InvalidIdException(message = "User Entered The Invalid Id For Update Country", exception = new Exception))
        } else Future.failed(EmptyListException(message = "The Country Table Is Empty In The Database", exception = new Exception))
      } else Future.failed(GenericException(message = "The Country Code  Is Empty", exception = new Exception))
    } yield finalResult
  }

  def deleteCountryRowById(deleteCountryId: Long): Future[Int] = {
    for {
      countryTableWithOutFuture <- countryRepository.countryFuture
      companyTableWithOutFuture <- companyRepository.companyFuture

      finalResult <- if (countryTableWithOutFuture.nonEmpty) {
        val validCountryId: Seq[Country] = countryTableWithOutFuture.filter(_.countryId == deleteCountryId)
        if (validCountryId.nonEmpty) {
          val resOfSeqCompany: Seq[Company] = companyTableWithOutFuture.filter(_.countryId == deleteCountryId)
          if (resOfSeqCompany.isEmpty) {
            countryRepository.internalDelete(deleteCountryId)
          } else Future.failed(ForeignKeyException(message = "ForEign Key Relation Is Exists", exception = new Exception))
        } else Future.failed(InvalidIdException(message = "User Entered The Invalid Id For The Country Table", exception = new Exception))
      } else Future.failed(EmptyListException(message = "Country Table Is Empty In The Database", exception = new Exception))
    } yield finalResult
  }

  def getAllCountryData: Future[Seq[Country]] = {
    for {
      countryTableWithOutFuture <- countryRepository.countryFuture
      finalResult = countryTableWithOutFuture
    } yield finalResult
  }
}

class CountryRoute(countryService: CountryService) extends DirectiveWithGenericErrorHandling {
  val countryRoute: Route = pathPrefix("country") {
    path("readCountryData" / LongNumber) { countryId =>
      get {
        complete(respond(countryService.readDataForCountry(countryId)))
      }
    } ~ path("deleteCountryData" / LongNumber) { countryId =>
      delete {
        complete(respond(countryService.deleteCountryRowById(countryId)))
      }
    } ~ path("insertDataForCountry") {
      post {
        entity(as[String]) { postBody =>
          val countryEntity: Country = read[Country](postBody)
          complete(respond(countryService.insertDataOnlyForCountry(countryEntity)))
        }
      }
    } ~ path("updateOperationForCountry" / LongNumber) { updatePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val countryEntity: Country = read[Country](postBody)
          complete(respond(countryService.updateOperationForCountry(updatePrimaryId, countryEntity)))
        }
      }
    } ~ path("getAllCountryData") {
      get {
        complete(respond(countryService.getAllCountryData))
      }
    }
  }
}