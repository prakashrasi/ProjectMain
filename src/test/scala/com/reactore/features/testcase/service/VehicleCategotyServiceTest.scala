package com.reactore.features.testcase.service

import com.reactore.core._
import com.reactore.features.VehicleCategoryService
import com.reactore.features.testcase.{MockDataForGlobalTestFeatures, MockVehicleCategoryFacade}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class VehicleCategoryServiceTest extends WordSpec with Matchers with MockitoSugar with ScalaFutures with MockDataForGlobalTestFeatures {

  "Process The Vehicle Assignment Test Case Logic" should {

    "readDataForVehicleCategory If Valid Input Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      val actualResult: Future[VehicleCategory] = MockVehicleCategoryService.readDataForVehicelCategory(2L)
      actualResult.futureValue shouldBe vehicleCategory2
    }

    "readDataForVehicleCategory If Invalid Input Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      val actualResult: Future[VehicleCategory] = MockVehicleCategoryService.readDataForVehicelCategory(22222L)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "readDataForVehicleCategory If Empty List Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(Nil))
      val actualResult: Future[VehicleCategory] = MockVehicleCategoryService.readDataForVehicelCategory(22222L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "getAllDataForVehicleCategory" in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      val actualResult: Future[Seq[VehicleCategory]] = MockVehicleCategoryService.getAllVehicleCategoryData
      actualResult.futureValue shouldBe seqOfVehicleCategory
    }

    "insertDataForVehicleCategory If Valid Input Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.internalInsert(any[VehicleCategory])).thenReturn(Future.successful(1))
      val inputEntity = VehicleCategory(6, "10Wheeler", None, 1450)
      val actualResult: Future[Int] = MockVehicleCategoryService.insertDataOnlyForVehicelCategory(inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "insertDataForVehicleCategory If Invalid Input Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.internalInsert(any[VehicleCategory])).thenReturn(Future.successful(1))

      val inputEntity = VehicleCategory(6, "", None, 1450)
      val actualResult: Future[Int] = MockVehicleCategoryService.insertDataOnlyForVehicelCategory(inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "updateDataForVehicleCategory If Valid Input Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.internalUpdate(any[Long], any[VehicleCategory])).thenReturn(Future.successful(1))

      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      val inputEntity = VehicleCategory(5L, "10Wheeler", None, 145555)
      val actualResult: Future[Int] = MockVehicleCategoryService.updateOperationForVehicleCategory(5L, inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "updateDataForVehicleCategory If Invalid Input Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.internalUpdate(any[Long], any[VehicleCategory])).thenReturn(Future.successful(1))

      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      val inputEntity = VehicleCategory(5L, "10Wheeler", None, 145555)
      val actualResult: Future[Int] = MockVehicleCategoryService.updateOperationForVehicleCategory(555L, inputEntity)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "updateDataForVehicleCategory If Invalid Input Is Passed =Name Is Not Defined" in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.internalUpdate(any[Long], any[VehicleCategory])).thenReturn(Future.successful(1))

      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      val inputEntity = VehicleCategory(5L, "", None, 145555)
      val actualResult: Future[Int] = MockVehicleCategoryService.updateOperationForVehicleCategory(555L, inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "updateDataForVehicleCategory If Empty List Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.internalUpdate(any[Long], any[VehicleCategory])).thenReturn(Future.successful(1))

      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(Nil))
      val inputEntity = VehicleCategory(5L, "10Wheeler", None, 145555)
      val actualResult: Future[Int] = MockVehicleCategoryService.updateOperationForVehicleCategory(5L, inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "DeleteDataRowForVehicleCategory If Valid Input Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleCategoryService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleCategoryService.vehicleCategoryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockVehicleCategoryService.deleteVehicleCategoryRowById(5L)
      actualResult.futureValue shouldBe 1
    }

    "DeleteDataRowForVehicleCategory If Invalid Input Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleCategoryService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleCategoryService.vehicleCategoryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockVehicleCategoryService.deleteVehicleCategoryRowById(555)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "DeleteDataRowForVehicleCategory If Empty List Is Passed " in {
      when(MockVehicleCategoryService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleCategoryService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleCategoryService.vehicleCategoryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockVehicleCategoryService.deleteVehicleCategoryRowById(5L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }
  } //endOfProcess
}

object MockVehicleCategoryService extends VehicleCategoryService with MockVehicleCategoryFacade