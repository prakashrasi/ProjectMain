package com.reactore.features.testcase.service

import com.reactore.core._
import com.reactore.features.CountryService
import com.reactore.features.testcase.{MockCountryFacade, MockDataForGlobalTestFeatures}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class CountryServiceTest extends WordSpec with Matchers with MockitoSugar with ScalaFutures with MockDataForGlobalTestFeatures {

  "Process The Vehicle Assignment Test Case Logic" should {

    "readDataForCountry If Valid Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      val actualResult: Future[Country] = MockCountryService.readDataForCountry(1L)
      actualResult.futureValue shouldBe country1
    }

    "readDataForCountry If InValid Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      val actualResult: Future[Country] = MockCountryService.readDataForCountry(11111L)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "getAllCountryData " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      val actualResult: Future[Seq[Country]] = MockCountryService.getAllCountryData
      actualResult.futureValue shouldBe Seq(country1, country2, country3, country4, country5, country6)
    }

    "readDataForCountry If Empty List Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(Nil))
      val actualResult: Future[Country] = MockCountryService.readDataForCountry(11111L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "insertDataForCountry If Valid Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalInsert(any[Country])).thenReturn(Future.successful(1))

      val inputEntity = Country(7, "Egypt", "Egypt Language", "097")
      val actualResult: Future[Int] = MockCountryService.insertDataOnlyForCountry(inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "insertDataForCountry If InValid Input Is Passed=Country Name Is Empty " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalInsert(any[Country])).thenReturn(Future.successful(1))

      val inputEntity = Country(7, "", "Egypt Language", "097")
      val actualResult: Future[Int] = MockCountryService.insertDataOnlyForCountry(inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "insertDataForCountry If InValid Input Is Passed=Country Code Is Empty " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalInsert(any[Country])).thenReturn(Future.successful(1))

      val inputEntity = Country(7, "Egypt", "", "097")
      val actualResult: Future[Int] = MockCountryService.insertDataOnlyForCountry(inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "insertDataForCountry If InValid Input Is Passed=Country Language Is Empty " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalInsert(any[Country])).thenReturn(Future.successful(1))

      val inputEntity = Country(7, "Egypt", "Egypt Language", "")
      val actualResult: Future[Int] = MockCountryService.insertDataOnlyForCountry(inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "insertDataForCountry If InValid Input Is Passed=Country Language Is Not Unique " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalInsert(any[Country])).thenReturn(Future.successful(1))

      val inputEntity = Country(7, "Egypt", "Egypt Language", "094")
      val actualResult: Future[Int] = MockCountryService.insertDataOnlyForCountry(inputEntity)
      actualResult.failed.futureValue shouldBe an[UniqueKeyViolationException]
    }


    "updateDataForCounty If Valid Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "Canada", "Canadian Language People", "096")
      val actualResult: Future[Int] = MockCountryService.updateOperationForCountry(6L, inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "updateDataForCounty If InValid Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "Canada", "Canadian Language People", "096")
      val actualResult: Future[Int] = MockCountryService.updateOperationForCountry(6666L, inputEntity)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "updateDataForCounty If Empty Input Is Passed " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(List()))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "Canada", "Canadian Language People", "096")
      val actualResult: Future[Int] = MockCountryService.updateOperationForCountry(6666L, inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "updateDataForCounty If InValid Input Is Passed=Country Name is Empty " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "", "Canadian Language People", "096")
      val actualResult: Future[Int] = MockCountryService.updateOperationForCountry(6L, inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "updateDataForCounty If InValid Input Is Passed=Country Language is Empty " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "Canada", "", "096")
      val actualResult: Future[Int] = MockCountryService.updateOperationForCountry(6L, inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }


    "updateDataForCounty If InValid Input Is Passed=Country Code Is Null " in {
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalUpdate(any[Long], any[Country])).thenReturn(Future.successful(1))

      val inputEntity: Country = Country(6, "Canada", "Canadian Language People", "")
      val actualResult: Future[Int] = MockCountryService.updateOperationForCountry(6L, inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }


    "DeleteDataRowForCountry If Valid Input Is Passed " in {
      when(MockCountryService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockCountryService.deleteCountryRowById(6L)
      actualResult.futureValue shouldBe 1
    }

    "DeleteDataRowForCountry If InValid Input Is Passed " in {
      when(MockCountryService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(seqOfCountry))
      when(MockCountryService.countryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockCountryService.deleteCountryRowById(66666L)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }


    "DeleteDataRowForCountry If Empty List  Is Passed For Country " in {
      when(MockCountryService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockCountryService.countryRepository.countryFuture).thenReturn(Future.successful(List()))
      when(MockCountryService.countryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockCountryService.deleteCountryRowById(6L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }
  } //endOfProcess
}

object MockCountryService extends CountryService with MockCountryFacade