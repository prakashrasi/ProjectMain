package com.reactore.features.testcase.rest

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.core.{Country, DirectiveWithGenericErrorHandling}
import com.reactore.features._
import com.reactore.features.testcase.{MockCountryFacade, MockDataForGlobalTestFeatures}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RouteTestForCountry extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar with DirectiveWithGenericErrorHandling with MockDataForGlobalTestFeatures {
  val mockCountryRest  = new CountryRoute(MockCountryService)
  val mockRoute: Route = mockCountryRest.countryRoute

  "Process The Country Route Test Logic" should {

    "readDataForCountry" in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      Get("/country/readCountryData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe country1.asJson
      }
    }

    "readDataForCountry If InValid Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      Get("/country/readCountryData/1111") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Country Id Is Invalid And Not Match In The Database".asJson
      }
    }

    "readDataForCountry If Empty List Is Passed" in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(Nil))
      Get("/country/readCountryData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Country Table Is Empty In The Database".asJson
      }
    }

    "getAllCountryData" in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      Get("/country/getAllCountryData") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(country1, country2, country3, country4, country5, country6).asJson
      }
    }


    "insertDataForCountry If Valid Input Is Passed" in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalInsert(any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(7, "Egypt", "Egypt Language", "097")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/country/insertDataForCountry", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "insertDataForCountry If InValid Input Is Passed=Country Name Is Empty" in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalInsert(any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(7, "", "Egypt Language", "097")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/country/insertDataForCountry", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Name Is Empty".asJson
      }
    }

    "insertDataForCountry If InValid Input Is Passed=Country Language Is Empty" in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalInsert(any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(7, "Egypt", "", "097")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/country/insertDataForCountry", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Language Is Empty".asJson
      }
    }

    "insertDataForCountry If InValid Input Is Passed=Country Code Is Empty" in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalInsert(any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(7, "Egypt", "Egypt Language", "")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/country/insertDataForCountry", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Country Code  Is Empty".asJson
      }
    }

    "insertDataForCountry If InValid Input Is Passed=Country Code Is Not Unique" in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalInsert(any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(7, "Egypt", "Egypt Language", "094")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/country/insertDataForCountry", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Country Code Is Not Unique".asJson
      }
    }

    "updateDataForCounty If Valid Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "Canada", "Canadian Language People", "096")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/country/updateOperationForCountry/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "updateDataForCounty If InValid Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "Canada", "Canadian Language People", "096")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/country/updateOperationForCountry/66666", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Id For Update Country".asJson
      }
    }

    "updateDataForCounty If Empty Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(Nil))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "Canada", "Canadian Language People", "096")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/country/updateOperationForCountry/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Country Table Is Empty In The Database".asJson
      }
    }

    "updateDataForCounty If InValid Input Is Passed=Country Name is Empty " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "", "Canadian Language People", "096")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/country/updateOperationForCountry/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Name Is Empty".asJson
      }
    }

    "updateDataForCounty If InValid Input Is Passed=Country Language is Empty " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "Canada", "", "096")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/country/updateOperationForCountry/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Language Is Empty".asJson
      }
    }

    "updateDataForCounty If InValid Input Is Passed=Country Code Is Null " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "Canada", "Canadian Language People", "")
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/country/updateOperationForCountry/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Country Code  Is Empty".asJson
      }
    }

    "DeleteDataRowForCountry If Valid Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCountryService.countryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/country/deleteCountryData/6") ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForCountry If InValid Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCountryService.countryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/country/deleteCountryData/66") ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Id For The Country Table".asJson
      }
    }

    "DeleteDataRowForCountry If Empty List  Is Passed For Country " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(Nil))
      when(MockCountryService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCountryService.countryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/country/deleteCountryData/6") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Country Table Is Empty In The Database".asJson
      }
    }
  } //endofprocess
}

object MockCountryService extends CountryService with MockCountryFacade