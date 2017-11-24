package com.reactore.bookfeatures.testcase.rest

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.bookcore.{DateTimeRange, EmployeeAvailability, InsertUpdateContainer}
import com.reactore.bookcore.DirectiveWithGenericErrorHandling._
import com.reactore.bookfeatures.testcase.MockDataForGlobalTestFeatures
import com.reactore.bookfeatures.{EmployeeAvailabilityRepositry, EmployeeAvailabilityRoute}
import org.joda.time.DateTime
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RouteTestForEmployeeAvailability extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar {

  val mockRepo                    : EmployeeAvailabilityRepositry = mock[EmployeeAvailabilityRepositry]
  val mockEmployeeAvailabilityRest: EmployeeAvailabilityRoute     = new EmployeeAvailabilityRoute(mockRepo)
  val mockRoute                   : Route                         = mockEmployeeAvailabilityRest.employeeAvailabilityRoute





  "Process The Employee Availability Route Test Logic" should {

    "readDataForEmployeeAvailability" in {
      when(mockRepo.findById(any[Long])).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.availability1)))
      Get("/employeeAvailability/readEmployeeAvailabilityData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(MockDataForGlobalTestFeatures.availability1).asJson
      }
    }

    "getAllEmployeeAvailabilityData" in {
      when(mockRepo.getAll).thenReturn(Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability))
      Get("/employeeAvailability/getAllEmployeeAvailabilityData") ~> mockRoute ~> check {
        responseAs[String] shouldBe MockDataForGlobalTestFeatures.seqOfEmployeeAvailability.asJson
      }
    }

    "CreateDataForEmployeeAvailability" in {
      val inputEntity: EmployeeAvailability = EmployeeAvailability(5, 3, 2, DateTime.parse("2011-06-22T02:55:40"), DateTime.parse("2011-06-26T02:55:40"))
      when(mockRepo.create(any[EmployeeAvailability])).thenReturn(Future.successful(inputEntity))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/employeeAvailability/createEmployeeAvailabilityData", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe inputEntity.asJson
      }
    }


    "updateDataForEmployeeAvailability If Valid Input Is Passed " in {
      when(mockRepo.updateById(any[Long], any[EmployeeAvailability])).thenReturn(Future.successful(1))
      Put("/employeeAvailability/updateEmployeeAvailabilityData/4", MockDataForGlobalTestFeatures.availability4.asJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForEmployeeAvailability If Valid Input Is Passed " in {
      when(mockRepo.deleteById(any[Long])).thenReturn(Future.successful(true))
      Delete("/employeeAvailability/deleteEmployeeAvailabilityData/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe "true"
      }
    }

    "getEmployeeAvailabilityByEmployeeId" in {
      when(mockRepo.getEmployeeAvailabilityByEmployeeId(any[Long])).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.availability4)))
      Get("/employeeAvailability/getEmployeeAvailabilityByEmployeeId/4") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(MockDataForGlobalTestFeatures.availability4).asJson
      }
    }

    "insertEmployeeAvailabilityEntity" in {
      val employeeAvailability: EmployeeAvailability = EmployeeAvailability(5, 4, 2, DateTime.parse("2016-07-22T02:55:40"), DateTime.parse("2016-07-26T02:55:40"))
      when(mockRepo.insertEmployeeAvailabilityEntity(any[EmployeeAvailability])).thenReturn(Future.successful(employeeAvailability))
      val inputInTheFormOfJson: String = employeeAvailability.asJson
      Post("/employeeAvailability/insertEmployeeAvailabilityEntity", inputInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe employeeAvailability.asJson
      }
    }

    "filterEmployeeAvailabilityDates" in {
      val expectedResponse: List[DateTimeRange] = List(DateTimeRange(DateTime.parse("2014-02-18T02:55:40"), DateTime.parse("2014-02-27T02:55:40")))
      when(mockRepo.filterEmployeeAvailabilityDates(any[Long])).thenReturn(Future.successful(expectedResponse))
      Get("/employeeAvailability/filterEmployeeAvailabilityDates/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe expectedResponse.asJson
      }
    }

    /** insertOrUpdateEmployeeAvailability Route Test Case **/

    "insertOrUpdateEmployeeAvailability" in {
      val listOfAvailability: List[EmployeeAvailability] = List(EmployeeAvailability(4, 3, 222, DateTime.parse("2016-07-22T02:55:40"), DateTime.parse("2016-07-26T02:55:40")), EmployeeAvailability(0, 2, 2, DateTime.parse("2016-05-22T02:55:40"), DateTime.parse("2016-05-26T02:55:40")))
      val expectedOutput: InsertUpdateContainer = InsertUpdateContainer(List(1), List(EmployeeAvailability(4, 3, 222, DateTime.parse("2016-07-22T02:55:40"), DateTime.parse("2016-07-26T02:55:40")), EmployeeAvailability(0, 2, 2, DateTime.parse("2016-05-22T02:55:40"), DateTime.parse("2016-05-26T02:55:40"))))
      when(mockRepo.insertOrUpdateEmployeeAvailability(any[List[EmployeeAvailability]])).thenReturn(Future.successful(expectedOutput))
      val inputInTheFormOfJson: String = listOfAvailability.asJson
      Post("/employeeAvailability/insertOrUpdateEmployeeAvailability", inputInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe expectedOutput.asJson
      }
    }

    "[insertOrUpdateEmployeeAvailability] Valid Input For  Update The Record For The First EmployeeId In The Availability Table  " in {
      val listOfAvailability: List[EmployeeAvailability] = List(EmployeeAvailability(4, 3, 222, DateTime.parse("2016-07-22T02:55:40"), DateTime.parse("2016-07-26T02:55:40")), EmployeeAvailability(0, 2, 2, DateTime.parse("2016-05-22T02:55:40"), DateTime.parse("2016-05-26T02:55:40")))
      val expectedOutput: InsertUpdateContainer = InsertUpdateContainer(List(1), List(EmployeeAvailability(0, 2, 2, DateTime.parse("2016-05-22T02:55:40"), DateTime.parse("2016-05-26T02:55:40"), false)))
      when(mockRepo.insertOrUpdateEmployeeAvailability(any[List[EmployeeAvailability]])).thenReturn(Future.successful(expectedOutput))
      val inputInTheFormOfJson: String = listOfAvailability.asJson
      Post("/employeeAvailability/insertOrUpdateEmployeeAvailability", inputInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe expectedOutput.asJson
      }
    }

    "[insertOrUpdateEmployeeAvailability] Valid For Insert The  New Employee Id=7 In The Availability Table " in {
      val listOfAvailability2: List[EmployeeAvailability] = List(EmployeeAvailability(4, 3, 222, DateTime.parse("2016-09-22T02:55:40"), DateTime.parse("2016-09-26T02:55:40")), EmployeeAvailability(0, 2, 2, DateTime.parse("2017-07-07T02:55:40"), DateTime.parse("2017-07-08T02:55:40")))
      val expectedOutput: InsertUpdateContainer = InsertUpdateContainer(List(1), List(EmployeeAvailability(0, 7, 2, DateTime.parse("2017-07-07T02:55:40"), DateTime.parse("2017-07-08T02:55:40"), false)))
      when(mockRepo.insertOrUpdateEmployeeAvailability(any[List[EmployeeAvailability]])).thenReturn(Future.successful(expectedOutput))
      val inputInTheFormOfJson: String = listOfAvailability2.asJson
      Post("/employeeAvailability/insertOrUpdateEmployeeAvailability", inputInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe expectedOutput.asJson
      }
    }


  } //endofprocess
}