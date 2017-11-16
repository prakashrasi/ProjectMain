package com.reactore.features.testcase.service

import com.reactore.core._
import com.reactore.features.VehicleService
import com.reactore.features.testcase.{MockDataForGlobalTestFeatures, MockVehicleFacade}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class VehicleServiceTest extends WordSpec with Matchers with MockitoSugar with ScalaFutures with MockDataForGlobalTestFeatures {

  "Process The Vehicle Assignment Test Case Logic" should {

    "readDataForVehicle If Valid Input Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      val actualResult: Future[Vehicle] = MockVehicleService.readDataForVehicle(2L)
      actualResult.futureValue shouldBe vehicle2
    }

    "readDataForVehicle If InValid Input Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      val actualResult: Future[Vehicle] = MockVehicleService.readDataForVehicle(2222L)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "readDataForVehicle If Empty List Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      val actualResult: Future[Vehicle] = MockVehicleService.readDataForVehicle(2222L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "getAllDataForVehicle " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.getAllVehicleData
      actualResult.futureValue shouldBe seqOfVehicle
    }

    "insertDataForVehicle If Valid Input Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity = Vehicle(3, "Akash BUS SERVICE", None, "mnbus503", 2, 1, 8, 8000)
      val actualResult: Future[Int] = MockVehicleService.insertDataOnlyForVehicel(inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "insertDataForVehicle If InValid Input Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity = Vehicle(3, "", None, "mnbus503", 2, 1, 8, 8000)
      val actualResult: Future[Int] = MockVehicleService.insertDataOnlyForVehicel(inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "insertDataForVehicle If InValid Input Is Passed i.e., modelNumber Is Not Defined " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity = Vehicle(3, "Akash Bus Service", None, "", 2, 1, 8, 8000)
      val actualResult: Future[Int] = MockVehicleService.insertDataOnlyForVehicel(inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "insertDataForVehicle If InValid Input Is Passed i.e., UniqueKeyViolation  " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity = Vehicle(3, "Akash Bus Service", None, "mnbus502", 2, 1, 8, 8000)
      val actualResult: Future[Int] = MockVehicleService.insertDataOnlyForVehicel(inputEntity)
      actualResult.failed.futureValue shouldBe an[UniqueKeyViolationException]
    }

    "insertDataForVehicle If InValid Input Is Passed i.e., ForEignKey Violation For VehicleType  " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity = Vehicle(3, "Akash Bus Service", None, "mnbus503", 222, 1, 8, 8000)
      val actualResult: Future[Int] = MockVehicleService.insertDataOnlyForVehicel(inputEntity)
      actualResult.failed.futureValue shouldBe an[ForeignKeyException]
    }

    "insertDataForVehicle If InValid Input Is Passed i.e., ForEignKey Violation For Company  " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity = Vehicle(3, "Akash Bus Service", None, "mnbus503", 2, 111, 8, 8000)
      val actualResult: Future[Int] = MockVehicleService.insertDataOnlyForVehicel(inputEntity)
      actualResult.failed.futureValue shouldBe an[ForeignKeyException]
    }

    "insertDataForVehicle Handle Exception If Empty List Is Passed For VehicelType  " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity = Vehicle(3, "Akash Bus Service", None, "mnbus503", 2, 1, 8, 8000)
      val actualResult: Future[Int] = MockVehicleService.insertDataOnlyForVehicel(inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "insertDataForVehicle Handle Exception If Empty List Is Passed For Company  " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(List()))
      when(MockVehicleService.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity = Vehicle(3, "Akash Bus Service", None, "mnbus503", 2, 1, 8, 8000)
      val actualResult: Future[Int] = MockVehicleService.insertDataOnlyForVehicel(inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }


    "updateDataForVehicle If Valid Input Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity = Vehicle(2, "Nandan Bus Service Center", None, "mnbus502", 2, 1, 4, 4000)
      val actualResult: Future[Int] = MockVehicleService.updateOperationForVehicle(2L, inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "updateDataForVehicle If Invalid Input Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity = Vehicle(2, "Nandan Bus Service Center", None, "mnbus502", 2, 1, 4, 4000)
      val actualResult: Future[Int] = MockVehicleService.updateOperationForVehicle(22222, inputEntity)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "updateDataForVehicle If Empty List Is Passed For Vehicle Table" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity = Vehicle(2, "Nandan Bus Service Center", None, "mnbus502", 2, 1, 4, 4000)
      val actualResult: Future[Int] = MockVehicleService.updateOperationForVehicle(2L, inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "updateDataForVehicle If Empty List Is Passed For Vehicle Type Table" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity = Vehicle(2, "Nandan Bus Service Center", None, "mnbus502", 2, 1, 4, 4000)
      val actualResult: Future[Int] = MockVehicleService.updateOperationForVehicle(2L, inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "updateDataForVehicle If Empty List Is Passed For Company Table" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity = Vehicle(2, "Nandan Bus Service Center", None, "mnbus502", 2, 1, 4, 4000)
      val actualResult: Future[Int] = MockVehicleService.updateOperationForVehicle(2L, inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "updateDataForVehicle If Invalid Input Is Passed For VehicleName iS Not Defined" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity = Vehicle(2, "", None, "mnbus502", 2, 1, 4, 4000)
      val actualResult: Future[Int] = MockVehicleService.updateOperationForVehicle(2L, inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }


    "updateDataForVehicle If Invalid Input Is Passed For ModelNumber Is Not Defined" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity = Vehicle(2, "Nandan Bus Service Center", None, "", 2, 1, 4, 4000)
      val actualResult: Future[Int] = MockVehicleService.updateOperationForVehicle(2L, inputEntity)
      actualResult.failed.futureValue shouldBe an[GenericException]
    }

    "updateDataForVehicle Handle Exception For ForEignKey Violation" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity = Vehicle(2, "Nandan Bus Service Center", None, "mnbus502", 2222, 1, 4, 4000)
      val actualResult: Future[Int] = MockVehicleService.updateOperationForVehicle(2L, inputEntity)
      actualResult.failed.futureValue shouldBe an[ForeignKeyException]
    }

    "updateDataForVehicle Handle Exception For ForEignKey Violation For Company" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity = Vehicle(2, "Nandan Bus Service Center", None, "mnbus502", 2, 1111, 4, 4000)
      val actualResult: Future[Int] = MockVehicleService.updateOperationForVehicle(2L, inputEntity)
      actualResult.failed.futureValue shouldBe an[ForeignKeyException]
    }

    "DeleteDataRowForVehicle If Valid Input Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehiclesRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockVehicleService.deleteVehicleRowById(2L)
      actualResult.futureValue shouldBe 1
    }

    "DeleteDataRowForVehicle If InValid Input Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehiclesRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockVehicleService.deleteVehicleRowById(22222L)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "DeleteDataRowForVehicle For Empty List Is Passed For Vehicle " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleService.vehiclesRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      val actualResult: Future[Int] = MockVehicleService.deleteVehicleRowById(2L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    /** 1. Create a method to group vehicles based on company and return the details **/
    "group vehicles based on company " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      val actualResult: Future[List[VehicleContainerGroupByCompany]] = MockVehicleService.groupVehiclesBasedOnCompany
      actualResult.futureValue shouldBe List(VehicleContainerGroupByCompany(1, List(vehicle1, vehicle2)))
    }

    "group vehicles based on company If Empty List Is Passd" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(List()))
      val actualResult: Future[List[VehicleContainerGroupByCompany]] = MockVehicleService.groupVehiclesBasedOnCompany
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    /** 2. Create a method to give vehicles based on vehicleCategory **/
    "giveVehiclesBasedOnVehicleCategoryByCategoryId " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.giveVehiclesBasedOnVehicleCategoryByCategoryId(3)
      actualResult.futureValue shouldBe Seq(vehicle1, vehicle2)
    }

    "giveVehiclesBasedOnVehicleCategoryByCategoryId Handle Exception For NoSuchEntityException " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.giveVehiclesBasedOnVehicleCategoryByCategoryId(2)
      actualResult.failed.futureValue shouldBe an[NoSuchEntityException]
    }

    "giveVehiclesBasedOnVehicleCategoryByCategoryId Handle Exception For Invalid Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.giveVehiclesBasedOnVehicleCategoryByCategoryId(222222)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "giveVehiclesBasedOnVehicleCategoryByCategoryId Handle Exception For Empty List Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.giveVehiclesBasedOnVehicleCategoryByCategoryId(222222)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }


    "giveVehiclesBasedOnVehicleCategoryByCategoryId Handle Exception For Empty Is Passed For Vehicle Type " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.giveVehiclesBasedOnVehicleCategoryByCategoryId(222222)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    /** 4. Create a method to group vehicles based on vehiclecategory and return the details **/
    "groupVehiclesBasedOnVehicleCategory " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      val actualResult: Future[List[VehicleContainerGroupByCategoryId]] = MockVehicleService.groupVehiclesBasedOnVehicleCategory
      actualResult.futureValue shouldBe List(VehicleContainerGroupByCategoryId(2, List()), VehicleContainerGroupByCategoryId(4, List()),
        VehicleContainerGroupByCategoryId(1, List()),
        VehicleContainerGroupByCategoryId(3, List(vehicle1, vehicle2)))
    }

    "groupVehiclesBasedOnVehicleCategory If Empty List Is Passed For Vehicle " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      val actualResult: Future[List[VehicleContainerGroupByCategoryId]] = MockVehicleService.groupVehiclesBasedOnVehicleCategory
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "groupVehiclesBasedOnVehicleCategory If Empty List Is Passed For VehicleType " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))

      val actualResult: Future[List[VehicleContainerGroupByCategoryId]] = MockVehicleService.groupVehiclesBasedOnVehicleCategory
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    /** 5. Create a method to get all the vehicles with maxCapacity greater than given value **/
    "getAllVehiclesWithMaxCapacity " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.getAllVehiclesWithMaxCapacity(400.0)
      actualResult.futureValue shouldBe Seq(vehicle1, vehicle2)
    }

    "getAllVehiclesWithMaxCapacity Input = 800" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.getAllVehiclesWithMaxCapacity(800)
      actualResult.failed.futureValue shouldBe an[NoSuchEntityException]
    }

    "getAllVehiclesWithMaxCapacity Input = 8888" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.getAllVehiclesWithMaxCapacity(800)
      actualResult.failed.futureValue shouldBe an[NoSuchEntityException]
    }

    "getAllVehiclesWithMaxCapacity If Empty List Is Passed For Vehicle" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.getAllVehiclesWithMaxCapacity(400)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "getAllVehiclesWithMaxCapacity If Empty List Is Passed For VehicleType" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(List()))
      when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.getAllVehiclesWithMaxCapacity(400)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }


    "getAllVehiclesWithMaxCapacity If Empty List Is Passed For Vehicle Category " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleService.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(Nil))

      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.getAllVehiclesWithMaxCapacity(400)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }


    /** 7. Create a method to get number of vehicles for given country **/
    "getNumberOfVehiclesForFivenCountry " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      val actualResult: Future[Int] = MockVehicleService.getNumberOfVehiclesForFivenCountry(1)
      actualResult.futureValue shouldBe 2
    }

    "getNumberOfVehiclesForFivenCountry if Invalid Input Is Passed" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      val actualResult: Future[Int] = MockVehicleService.getNumberOfVehiclesForFivenCountry(1111)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "getNumberOfVehiclesForFivenCountry Handle Exception For NoSuchEntityException" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      val actualResult: Future[Int] = MockVehicleService.getNumberOfVehiclesForFivenCountry(4)
      actualResult.failed.futureValue shouldBe an[NoSuchEntityException]
    }

    "getNumberOfVehiclesForFivenCountry If Empty List Is Passed FOr Vehicle" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      val actualResult: Future[Int] = MockVehicleService.getNumberOfVehiclesForFivenCountry(4)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "getNumberOfVehiclesForFivenCountry If Empty List Is Passed For Company Table " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(Nil))

      val actualResult: Future[Int] = MockVehicleService.getNumberOfVehiclesForFivenCountry(4)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    /** 8. Create a method to update quantity of a particular vehicle , weight and
      * description by taking these values as input **/
    "updateVehicleDetailsBasedOnInput " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: NewDetailsForVehicle = NewDetailsForVehicle(description = Some("Location Madiwala"), 6, 4444)
      val actualResult: Future[Int] = MockVehicleService.updateVehicleDetailsBasedOnInput(2L, inputEntity)
      actualResult.futureValue shouldBe 1
    }

    "updateVehicleDetailsBasedOnInput Handle Exception For If Invalid Input Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      Vehicle(2, "Nandu BUS SERVICE", None, "mnbus502", 2, 1, 4, 4000)
      val inputEntity: NewDetailsForVehicle = NewDetailsForVehicle(description = Some("Madiwala"), 6, 4444)
      val actualResult: Future[Int] = MockVehicleService.updateVehicleDetailsBasedOnInput(2222, inputEntity)
      actualResult.failed.futureValue shouldBe an[InvalidIdException]
    }

    "updateVehicleDetailsBasedOnInput Handle Exception For If Empty List  Is Passed " in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(List()))
      when(MockVehicleService.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      Vehicle(2, "Nandu BUS SERVICE", None, "mnbus502", 2, 1, 4, 4000)
      val inputEntity: NewDetailsForVehicle = NewDetailsForVehicle(description = Some("Madiwala"), 6, 4444)
      val actualResult: Future[Int] = MockVehicleService.updateVehicleDetailsBasedOnInput(2222, inputEntity)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    /*"getAllVehicles For Given Years" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.getAllVehiclesBasedOnYears(25)
      actualResult.futureValue shouldBe Seq(vehicle1, vehicle2)
    }

    "getAllVehicles For Given Years For Handle No Such Entity Exception" in {
      when(MockVehicleService.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleService.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      val actualResult: Future[Seq[Vehicle]] = MockVehicleService.getAllVehiclesBasedOnYears(29)
      actualResult.failed.futureValue shouldBe an[NoSuchEntityException]
    }*/

    //////////////////////////////////////
  } //endOfProcess
}

object MockVehicleService extends VehicleService with MockVehicleFacade