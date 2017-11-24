package com.reactore.bookfeatures

import com.reactore.bookcore.MyTables._
import com.reactore.bookcore._
import org.joda.time.DateTime
import slick.lifted.TableQuery

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TransactionRepositry extends DatabaseCrudRepository[TransactionTable, Transaction](TableQuery[TransactionTable]) {

  def findAll: Future[Seq[Transaction]] = {
    getAll
  }

  //5. get all transactions happened on 2017-10-10 and status as not fine
  def getAllTransactionComparedWithDateAndFine(jodaDateTime: DateTime): Future[Seq[Transaction]] = {
    println(jodaDateTime)
    for {
      allSeqTransaction <- findAll
      _ = if (allSeqTransaction.isEmpty) throw EmptyListException(message = "There Is No Such Transaction", exception = new Exception)
      transactionFilterResult = allSeqTransaction.filter(transaction => transaction.issueDate.isEqual(jodaDateTime)
        && transaction.fine.isDefined && transaction.fine.get == 0)
      finalResult = if (transactionFilterResult.nonEmpty) transactionFilterResult else throw NoSuchEntityException(message = "There Is No Transaction For This Date And Fine", exception = new Exception)
    } yield finalResult
  }
}
