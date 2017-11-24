package com.reactore.bookfeatures

import scala.concurrent.ExecutionContext.Implicits.global
import com.reactore.bookcore._
import slick.lifted.TableQuery
import MyTables._
import org.joda.time.DateTime

import scala.concurrent.Future

class EmployeeRepositry extends DatabaseCrudRepository[EmployeeTable, Employee](clazz = TableQuery[EmployeeTable]) {

  def findAll: Future[Seq[Employee]] = {
    getAll
  }

  /** 4. get employees having date of joining greater than 2017-01-01 and
    * employment type as permanent **/
  def getEmployeesComparedWithDateOfJoiningAndPermanent(jodaDateTime: DateTime): Future[Seq[Employee]] = {
    for {
      allEmployeeSeq <- findAll
      _ = if (allEmployeeSeq.isEmpty) throw EmptyListException(message = "There Is No Such Books", exception = new Exception)
      finalResult = allEmployeeSeq.filter(employee => employee.dateOfJoining.isAfter(jodaDateTime) && employee.employeeType.isDefined && employee.employeeType.get.toLowerCase == "permanent")
    } yield finalResult
  }


}