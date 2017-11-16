package com.reactore.features.testcase.service

import java.sql.Timestamp

import com.reactore.core._
import com.reactore.features.CompanyService
import com.reactore.features.testcase.{MockCompanyFacade, MockDataForGlobalTestFeatures}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class CompanyServiceTest extends WordSpec with Matchers with MockitoSugar with ScalaFutures with MockDataForGlobalTestFeatures {

  "Process The Vehicle Assignment Test Case Logic" should {

    "readDataForCompany If Valid Input Is Passed " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      val actualResult: Future[Company] = MockCompanyService.readDataForCompany(1L)
      actualResult.futureValue shouldBe company1
    }

    "readDataForCompany If InValid Input Is Passed " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      val actualResult: Future[Company] = MockCompanyService.readDataForCompany(1111L)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "readDataForCompany If EmptyList Is Passed " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(Nil))
      val actualResult: Future[Company] = MockCompanyService.readDataForCompany(1L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "getAllDataForCompany" in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      val actualResult: Future[Seq[Company]] = MockCompanyService.getAllCompanyData
      actualResult.futureValue shouldBe Seq(company1, company2, company3, company4, company5, company6)
    }

    "insertDataForCompany If Valid Input Is Passed " in {
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.companyRepository.internalInsert(any[Company])).thenReturn(Future.successful(1))

      val inputEntity = Company(7L, "SUZUKI SUZUKI", Some("It Is Suzuki Suzuki"), "clnSuzuki157", 6L, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.insertDataOnlyForCompany(inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "insertDataForCompany If Empty List Is Passed  For Country" in {
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(Nil))
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.companyRepository.internalInsert(any[Company])).thenReturn(Future.successful(1))

      val inputEntity = Company(7L, "SUZUKI SUZUKI", Some("It Is Suzuki Suzuki"), "clnSuzuki157", 6L, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.insertDataOnlyForCompany(inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "insertDataForCompany If InValid Input Is Passed = Company Name Is Empty " in {
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.companyRepository.internalInsert(any[Company])).thenReturn(Future.successful(1))

      val inputEntity = Company(7L, "", Some("It Is Suzuki Suzuki"), "clnSuzuki157", 6L, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.insertDataOnlyForCompany(inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "insertDataForCompany If InValid Input Is Passed = License Number Is Empty " in {
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.companyRepository.internalInsert(any[Company])).thenReturn(Future.successful(1))

      val inputEntity = Company(7L, "Suzuki Suzuki", Some("It Is Suzuki Suzuki"), "", 6L, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.insertDataOnlyForCompany(inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "insertDataForCompany If InValid Input Is Passed = ForeignKey Exception " in {
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.companyRepository.internalInsert(any[Company])).thenReturn(Future.successful(1))

      val inputEntity = Company(7L, "Suzuki Suzuki", None, "clnSuzuki157", 6666L, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.insertDataOnlyForCompany(inputEntity)
      actualResult.failed.futureValue shouldBe an[ForeignKeyException]
    }

    "updateDataForCompany If Valid Input Is Passed " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyService.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))
      val inputEntity: Company = Company(6, "Suzuki", Some("Suzuki"), "clnSuzuki156", 6, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.updateOperationForCompany(6L, inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "updateDataForCompany If Empty List Is Passed For Company " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(Nil))
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyService.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "Suzuki", Some("Suzuki"), "clnSuzuki156", 6, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.updateOperationForCompany(6L, inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "updateDataForCompany If Empty List Is Passed For Country " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(Nil))
      when(MockCompanyService.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "Suzuki", Some("Suzuki"), "clnSuzuki156", 6, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.updateOperationForCompany(6L, inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "updateDataForCompany If InValid Input Is Passed=Company Name iS Empty " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyService.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "", Some("Suzuki"), "clnSuzuki156", 6, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.updateOperationForCompany(6L, inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "updateDataForCompany If InValid Input Is Passed=license Number iS Empty " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyService.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "Suzuki", Some("Suzuki"), "", 6, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.updateOperationForCompany(6L, inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "updateDataForCompany If InValid Input Is Passed=ForEign Key Exception " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCompanyService.companyRepository.internalUpdate(any[Long], any[Company])).thenReturn(Future.successful(1))

      val inputEntity: Company = Company(6, "Suzuki", Some("Suzuki"), "clnSuzuki156", 666666, Timestamp.valueOf("1990-11-16 06:55:40.11"))
      val actualResult: Future[Int] = MockCompanyService.updateOperationForCompany(6L, inputEntity)
      actualResult.failed.futureValue shouldBe an[ForeignKeyException]
    }

    "DeleteDataRowForCompany If Valid Input Is Passed " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyService.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))
      val actualResult: Future[Int] = MockCompanyService.deleteCompanyRowById(6L)
      actualResult.futureValue shouldBe 1
    }

    "DeleteDataRowForCompany If InValid Input Is Passed " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyService.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockCompanyService.deleteCompanyRowById(66666L)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "DeleteDataRowForCompany HandleException For ForeignKey " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyService.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockCompanyService.deleteCompanyRowById(1L)
      actualResult.failed.futureValue shouldBe an[ForeignKeyException]
    }

    "DeleteDataRowForCompany HandleException For EmptyListException " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(Nil))
      when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyService.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockCompanyService.deleteCompanyRowById(1L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    /** 9. Create a method to delete company and delete related data
      * from vehicle category , vehicleType and remaining tables **/

    "DeleteDataRowForCompanyWithRelation If Valid Input Is Passed " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyService.vehicleRepository.internalDeleteForListOfId(any[List[Long]])).thenReturn(Future.successful(1))
      when(MockCompanyService.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))
      val actualResult: Future[Int] = MockCompanyService.deleteCompanyRowByIdWithRelation(6L)
      actualResult.futureValue shouldBe 1
    }


    "DeleteDataRowForCompanyWithRelation If Invalid Input Is Passed " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyService.vehicleRepository.internalDeleteForListOfId(any[List[Long]])).thenReturn(Future.successful(1))
      when(MockCompanyService.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))
      val actualResult: Future[Int] = MockCompanyService.deleteCompanyRowByIdWithRelation(1111)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "DeleteDataRowForCompanyWithRelation If Empty List Is Passed " in {
      when(MockCompanyService.companyRepository.companyFuture).thenReturn(Future.successful(List()))
      when(MockCompanyService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockCompanyService.vehicleRepository.internalDeleteForListOfId(any[List[Long]])).thenReturn(Future.successful(1))
      when(MockCompanyService.companyRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))
      val actualResult: Future[Int] = MockCompanyService.deleteCompanyRowByIdWithRelation(1111)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }
  } //endOfProcess
}

object MockCompanyService extends CompanyService with MockCompanyFacade