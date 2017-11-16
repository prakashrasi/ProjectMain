package com.reactore.features.testcase.rest

import java.sql.Timestamp

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.core.{Company, DirectiveWithGenericErrorHandling}
import com.reactore.features.{CompanyRoute, CompanyService}
import com.reactore.features.testcase.{MockCompanyFacade, MockDataForGlobalTestFeatures}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RouteTestForCompany extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar with DirectiveWithGenericErrorHandling with MockDataForGlobalTestFeatures {


  val mockCompanyRest  = new CompanyRoute(MockCompanyServiceAndRoute)
  val mockRoute: Route = mockCompanyRest.companyRoute

  "Process The Company Route Test Logic" should {

    "readDataForCompany" in {
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      Get("/company/readCompanyData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe company1.asJson
      }
    }

    "readDataForCompany If InValid Input Is Passed " in {
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      Get("/company/readCompanyData/11111") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Company Id Is Invalid And Not Match In The Database".asJson
      }
    }

    "readDataForCompany If EmptyList Is Passed " in {
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(Nil))
      Get("/company/readCompanyData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Company Table Is Empty In The Database".asJson
      }
    }

    "getAllDataForCompany" in {
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      Get("/company/getAllCompanyData") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(company1, company2, company3, company4, company5, company6).asJson
      }
    }

    "insertDataForCompany If Valid Input Is Passed " in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalInsert(any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(7, "Rolls Royce", None, "clnrolls157", 5, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/company/insertDataForCompany", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "insertDataForCompany If Empty List Is Passed  For Country" in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(Nil))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalInsert(any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(7, "Rolls Royce", None, "clnrolls157", 5, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/company/insertDataForCompany", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Country Table Is Empty In The Database".asJson
      }
    }

    "insertDataForCompany If InValid Input Is Passed = Company Name Is Empty " in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalInsert(any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(7, "", None, "clnrolls157", 5, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/company/insertDataForCompany", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Name Is Empty".asJson
      }
    }

    "insertDataForCompany If InValid Input Is Passed = License Number Is Empty " in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalInsert(any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(7, "Rolls Royce", None, "", 5, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/company/insertDataForCompany", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The License Number Is Empty".asJson
      }
    }


    "insertDataForCompany If InValid Input Is Passed = ForeignKey Exception " in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalInsert(any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(7, "Rolls Royce", None, "clnrolls157", 555, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/company/insertDataForCompany", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "ForEign Key Exception For Country Number".asJson
      }
    }

    "updateDataForCompany If Valid Input Is Passed " in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "Suzuki", None, "clnSuzuki156", 5, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/company/updateOperationForCompany/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "updateDataForCompany If Empty List Is Passed For Company " in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(List()))
      when(MockCompanyServiceAndRoute.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "Suzuki", None, "clnSuzuki156", 5, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/company/updateOperationForCompany/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Company Table Is Empty In The Database".asJson
      }
    }

    "updateDataForCompany If Empty List Is Passed For Country " in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(Nil))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "Suzuki", None, "clnSuzuki156", 5, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/company/updateOperationForCompany/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Country Table Is Empty In The Database".asJson
      }
    }

    "updateDataForCompany If InValid Input Is Passed=Company Name iS Empty " in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "", None, "clnSuzuki156", 5, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/company/updateOperationForCompany/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Name Is Empty".asJson
      }
    }

    "updateDataForCompany If InValid Input Is Passed=license Number iS Empty " in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "Suzuki", None, "", 5, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/company/updateOperationForCompany/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The License Number Is Empty".asJson
      }
    }

    "updateDataForCompany If InValid Input Is Passed=ForEign Key Exception " in {
      when(MockCompanyServiceAndRoute.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "Suzuki", None, "clnSuzuki156", 5555, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/company/updateOperationForCompany/6", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "ForEign Key Exception For Country Number".asJson
      }
    }

    "DeleteDataRowForCompany If Valid Input Is Passed " in {
      when(MockCompanyServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/company/deleteCompanyData/6") ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForCompany If InValid Input Is Passed " in {
      when(MockCompanyServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/company/deleteCompanyData/66666") ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Id For The Company Table".asJson
      }
    }

    "DeleteDataRowForCompany HandleException For ForeignKey " in {
      when(MockCompanyServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/company/deleteCompanyData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe "ForEign Key Relation Is Exists".asJson
      }
    }

    "DeleteDataRowForCompany HandleException For EmptyListException " in {
      when(MockCompanyServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(Nil))
      when(MockCompanyServiceAndRoute.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/company/deleteCompanyData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Company Table Is Empty In The Database".asJson
      }
    }

    /** 9. Create a method to delete company and delete related data
      * from vehicle category , vehicleType and remaining tables **/

    "DeleteDataRowForCompanyWithRelation If Valid Input Is Passed " in {
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyServiceAndRoute.vehicleRepository.internalDeleteForListOfId(any[List[Long]])).thenReturn(Future.successful(1))
      when(MockCompanyServiceAndRoute.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/company/deleteCompanyRowByIdWithRelation/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForCompanyWithRelation If Invalid Input Is Passed " in {
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyServiceAndRoute.vehicleRepository.internalDeleteForListOfId(any[List[Long]])).thenReturn(Future.successful(1))
      when(MockCompanyServiceAndRoute.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/company/deleteCompanyRowByIdWithRelation/22222") ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Id For The Company Table".asJson
      }
    }

    "DeleteDataRowForCompanyWithRelation If Empty List Is Passed " in {
      when(MockCompanyServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(Nil))
      when(MockCompanyServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyServiceAndRoute.vehicleRepository.internalDeleteForListOfId(any[List[Long]])).thenReturn(Future.successful(1))
      when(MockCompanyServiceAndRoute.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/company/deleteCompanyRowByIdWithRelation/22222") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Company Table Is Empty In The Database".asJson
      }
    }
  } //endofprocess
}

object MockCompanyServiceAndRoute extends CompanyService with MockCompanyFacade