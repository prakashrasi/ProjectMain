package com.reactore.bookfeatures

import akka.http.scaladsl.server.Route
import com.reactore.bookcore._
import org.json4s.native.Serialization._

import scala.concurrent.Future

class BookRoute(bookRepositry: BookRepositry) extends DirectiveWithGenericErrorHandling {
  val bookRoute: Route = pathPrefix("book") {
    path("readBookData" / LongNumber) { bookId =>
      get {
        complete(respond(bookRepositry.findById(bookId)))
      }
    } ~ path("deleteBookData" / LongNumber) { bookId =>
      delete {
        complete(respond(bookRepositry.deleteById(bookId)))
      }
    } ~ path("createBookData") {
      post {
        entity(as[String]) { postBody =>
          val bookEntity: Book = read[Book](postBody)
          complete(respond(bookRepositry.create(bookEntity)))
        }
      }
    } ~ path("updateBookData" / LongNumber) { updatePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val bookEntity: Book = read[Book](postBody)
          complete(respond(bookRepositry.updateById(updatePrimaryId, bookEntity)))
        }
      }
    } ~ path("getAllBookData") {
      get {
        complete(respond(bookRepositry.getAll))
      }
    } ~ path("getBookByPrice" / LongNumber) { price =>
      get {
        getBPrice(price)
      }
    } ~ path("getDetailsOfBookWithPriceAndCopies" / LongNumber / LongNumber) { (price, copies) =>
      get {
        complete(respond(bookRepositry.getDetailsOfBookWithPriceAndCopies(price, copies)))
      }
    } ~ path("listOfBookNotReferenceCopy") {
      get {
        complete(respond(bookRepositry.listOfBookNotReferenceCopy))
      }
    }
  }

  private def getBPrice(price: Long) = {
    val result: Future[Seq[Book]] = bookRepositry.getBookByPrice(price)
    complete(respond(result))
  }
}