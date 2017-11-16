package com.reactore.features.testcase.rest

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.core.{DirectiveWithGenericErrorHandling, VehicleType}
import com.reactore.features.testcase.{MockDataForGlobalTestFeatures, MockVehicleTypeFacade}
import com.reactore.features.{VehicleTypeRoute, VehicleTypeService}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RouteTestForVehicleType extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar with DirectiveWithGenericErrorHandling with MockDataForGlobalTestFeatures {
  val mockVehicleTypeRoute = new VehicleTypeRoute(MockVehicleTypeServiceAndRoute)
  val mockRoute: Route     = mockVehicleTypeRoute.route1

  "Process The VehicleType Route Test Logic" should {

    "readDataForVehicleType If Valid Input Is Passed" in {
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      Get("/vehicleType/readVehicleTypeData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe vehicleType1.asJson
      }
    }

    "readDataForVehicleType If InValid Input Is Passed " in {
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      Get("/vehicleType/readVehicleTypeData/11111") ~> mockRoute ~> check {
        responseAs[String] shouldBe "vehicleType Id Is Invalid And Not Match In The Database".asJson
      }
    }

    "readDataForVehicleType If Empty List Is Passed " in {
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      Get("/vehicleType/readVehicleTypeData/11111") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Type Table Is Empty In The Database".asJson
      }
    }

    "getAllDataForVehicleType" in {
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      Get("/vehicleType/getAllVehicleTypeData") ~> mockRoute ~> check {
        responseAs[String] shouldBe seqOfVehicleType.asJson
      }
    }

    "insertDataForVehicleType If Valid Input Is Passed " in {
      when(MockVehicleTypeServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalInsert(any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity: VehicleType = VehicleType(6, "Taras", None, 3)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicleType/insertVehicleTypeData", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "insertDataForVehicleType If InValid Input Is Passed =i.e., Name iS Empty" in {
      when(MockVehicleTypeServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalInsert(any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity: VehicleType = VehicleType(6, "", None, 3)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicleType/insertVehicleTypeData", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Vehicle Type Name Is Empty".asJson
      }
    }

    "insertDataForVehicleType If InValid Input Is Passed = ForEign Key Violation" in {
      when(MockVehicleTypeServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalInsert(any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity: VehicleType = VehicleType(6, "Aeroplane", None, 333)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicleType/insertVehicleTypeData", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "ForEign Key Violation Message".asJson
      }
    }

    "insertDataForVehicleType If  Empty List Is Passed For VehicleCategory" in {
      when(MockVehicleTypeServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalInsert(any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity: VehicleType = VehicleType(6, "Aeroplane", None, 333)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicleType/insertVehicleTypeData", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The  vehicle Category Table Is Empty In The Database".asJson
      }
    }

    "updateDataForVehicleType If Valid Input Is Passed " in {
      when(MockVehicleTypeServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity: VehicleType = VehicleType(5, "DUMPER Dumper", None, 3)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicleType/updateOperationForVehicleType/5", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "updateDataForVehicleType If InValid Input Is Passed " in {
      when(MockVehicleTypeServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity: VehicleType = VehicleType(5, "DUMPER Dumper", None, 3)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicleType/updateOperationForVehicleType/555", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Primary Id For Update Vehicle Type Table".asJson
      }
    }

    "updateDataForVehicleType If Empty List Is Passed For VehicleCategory " in {
      when(MockVehicleTypeServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(List()))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity: VehicleType = VehicleType(5, "DUMPER Dumper", None, 3)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicleType/updateOperationForVehicleType/5", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The  vehicle Category Table Is Empty In The Database".asJson
      }
    }

    "updateDataForVehicleType If Empty List Is Passed For Vehicle Type" in {
      when(MockVehicleTypeServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity: VehicleType = VehicleType(5, "DUMPER Dumper", None, 3)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicleType/updateOperationForVehicleType/5555", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The  vehicle Type Table Is Empty In The Database".asJson
      }
    }

    "updateDataForVehicleType If Vehicle Category Name Is Not Defined " in {
      when(MockVehicleTypeServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity: VehicleType = VehicleType(5, "", None, 3)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicleType/updateOperationForVehicleType/5555", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Vehicle Type Name Is Empty".asJson
      }
    }

    "updateDataForVehicleType Handle Exception For ForEignKey Exception " in {
      when(MockVehicleTypeServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalUpdate(any[Long], any[VehicleType])).thenReturn(Future.successful(1))

      val inputEntity: VehicleType = VehicleType(5, "Dumper Dumper", None, 3333)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicleType/updateOperationForVehicleType/5", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "ForEign Key Violation Message".asJson
      }
    }

    "DeleteDataRowForVehicleType If Valid Input Is Passed " in {
      when(MockVehicleTypeServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/vehicleType/deleteVehicleTypeData/5") ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForVehicleType If Invalid Input Is Passed " in {
      when(MockVehicleTypeServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/vehicleType/deleteVehicleTypeData/5555") ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Id For The Vehicle Type Table".asJson
      }
    }

    "DeleteDataRowForVehicleType Handle Exception For FOREIGNKEYVIOLATION " in {
      when(MockVehicleTypeServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/vehicleType/deleteVehicleTypeData/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe "ForEign Key Relation Is Exists".asJson
      }
    }

    "DeleteDataRowForVehicleType If Empty List Is Passed For VehicleType " in {
      when(MockVehicleTypeServiceAndRoute.vehicleRepository.vehiclesFuture).thenReturn(Future.successful(seqOfVehicle))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleTypeServiceAndRoute.vehicleTypeRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/vehicleType/deleteVehicleTypeData/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Type Table Is Empty In The Database".asJson
      }
    }
  } //endofprocess
}

object MockVehicleTypeServiceAndRoute extends VehicleTypeService with MockVehicleTypeFacade