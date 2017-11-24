package com.reactore.bookfeatures

import com.reactore.bookcore.{BaseTable, DBFiles, MyPostgresDriver, Persistable}
import slick.lifted.TableQuery
import com.reactore.bookcore.MyPostgresDriver.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

abstract class DatabaseCrudRepository[T <: BaseTable[E], E <: Persistable[Long]](clazz: TableQuery[T]) extends DBFiles {

  val query: MyPostgresDriver.api.type#TableQuery[T] = clazz

  def getAll: Future[Seq[E]] = {
    for {
      res <- database.run(clazz.filter(_.isRemoved === false).result)
      _ = println("res = " + res)
    } yield res
  }

  def create(entity: E): Future[E] = {
    database.run(clazz returning clazz.map(obj => obj) += entity)
  }

  def updateById(id: Long, entity: E): Future[Int] = {
    database.run(clazz.filter(_.id === id).filter(_.isRemoved === false).update(entity))
  }

  def findById(id: Long): Future[Seq[E]] = {
    database.run(query.filter(_.id === id).filter(_.isRemoved === false).result)
  }

  def deleteById(id: Long): Future[Boolean] = {
    val res = clazz.filter(_.id === id).map(x => x.isRemoved).update(true)
    database.run(res).map(_ == 1)
  }
}