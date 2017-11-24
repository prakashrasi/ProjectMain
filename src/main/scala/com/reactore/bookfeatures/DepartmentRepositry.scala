package com.reactore.bookfeatures

import com.reactore.bookcore._
import slick.lifted.TableQuery
import MyTables._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DepartmentRepositry extends DatabaseCrudRepository[DepartmentTable, Department](clazz = TableQuery[DepartmentTable]) {

  def findAll: Future[Seq[Department]] = {
    getAll
  }

  //6. get all departments which are having head of department
  def getAllDepartments: Future[Seq[Department]] = {
    for {
      allDepartmentSeq <- findAll
      _ = if (allDepartmentSeq.isEmpty) throw EmptyListException(message = "There Is No Such Departments", exception = new Exception)
      filterResult = allDepartmentSeq.filter(_.departmentHead.isDefined)
      finalResult = if (filterResult.nonEmpty) filterResult else throw NoSuchEntityException(message = "There Is No Departments For Head Of Department", exception = new Exception)
    } yield finalResult
  }

}

object ImplDepartmentRepositry extends DepartmentRepositry