package com.reactore.features.testcase.rest

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.core._
import com.reactore.features.testcase.{MockDataForGlobalTestFeatures, MockVehicleFacade}
import com.reactore.features.{VehicleRoute, VehicleService}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RouteTestForVehicle extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar with DirectiveWithGenericErrorHandling with MockDataForGlobalTestFeatures {
  val mockVehicleRest  = new VehicleRoute(MockVehicleServiceAndRoute)
  val mockRoute: Route = mockVehicleRest.route1

  "Process The Vehicle Route Test Logic" should {

    "readDataForVehicle If Valid Input Is Passed" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      Get("/vehicle/readVehicleData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe vehicle1.asJson
      }
    }

    "readDataForVehicle If InValid Input Is Passed " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      Get("/vehicle/readVehicleData/11111") ~> mockRoute ~> check {
        responseAs[String] shouldBe "vehicle Id Is Invalid And Not Match In The Database".asJson
      }
    }

    "readDataForVehicle If Empty List Is Passed " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      Get("/vehicle/readVehicleData/11111") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle  Table Is Empty In The Database".asJson
      }
    }

    "getAllDataForVehicle" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      Get("/vehicle/getAllVehicleData") ~> mockRoute ~> check {
        responseAs[String] shouldBe seqOfVehicle.asJson
      }
    }

    "DeleteDataRowForVehicle If Valid Input Is Passed " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))
      Delete("/vehicle/deleteVehicleData/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForVehicle If InValid Input Is Passed " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))
      Delete("/vehicle/deleteVehicleData/2222") ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Id For The Vehicle Table".asJson
      }
    }

    "DeleteDataRowForVehicle For Empty List Is Passed For Vehicle " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))
      Delete("/vehicle/deleteVehicleData/2222") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle  Table Is Empty In The Database".asJson
      }
    }

    "insertDataForVehicle If Valid Input Is Passed " in {
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity: Vehicle = Vehicle(3, "Sagiri BUS SERVICE", None, "mnbus503", 2, 1, 6, 6000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicle/insertDataForVehicle", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "insertDataForVehicle If InValid Input Is Passed=Name Is Empty " in {
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity: Vehicle = Vehicle(3, "", None, "mnbus503", 2, 1, 6, 6000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicle/insertDataForVehicle", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Vehicle Name Is Empty".asJson
      }
    }

    "insertDataForVehicle If InValid Input Is Passed i.e., modelNumber Is Not Defined " in {
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity: Vehicle = Vehicle(3, "Sagiri BUS SERVICE", None, "", 2, 1, 6, 6000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicle/insertDataForVehicle", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Vehicle Model Number Is Empty".asJson
      }
    }

    "insertDataForVehicle If InValid Input Is Passed i.e., UniqueKeyViolation  " in {
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity: Vehicle = Vehicle(3, "Sagiri BUS SERVICE", None, "mnbus502", 2, 1, 6, 6000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicle/insertDataForVehicle", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered Model number Is Not Unique In The DataBase".asJson
      }
    }

    "insertDataForVehicle If InValid Input Is Passed i.e., ForEignKey Violation For VehicleType  " in {
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity: Vehicle = Vehicle(3, "Sagiri BUS SERVICE", None, "mnbus503", 222, 1, 6, 6000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicle/insertDataForVehicle", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "ForEign Key Violation".asJson
      }
    }

    "insertDataForVehicle If InValid Input Is Passed i.e., ForEignKey Violation For Company  " in {
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity: Vehicle = Vehicle(3, "Sagiri BUS SERVICE", None, "mnbus503", 2, 1111, 6, 6000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicle/insertDataForVehicle", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "ForEign Key Violation For Company Id".asJson
      }
    }

    "insertDataForVehicle Handle Exception If Empty List Is Passed For VehicelType  " in {
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity: Vehicle = Vehicle(3, "Sagiri BUS SERVICE", None, "mnbus503", 2, 1, 6, 6000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicle/insertDataForVehicle", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The  vehicle Type Table Is Empty In The Database".asJson
      }
    }

    "insertDataForVehicle Handle Exception If Empty List Is Passed For Company  " in {
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalInsert(any[Vehicle])).thenReturn(Future.successful(1))
      val inputEntity: Vehicle = Vehicle(3, "Sagiri BUS SERVICE", None, "mnbus503", 2, 1, 6, 6000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicle/insertDataForVehicle", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Company Table Is Empty In The Database".asJson
      }
    }

    "updateDataForVehicle If Valid Input Is Passed " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: Vehicle = Vehicle(2, "Nandu BUS SERVICE Center", None, "mnbus502", 2, 1, 4, 4000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateOperationForVehicle/2", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "updateDataForVehicle If Invalid Input Is Passed " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: Vehicle = Vehicle(2, "Nandu BUS SERVICE Center", None, "mnbus502", 2, 1, 4, 4000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateOperationForVehicle/2222", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Primary Id For Update Vehicle Table".asJson
      }
    }

    "updateDataForVehicle If Empty List Is Passed For Vehicle Table" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: Vehicle = Vehicle(2, "Nandu BUS SERVICE Center", None, "mnbus502", 2, 1, 4, 4000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateOperationForVehicle/2", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The  vehicle Table Is Empty In The Database".asJson
      }
    }

    "updateDataForVehicle If Empty List Is Passed For Vehicle Type Table" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: Vehicle = Vehicle(2, "Nandu BUS SERVICE Center", None, "mnbus502", 2, 1, 4, 4000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateOperationForVehicle/2", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The  vehicle Type Table Is Empty In The Database".asJson
      }
    }

    "updateDataForVehicle If Empty List Is Passed For Company Table" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: Vehicle = Vehicle(2, "Nandu BUS SERVICE Center", None, "mnbus502", 2, 1, 4, 4000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateOperationForVehicle/2", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Company Table Is Empty In The Database".asJson
      }
    }

    "updateDataForVehicle If Invalid Input Is Passed For VehicleName iS Not Defined" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: Vehicle = Vehicle(2, "", None, "mnbus502", 2, 1, 4, 4000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateOperationForVehicle/2", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Vehicle Name Is Empty".asJson
      }
    }

    "updateDataForVehicle If Invalid Input Is Passed For ModelNumber Is Not Defined" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: Vehicle = Vehicle(2, "Nandu BUS SERVICE Center", None, "", 2, 1, 4, 4000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateOperationForVehicle/2", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Vehicle Model Number Is Empty".asJson
      }
    }

    "updateDataForVehicle Handle Exception For ForEignKey Violation" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: Vehicle = Vehicle(2, "Nandu BUS SERVICE Center", None, "mnbus502", 2222, 1, 4, 4000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateOperationForVehicle/2", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "ForEign Key Violation".asJson
      }
    }

    "updateDataForVehicle Handle Exception For ForEignKey Violation For Company" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: Vehicle = Vehicle(2, "Nandu BUS SERVICE Center", None, "mnbus502", 2, 1111, 4, 4000)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateOperationForVehicle/2", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "ForEign Key Violation For Company Id".asJson
      }
    }

    /** 1. Create a method to group vehicles based on company and return the details **/

    "group vehicles based on company " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      Get("/vehicle/groupVehiclesBasedOnCompany") ~> mockRoute ~> check {
        responseAs[String] shouldBe List(VehicleContainerGroupByCompany(1, List(vehicle1, vehicle2))).asJson
      }
    }

    "group vehicles based on company If Empty List Is Passd" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      Get("/vehicle/groupVehiclesBasedOnCompany") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Table Is Empty In The Database".asJson
      }
    }

    /** 2. Create a method to give vehicles based on vehicleCategory **/
    "giveVehiclesBasedOnVehicleCategoryByCategoryId " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      Get("/vehicle/giveVehiclesBasedOnVehicleCategoryByCategoryId/3") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(vehicle1, vehicle2).asJson
      }
    }

    "giveVehiclesBasedOnVehicleCategoryByCategoryId Handle Exception For NoSuchEntityException " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      Get("/vehicle/giveVehiclesBasedOnVehicleCategoryByCategoryId/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe "No Such Entity Exception For This Id".asJson
      }
    }

    "giveVehiclesBasedOnVehicleCategoryByCategoryId Handle Exception For Invalid Is Passed " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      Get("/vehicle/giveVehiclesBasedOnVehicleCategoryByCategoryId/22222") ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Id For The Vehicle Type Table".asJson
      }
    }

    "giveVehiclesBasedOnVehicleCategoryByCategoryId Handle Exception For Empty List Is Passed " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      Get("/vehicle/giveVehiclesBasedOnVehicleCategoryByCategoryId/22222") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Table Is Empty In The Database".asJson
      }
    }

    "giveVehiclesBasedOnVehicleCategoryByCategoryId Handle Exception For Empty Is Passed For Vehicle Type " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      Get("/vehicle/giveVehiclesBasedOnVehicleCategoryByCategoryId/22222") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Type  Table Is Empty In The Database".asJson
      }
    }

    /** 4. Create a method to group vehicles based on vehiclecategory and return the details **/
    "groupVehiclesBasedOnVehicleCategory " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      Get("/vehicle/groupVehiclesBasedOnVehicleCategory") ~> mockRoute ~> check {
        responseAs[String] shouldBe List(VehicleContainerGroupByCategoryId(2, List()), VehicleContainerGroupByCategoryId(4, List()),
          VehicleContainerGroupByCategoryId(1, List()),
          VehicleContainerGroupByCategoryId(3, List(vehicle1, vehicle2))).asJson
      }
    }

    "groupVehiclesBasedOnVehicleCategory If Empty List Is Passed For Vehicle " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      Get("/vehicle/groupVehiclesBasedOnVehicleCategory") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Table Is Empty In The Database".asJson
      }
    }

    "groupVehiclesBasedOnVehicleCategory If Empty List Is Passed For VehicleType " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))

      Get("/vehicle/groupVehiclesBasedOnVehicleCategory") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Type  Table Is Empty In The Database".asJson
      }
    }

    /** 5. Create a method to get all the vehicles with maxCapacity greater than given value **/
    "getAllVehiclesWithMaxCapacity " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      Get("/vehicle/getAllVehiclesWithMaxCapacity/400.0") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(vehicle1, vehicle2).asJson
      }
    }

    "getAllVehiclesWithMaxCapacity Input = 800" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      Get("/vehicle/getAllVehiclesWithMaxCapacity/800.0") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle  Table Is Empty For This User Value".asJson
      }
    }

    "getAllVehiclesWithMaxCapacity Input = 8888" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      Get("/vehicle/getAllVehiclesWithMaxCapacity/888.0") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle  Table Is Empty For This User Value".asJson
      }
    }

    "getAllVehiclesWithMaxCapacity If Empty List Is Passed For Vehicle" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      Get("/vehicle/getAllVehiclesWithMaxCapacity/888.0") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Table Is Empty In The Database".asJson
      }
    }

    "getAllVehiclesWithMaxCapacity If Empty List Is Passed For Vehicle Category" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))

      Get("/vehicle/getAllVehiclesWithMaxCapacity/888.0") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Category  Table Is Empty In The Database".asJson
      }
    }


    "getAllVehiclesWithMaxCapacity If Empty List Is Passed For Vehicle Type " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(List()))

      Get("/vehicle/getAllVehiclesWithMaxCapacity/888.0") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Type  Table Is Empty In The Database".asJson
      }
    }

    /** 7. Create a method to get number of vehicles for given country **/
    "getNumberOfVehiclesForFivenCountry " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      Get("/vehicle/getNumberOfVehiclesForFivenCountry/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe "2"
      }
    }

    "getNumberOfVehiclesForFivenCountry if Invalid Input Is Passed" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      Get("/vehicle/getNumberOfVehiclesForFivenCountry/1111") ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Country Id For The Company Table".asJson
      }
    }

    "getNumberOfVehiclesForFivenCountry Handle Exception For NoSuchEntityException" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      Get("/vehicle/getNumberOfVehiclesForFivenCountry/4") ~> mockRoute ~> check {
        responseAs[String] shouldBe "There Is No Entity For This Country Id".asJson
      }
    }

    "getNumberOfVehiclesForFivenCountry If Empty List Is Passed For Vehicle" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(List()))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      Get("/vehicle/getNumberOfVehiclesForFivenCountry/4") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Table Is Empty In The Database".asJson
      }
    }

    "getNumberOfVehiclesForFivenCountry If Empty List Is Passed For Company Table " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(Nil))

      Get("/vehicle/getNumberOfVehiclesForFivenCountry/4") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Company Table Is Empty In The Database".asJson
      }
    }

    /** 8. Create a method to update quantity of a particular vehicle , weight and
      * description by taking these values as input **/
    "updateVehicleDetailsBasedOnInput " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: NewDetailsForVehicle = NewDetailsForVehicle(description = Some("Madiwala"), 6, 4444)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateVehicleDetailsBasedOnInput/2", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }


    "updateVehicleDetailsBasedOnInput Handle Exception For If Invalid Input Is Passed " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: NewDetailsForVehicle = NewDetailsForVehicle(description = Some("Madiwala"), 6, 4444)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateVehicleDetailsBasedOnInput/2222", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Id For The Vehicle Table".asJson
      }
    }

    "updateVehicleDetailsBasedOnInput Handle Exception For If Empty List  Is Passed " in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.vehiclesRepository.internalUpdate(any[Long], any[Vehicle])).thenReturn(Future.successful(1))

      val inputEntity: NewDetailsForVehicle = NewDetailsForVehicle(description = Some("Madiwala"), 6, 4444)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicle/updateVehicleDetailsBasedOnInput/2222", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Table Is Empty In The Database".asJson
      }
    }

    "getAllVehicles For Given Years" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      Get("/vehicle/getAllVehiclesBasedOnYears/25") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(vehicle1, vehicle2).asJson
      }
    }

    "getAllVehicles For Given Years For Handle No Such Entity Exception" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      Get("/vehicle/getAllVehiclesBasedOnYears/29") ~> mockRoute ~> check {
        responseAs[String] shouldBe "There Is No Vehicle For This Company Based On Years In The Database".asJson
      }
    }

    "getAllVehicles For Given Years Handle Exception For Empty List Is Passed For Vehicle" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(seqOfCompany))

      Get("/vehicle/getAllVehiclesBasedOnYears/29") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle  Table Is Empty In The Database".asJson
      }
    }

    "getAllVehicles For Given Years Handle Exception For Empty List Is Passed For Company" in {
      when(MockVehicleServiceAndRoute.vehiclesRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleServiceAndRoute.companyRepository.companyFuture).thenReturn(Future.successful(Nil))

      Get("/vehicle/getAllVehiclesBasedOnYears/29") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Company  Table Is Empty In The Database".asJson
      }
    }
  } //endofprocess
}

object MockVehicleServiceAndRoute extends VehicleService with MockVehicleFacade