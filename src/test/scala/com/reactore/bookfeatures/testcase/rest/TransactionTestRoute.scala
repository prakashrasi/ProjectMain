package com.reactore.bookfeatures.testcase.rest

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.bookcore.{DateTimeOnly, DirectiveWithGenericErrorHandling, Transaction}
import com.reactore.bookfeatures._
import com.reactore.bookfeatures.testcase.MockDataForGlobalTestFeatures
import org.joda.time.DateTime
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RouteTestForTransaction extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar with DirectiveWithGenericErrorHandling {

  val mockRepo           : TransactionRepositry = mock[TransactionRepositry]
  val mockTransactionRest: TransactionRoute     = new TransactionRoute(mockRepo)
  val mockRoute          : Route                = mockTransactionRest.transactionRoute

  "Process The Transaction Route Test Logic" should {

    "readDataForTransaction" in {
      when(mockRepo.findById(any[Long])).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.transaction1)))
      Get("/transaction/readTransactionData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(MockDataForGlobalTestFeatures.transaction1).asJson
      }
    }

    "getAllTransactionData" in {
      when(mockRepo.getAll).thenReturn(Future.successful(MockDataForGlobalTestFeatures.seqOfTransaction))
      Get("/transaction/getAllTransactionData") ~> mockRoute ~> check {
        responseAs[String] shouldBe MockDataForGlobalTestFeatures.seqOfTransaction.asJson
      }
    }


    "CreateDataForTransaction With Entity Method " in {
      when(mockRepo.create(any[Transaction])).thenReturn(Future.successful(MockDataForGlobalTestFeatures.transaction1))
      Post("/transaction/createTransactionData").withEntity(MockDataForGlobalTestFeatures.transaction1.asJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe MockDataForGlobalTestFeatures.transaction1.asJson
      }
    }

    "updateDataForTransaction If Valid Input Is Passed " in {
      when(mockRepo.updateById(any[Long], any[Transaction])).thenReturn(Future.successful(1))
      Put("/transaction/updateTransactionData/4", MockDataForGlobalTestFeatures.transaction4.asJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForTransaction If Valid Input Is Passed " in {
      when(mockRepo.deleteById(any[Long])).thenReturn(Future.successful(true))
      Delete("/transaction/deleteTransactionData/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe "true"
      }
    }

    "getAllTransactionComparedWithDateAndFine" in {
      when(mockRepo.getAllTransactionComparedWithDateAndFine(any[DateTime])).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.transaction5)))
      val jodaDateTime: DateTime = DateTime.parse("2017-10-10T00:00:00")
      val dateTime: DateTimeOnly = DateTimeOnly(jodaDateTime)
      val inputInTheFormOfJson: String = dateTime.asJson
      Post("/transaction/getAllTransactionComparedWithDateAndFine", inputInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(MockDataForGlobalTestFeatures.transaction5).asJson
      }
    }
  } //endofprocess
}