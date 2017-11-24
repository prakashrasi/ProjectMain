package com.reactore.bookfeatures.testcase.rest

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.bookcore.{DirectiveWithGenericErrorHandling, Employee}
import com.reactore.bookfeatures.testcase.MockDataForGlobalTestFeatures
import com.reactore.bookfeatures.{EmployeeRepositry, EmployeeRoute}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RouteTestForEmployee extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar with DirectiveWithGenericErrorHandling {

  val mockRepo        : EmployeeRepositry = mock[EmployeeRepositry]
  val mockEmployeeRest: EmployeeRoute     = new EmployeeRoute(mockRepo)
  val mockRoute       : Route             = mockEmployeeRest.employeeRoute

  "Process The Employee Route Test Logic" should {

    "readDataForEmployee" in {
      when(mockRepo.findById(any[Long])).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.employee2)))
      Get("/employee/readEmployeeData/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(MockDataForGlobalTestFeatures.employee2).asJson
      }
    }

    "getAllEmployeeData" in {
      when(mockRepo.getAll).thenReturn(Future.successful(MockDataForGlobalTestFeatures.seqOfEmployee))
      Get("/employee/getAllEmployeeData") ~> mockRoute ~> check {
        responseAs[String] shouldBe MockDataForGlobalTestFeatures.seqOfEmployee.asJson
      }
    }


    "CreateDataForEmployee With Entity Method " in {
      when(mockRepo.create(any[Employee])).thenReturn(Future.successful(MockDataForGlobalTestFeatures.employee2))
      Post("/employee/createEmployeeData").withEntity(MockDataForGlobalTestFeatures.employee2.asJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe MockDataForGlobalTestFeatures.employee2.asJson
      }
    }

    "updateDataForEmployee If Valid Input Is Passed " in {
      when(mockRepo.updateById(any[Long], any[Employee])).thenReturn(Future.successful(1))
      Put("/employee/updateEmployeeData/4", MockDataForGlobalTestFeatures.employee4.asJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForEmployee If Valid Input Is Passed " in {
      when(mockRepo.deleteById(any[Long])).thenReturn(Future.successful(true))
      Delete("/employee/deleteEmployeeData/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe "true"
      }
    }
  } //endofprocess
}