package com.reactore.bookfeatures

import java.sql.Timestamp
import java.text.SimpleDateFormat

import akka.http.scaladsl.server.Route
import com.reactore.bookcore._
import org.json4s.native.Serialization._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class TransactionRoute(transactionRepositry: TransactionRepositry) extends DirectiveWithGenericErrorHandling {
  val transactionRoute: Route = pathPrefix("transaction") {
    path("readTransactionData" / LongNumber) { id =>
      get {
        complete(respond(transactionRepositry.findById(id)))
      }
    } ~ path("deleteTransactionData" / LongNumber) { id =>
      delete {
        complete(respond(transactionRepositry.deleteById(id)))
      }
    } ~ path("createTransactionData") {
      post {
        entity(as[String]) { postBody =>
          val entity: Transaction = read[Transaction](postBody)
          complete(respond(transactionRepositry.create(entity)))
        }
      }
    } ~ path("updateTransactionData" / LongNumber) { updatePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val entity: Transaction = read[Transaction](postBody)
          complete(respond(transactionRepositry.updateById(updatePrimaryId, entity)))
        }
      }
    } ~ path("getAllTransactionData") {
      get {
        complete(respond(transactionRepositry.getAll))
      }
    } ~ path("getAllTransactionComparedWithDateAndFine") {
      post {
        entity(as[String]) { postBody =>
          val dateTime: DateTimeOnly = read[DateTimeOnly](postBody)
          complete(respond(transactionRepositry.getAllTransactionComparedWithDateAndFine(dateTime.jodaDateTime)))
        }
      }
    }
  }
}