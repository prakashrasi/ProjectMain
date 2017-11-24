package com.reactore.bookfeatures.testcase.rest

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.bookcore.{Department, DirectiveWithGenericErrorHandling}
import com.reactore.bookfeatures._
import com.reactore.bookfeatures.testcase.MockDataForGlobalTestFeatures
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RouteTestForDepartment extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar with DirectiveWithGenericErrorHandling {

  val mockRepo          : DepartmentRepositry = mock[DepartmentRepositry]
  val mockDepartmentRest: DepartmentRoute     = new DepartmentRoute(mockRepo)
  val mockRoute         : Route               = mockDepartmentRest.departmentRoute

  "Process The Department Route Test Logic" should {

    "readDataForDepartment" in {
      when(mockRepo.findById(any[Long])).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.department3)))
      Get("/department/readDepartmentData/3") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(MockDataForGlobalTestFeatures.department3).asJson
      }
    }

    "getAllDepartmentData" in {
      when(mockRepo.getAll).thenReturn(Future.successful(MockDataForGlobalTestFeatures.seqOfDepartment))
      Get("/department/getAllDepartmentData") ~> mockRoute ~> check {
        responseAs[String] shouldBe MockDataForGlobalTestFeatures.seqOfDepartment.asJson
      }
    }

    "CreateDataForDepartment With Entity Method " in {
      when(mockRepo.create(any[Department])).thenReturn(Future.successful(MockDataForGlobalTestFeatures.department4))
      Post("/department/createDepartmentData").withEntity(MockDataForGlobalTestFeatures.department4.asJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe MockDataForGlobalTestFeatures.department4.asJson
      }
    }

    "updateDataForDepartment If Valid Input Is Passed " in {
      when(mockRepo.updateById(any[Long], any[Department])).thenReturn(Future.successful(1))
      Put("/department/updateDepartmentData/4", MockDataForGlobalTestFeatures.department4.asJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForDepartment If Valid Input Is Passed " in {
      when(mockRepo.deleteById(any[Long])).thenReturn(Future.successful(true))
      Delete("/department/deleteDepartmentData/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe "true"
      }
    }

    "getAllDepartments which are having head of department" in {
      when(mockRepo.getAllDepartments).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.department6)))
      Get("/department/getAllDepartments") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(MockDataForGlobalTestFeatures.department6).asJson
      }
    }

  } //endofprocess
}