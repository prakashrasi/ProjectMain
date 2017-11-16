package com.reactore.features.testcase.service

import com.reactore.core._
import com.reactore.features.VehicleTypeService
import com.reactore.features.testcase.{MockDataForGlobalTestFeatures, MockVehicleTypeFacade}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class VehicleTypeServiceTest extends WordSpec with Matchers with MockitoSugar with ScalaFutures with MockDataForGlobalTestFeatures {

  "Process The Vehicle Assignment Test Case Logic" should {

    "readDataForVehicleType If Valid Input Is Passed " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      val actualResult: Future[VehicleType] = MockVehicleTypeService.readDataForVehicelType(1L)
      actualResult.futureValue shouldBe vehicleType1
    }

    "readDataForVehicleType If InValid Input Is Passed " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      val actualResult: Future[VehicleType] = MockVehicleTypeService.readDataForVehicelType(1111L)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "readDataForVehicleType If Empty List Is Passed " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      val actualResult: Future[VehicleType] = MockVehicleTypeService.readDataForVehicelType(1L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "getAllDataForVehicleType " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      val actualResult: Future[Seq[VehicleType]] = MockVehicleTypeService.getAllVehicleTypeData
      actualResult.futureValue shouldBe seqOfVehicleType
    }

    "insertDataForVehicleType If Valid Input Is Passed " in {
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeService.vehicleTypeRepository.internalInsert(any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(6, "Aeroplane", None, 3)
      val actualResult: Future[Int] = MockVehicleTypeService.insertDataOnlyForVehicelType(inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "insertDataForVehicleType If InValid Input Is Passed " in {
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeService.vehicleTypeRepository.internalInsert(any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(6, "", None, 3)
      val actualResult: Future[Int] = MockVehicleTypeService.insertDataOnlyForVehicelType(inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "insertDataForVehicleType If InValid Input Is Passed = ForEign Key Violation" in {
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeService.vehicleTypeRepository.internalInsert(any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(6, "Aeroplane", None, 33)
      val actualResult: Future[Int] = MockVehicleTypeService.insertDataOnlyForVehicelType(inputEntity)
      actualResult.failed.futureValue shouldBe an[ForeignKeyException]
    }

    "insertDataForVehicleType If  Empty List Is Passed For VehicleCategory" in {
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleTypeService.vehicleTypeRepository.internalInsert(any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(6, "Aeroplane", None, 3)
      val actualResult: Future[Int] = MockVehicleTypeService.insertDataOnlyForVehicelType(inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "updateDataForVehicleType If Valid Input Is Passed " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeService.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(5, "DUMPER SUPER", None, 3)
      val actualResult: Future[Int] = MockVehicleTypeService.updateOperationForVehicleType(5L, inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "updateDataForVehicleType If InValid Input Is Passed " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeService.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(5, "DUMPER SUPER", None, 3)
      val actualResult: Future[Int] = MockVehicleTypeService.updateOperationForVehicleType(5555L, inputEntity)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "updateDataForVehicleType If Empty List Is Passed " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeService.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(5, "DUMPER SUPER", None, 3)
      val actualResult: Future[Int] = MockVehicleTypeService.updateOperationForVehicleType(5L, inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "updateDataForVehicleType If Empty List Is Passed For Vehicle Category" in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleTypeService.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(5, "DUMPER SUPER", None, 3)
      val actualResult: Future[Int] = MockVehicleTypeService.updateOperationForVehicleType(5L, inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "updateDataForVehicleType If Vehicle Category Name Is Not Defined " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeService.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(5, "", None, 3)
      val actualResult: Future[Int] = MockVehicleTypeService.updateOperationForVehicleType(5L, inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "updateDataForVehicleType Handle Exception For ForEignKey Exception " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeService.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(5, "DUMPER", None, 3333)
      val actualResult: Future[Int] = MockVehicleTypeService.updateOperationForVehicleType(5L, inputEntity)
      actualResult.failed.futureValue shouldBe an[ForeignKeyException]
    }

    "updateDataForVehicleType Changes For Foreignkey Value " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeService.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity = VehicleType(5, "DUMPER", None, 5)
      val actualResult: Future[Int] = MockVehicleTypeService.updateOperationForVehicleType(5L, inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "DeleteDataRowForVehicleType If Valid Input Is Passed " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleTypeService.vehicleTypeRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockVehicleTypeService.deleteVehicleTypeRowById(4L)
      actualResult.futureValue shouldBe 1
    }

    "DeleteDataRowForVehicleType If Invalid Input Is Passed " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleTypeService.vehicleTypeRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockVehicleTypeService.deleteVehicleTypeRowById(4444L)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "DeleteDataRowForVehicleType Handle Exception For FOREIGNKEYVIOLATION " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleTypeService.vehicleTypeRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockVehicleTypeService.deleteVehicleTypeRowById(2L)
      actualResult.failed.futureValue shouldBe an[ForeignKeyException]
    }

    "DeleteDataRowForVehicleType If Empty List Is Passed For VehicleType " in {
      when(MockVehicleTypeService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleTypeService.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleTypeService.vehicleTypeRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockVehicleTypeService.deleteVehicleTypeRowById(4L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }
  } //endOfProcess
}

object MockVehicleTypeService extends VehicleTypeService with MockVehicleTypeFacade