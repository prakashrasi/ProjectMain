package com.reactore.bookfeatures.testcase.rest

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.reactore.bookcore.Book
import com.reactore.bookcore.DirectiveWithGenericErrorHandling._
import com.reactore.bookfeatures.testcase.MockDataForGlobalTestFeatures
import com.reactore.bookfeatures.{BookRepositry, BookRoute}
import org.joda.time.DateTime
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RouteTestForBook extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar {

  val mockRepo    : BookRepositry = mock[BookRepositry]
  val mockBookRest: BookRoute     = new BookRoute(mockRepo)
  val mockRoute   : Route         = mockBookRest.bookRoute

  "Process The Book Route Test Logic" should {

    "readDataForBook" in {
      when(mockRepo.findById(any[Long])).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.book1)))
      Get("/book/readBookData/1") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(MockDataForGlobalTestFeatures.book1).asJson
      }
    }

    "getAllBookData" in {
      when(mockRepo.getAll).thenReturn(Future.successful(MockDataForGlobalTestFeatures.seqOfBook))
      Get("/book/getAllBookData") ~> mockRoute ~> check {
        responseAs[String] shouldBe MockDataForGlobalTestFeatures.seqOfBook.asJson
      }
    }

    "CreateDataForBook" in {
      val inputEntity: Book = Book(6, "M6", None, "Vignesh Kumar", None, 1400, DateTime.parse("2016-10-14T02:55:40"), None, 14, "Good", None)
      when(mockRepo.create(any[Book])).thenReturn(Future.successful(inputEntity))
      val inputEntityInTheFormOfJson: String = inputEntity.asJson
      Post("/book/createBookData", inputEntityInTheFormOfJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe inputEntity.asJson
      }
    }

    "CreateDataForBook With Entity Method " in {
      when(mockRepo.create(any[Book])).thenReturn(Future.successful(MockDataForGlobalTestFeatures.book4))
      Post("/book/createBookData").withEntity(MockDataForGlobalTestFeatures.book4.asJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe MockDataForGlobalTestFeatures.book4.asJson
      }
    }

    "updateDataForBook If Valid Input Is Passed " in {
      when(mockRepo.updateById(any[Long], any[Book])).thenReturn(Future.successful(1))
      Put("/book/updateBookData/4", MockDataForGlobalTestFeatures.book4.asJson) ~> mockRoute ~> check {
        responseAs[String] shouldBe "1"
      }
    }

    "DeleteDataRowForBook If Valid Input Is Passed " in {
      when(mockRepo.deleteById(any[Long])).thenReturn(Future.successful(true))
      Delete("/book/deleteBookData/2") ~> mockRoute ~> check {
        responseAs[String] shouldBe "true"
      }
    }

    //1) Get Book by Price
    "getBookByPrice If Valid Input Is passed" in {
      when(mockRepo.getBookByPrice(any[Long])).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.book3)))
      Get("/book/getBookByPrice/1000") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(MockDataForGlobalTestFeatures.book3).asJson
      }
    }


    //2. get details of book which is having more price and more no of copies
    "getDetailsOfBook If Valid Input Is passed" in {
      when(mockRepo.getDetailsOfBookWithPriceAndCopies(any[Long], any[Long])).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.book4)))
      Get("/book/getDetailsOfBookWithPriceAndCopies/1000/8") ~> mockRoute ~> check {
        responseAs[String] shouldBe Seq(MockDataForGlobalTestFeatures.book4).asJson
      }
    }

    //3. List of all books which won't come under reference copy
    "listOfAllBooks If Valid Input Is passed" in {
      when(mockRepo.listOfBookNotReferenceCopy).thenReturn(Future.successful(MockDataForGlobalTestFeatures.seqOfBook))
      Get("/book/listOfBookNotReferenceCopy") ~> mockRoute ~> check {
        responseAs[String] shouldBe MockDataForGlobalTestFeatures.seqOfBook.asJson
      }
    }
  } //endofprocess
}
