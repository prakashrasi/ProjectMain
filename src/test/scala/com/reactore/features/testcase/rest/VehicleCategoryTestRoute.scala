package com.reactore.features.testcase.rest

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.core.{DirectiveWithGenericErrorHandling, VehicleCategory}
import com.reactore.features.testcase.{MockDataForGlobalTestFeatures, MockVehicleCategoryFacade}
import com.reactore.features.{VehicleCategoryRoute, VehicleCategoryService}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RouteTestForVehicleCategory extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar with DirectiveWithGenericErrorHandling with MockDataForGlobalTestFeatures {

  val mockVehicleCategoryRoute = new VehicleCategoryRoute(MockVehicleCategoryServiceAndRoute)
  val mockRoute: Route         = mockVehicleCategoryRoute.vehicleCategoryRoute


  "Process The VehicleCategory Route Test Logic" should {

    "readDataForVehicleCategory If Valid Input Is Passed" in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      Get("/vehicleCategory/readVehicleCategoryData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe vehicleCategory1.asJson
      }
    }

    "readDataForVehicleCategory If Invalid Input Is Passed " in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      Get("/vehicleCategory/readVehicleCategoryData/11111") ~> mockRoute ~> check {
        responseAs[String] shouldBe "vehicleCategory Id Is Invalid And Not Match In The Database".asJson
      }
    }

    "readDataForVehicleCategory If Empty List Is Passed " in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(List()))
      Get("/vehicleCategory/readVehicleCategoryData/11111") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Category Table Is Empty In The Database".asJson
      }
    }

    "getAllDataForVehicleCategory " in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      Get("/vehicleCategory/getAllVehicleCategoryData") ~> mockRoute ~> check {
        responseAs[String] shouldBe seqOfVehicleCategory.asJson
      }
    }

    "insertDataForVehicleCategory If Valid Input Is Passed " in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(List()))
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.internalInsert(any[VehicleCategory])).thenReturn(Future.successful(1))
      val inputEntity: VehicleCategory = VehicleCategory(6, "10Wheeler", None, 1750)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicleCategory/insertDataForVehicleCategory", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "insertDataForVehicleCategory If Invalid Input Is Passed =Name Is Empty" in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(List()))
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.internalInsert(any[VehicleCategory])).thenReturn(Future.successful(1))
      val inputEntity: VehicleCategory = VehicleCategory(6, "", None, 1750)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/vehicleCategory/insertDataForVehicleCategory", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Vehicle Category Name Is Empty".asJson
      }
    }

    "updateDataForVehicleCategory If Valid Input Is Passed " in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.internalUpdate(any[Long], any[VehicleCategory])).thenReturn(Future.successful(1))

      val inputEntity: VehicleCategory = VehicleCategory(5, "8Wheeler", None, 1250)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicleCategory/updateOperationForVehicleCategory/5", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "updateDataForVehicleCategory If Invalid Input Is Passed " in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.internalUpdate(any[Long], any[VehicleCategory])).thenReturn(Future.successful(1))

      val inputEntity: VehicleCategory = VehicleCategory(5, "8Wheeler", None, 1250)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicleCategory/updateOperationForVehicleCategory/555", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Id For Update VehicleCategory".asJson
      }
    }

    "updateDataForVehicleCategory If Invalid Input Is Passed =Name Is Not Defined" in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.internalUpdate(any[Long], any[VehicleCategory])).thenReturn(Future.successful(1))

      val inputEntity: VehicleCategory = VehicleCategory(5, "", None, 1250)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicleCategory/updateOperationForVehicleCategory/5", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "The Category Name is Not Defined".asJson
      }
    }

    "updateDataForVehicleCategory If Empty List Is Passed " in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.internalUpdate(any[Long], any[VehicleCategory])).thenReturn(Future.successful(1))

      val inputEntity: VehicleCategory = VehicleCategory(5, "8Wheeler", None, 1723)
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Put("/vehicleCategory/updateOperationForVehicleCategory/5", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Category Table Is Empty In The Database".asJson
      }
    }

    "DeleteDataRowForVehicleCategory If Valid Input Is Passed " in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      when(MockVehicleCategoryServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      Delete("/vehicleCategory/deleteVehicleCategoryData/5") ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForVehicleCategory If Invalid Input Is Passed " in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(seqOfVehicleCategory))
      when(MockVehicleCategoryServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/vehicleCategory/deleteVehicleCategoryData/5555") ~> mockRoute ~> check {
        responseAs[String] shouldBe "User Entered The Invalid Id For The Vehicle Category Table".asJson
      }
    }

    "DeleteDataRowForVehicleCategory If Empty List Is Passed " in {
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.vehicleCategoryFuture).thenReturn(Future.successful(Nil))
      when(MockVehicleCategoryServiceAndRoute.vehicleTypeRepository.vehicleTypeFuture).thenReturn(Future.successful(seqOfVehicleType))
      when(MockVehicleCategoryServiceAndRoute.vehicleCategoryRepository.internalDelete(any[Long])).thenReturn(Future.successful(1))

      Delete("/vehicleCategory/deleteVehicleCategoryData/5555") ~> mockRoute ~> check {
        responseAs[String] shouldBe "Vehicle Category Table Is Empty In The Database".asJson
      }
    }


  } //endofprocess
}

object MockVehicleCategoryServiceAndRoute extends VehicleCategoryService with MockVehicleCategoryFacade