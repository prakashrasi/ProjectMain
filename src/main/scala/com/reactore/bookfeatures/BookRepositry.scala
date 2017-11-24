package com.reactore.bookfeatures

import com.reactore.bookcore._
import slick.lifted.TableQuery
import MyTables._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class BookRepositry extends DatabaseCrudRepository[BookTable, Book](clazz = TableQuery[BookTable]) {
  
  def findAll: Future[Seq[Book]] = {
    getAll
  }
  //1) Get Book by Price
  def getBookByPrice(price: Long): Future[Seq[Book]] = {
    for {
      allBookSeq <- findAll
      _ = if (allBookSeq.isEmpty) throw EmptyListException(message = "There Is No Such Books", exception = new Exception)
      filterResult = allBookSeq.filter(_.price == price)
      finalResult = if (filterResult.nonEmpty) filterResult else throw NoSuchEntityException(message = "There Is No Book For This Price", exception = new Exception)
    } yield finalResult
  }

  //2. get details of book which is having more price and more no of copies
  def getDetailsOfBookWithPriceAndCopies(price: Long, copies: Long): Future[Seq[Book]] = {
    for {
      allBookSeq <- findAll
      _ = if (allBookSeq.isEmpty) throw EmptyListException(message = "There Is No Such Books", exception = new Exception)
      finalResult = allBookSeq.filter(book => book.price > price && book.numberOfCopies > copies)
    } yield finalResult
  }

  //3. List of all books which won't come under reference copy
  def listOfBookNotReferenceCopy: Future[Seq[Book]] = {
    for {
      allBookSeq <- findAll
      _ = if (allBookSeq.isEmpty) throw EmptyListException(message = "There Is No Such Books", exception = new Exception)
      bookResult = allBookSeq.filterNot(_.isReferenceCopy)
      finalResult = if (bookResult.nonEmpty) bookResult else throw NoSuchEntityException(message = "There Is No Enitty", exception = new Exception)
    } yield finalResult
  }

}

object ImplBookRepository extends BookRepositry

