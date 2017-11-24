package com.reactore.bookfeatures

import com.reactore.bookcore.MyTables._
import com.reactore.bookcore.{EmployeeAvailability, _}
import slick.lifted.TableQuery
import org.joda.time.{DateTime, DateTimeZone}

import scala.annotation.tailrec
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class EmployeeAvailabilityRepositry extends DatabaseCrudRepository[EmployeeAvailabilityTable, EmployeeAvailability](clazz = TableQuery[EmployeeAvailabilityTable]) {


  def findAll: Future[Seq[EmployeeAvailability]] = {
    getAll
  }

  //(1)getEmployeeAvailabilityByEmployeeId
  def getEmployeeAvailabilityByEmployeeId(employeeId: Long): Future[Seq[EmployeeAvailability]] = {
    for {
      allEmployeeAvailabilitySeq <- findAll
      _ = if (allEmployeeAvailabilitySeq.isEmpty) throw EmptyListException(message = "There Is No Such Employee Availability", exception = new Exception)
      filteredResult = allEmployeeAvailabilitySeq.filter(_.employeeId == employeeId)
      finalResult = if (filteredResult.nonEmpty) {
        filteredResult
      } else throw NoSuchEntityException(message = "There Is No Employee Availability For This Employee Id", exception = new Exception)
    } yield finalResult
  }

  /** (2) insertEntity => (a) check for the availability of employee first
    * on the input dates ,if not insert else throw duplicate exception **/
  def checkEmployeeAvailabilityOverlapping(employeeAvailability: EmployeeAvailability, databaseEmployeeAvailability: EmployeeAvailability): Boolean = {
    val _1 = if (employeeAvailability.startDate.isBefore(databaseEmployeeAvailability.startDate) && employeeAvailability.endDate.isBefore(databaseEmployeeAvailability.endDate)
      && employeeAvailability.endDate.isAfter(databaseEmployeeAvailability.startDate))
      throw EmployeeAvailabilityOverlappingException(message = "New Employee Availability Start Date Is Overlapping", exception = new Exception)

    val _2 = if (employeeAvailability.startDate.isAfter(databaseEmployeeAvailability.startDate) && employeeAvailability.endDate.isAfter(databaseEmployeeAvailability.endDate)
      && employeeAvailability.startDate.isBefore(databaseEmployeeAvailability.endDate))
      throw EmployeeAvailabilityOverlappingException(message = "New Employee Availability End date Is Overlapping", exception = new Exception)

    val _3 = if (employeeAvailability.startDate.isBefore(databaseEmployeeAvailability.startDate) && employeeAvailability.endDate.isAfter(databaseEmployeeAvailability.endDate))
      throw EmployeeAvailabilityOverlappingException(message = "New Employee Availability End date Is Overlapping", exception = new Exception)

    val result = if (employeeAvailability.startDate.isAfter(databaseEmployeeAvailability.startDate) && employeeAvailability.endDate.isBefore(databaseEmployeeAvailability.endDate))
      throw DuplicateEntityException(message = "New Employee Availability Of Start And End Date Both Are Overlapping", exception = new Exception) else true
    result
  }

  def insertEmployeeAvailabilityEntity(newEmployeeAvailability: EmployeeAvailability): Future[EmployeeAvailability] = {
    for {
      allEmployeeAvailabilitySeq <- findAll
      finalResult <- if (allEmployeeAvailabilitySeq.isEmpty) {
        create(newEmployeeAvailability)
      } else {
        val filteredResultByEmployeeId: Seq[EmployeeAvailability] = allEmployeeAvailabilitySeq.filter(_.employeeId == newEmployeeAvailability.employeeId)
        if (filteredResultByEmployeeId.isEmpty) {
          create(newEmployeeAvailability)
        } else {
          val seqOfEmployeeAvailability: Seq[EmployeeAvailability] = filteredResultByEmployeeId.filter(employeeAvail => checkEmployeeAvailabilityOverlapping(newEmployeeAvailability, employeeAvail))
          if (seqOfEmployeeAvailability.size == filteredResultByEmployeeId.size) {
            create(newEmployeeAvailability)
          }
          else Future.failed(DuplicateEntityException(message = "Duplicate Exception Is Occur  With Availability  Dates", exception = new Exception))
        }
      }
    } yield finalResult
  }

  //(4) filterEmployeeAvailabilityDates(row:EmployeeAvailability)
  @tailrec
  final def mergeWithOverlap(dateTimeRangeList: List[DateTimeRange], accumulator: List[DateTimeRange]): List[DateTimeRange] = {
    dateTimeRangeList match {
      case (presentDate :: nextDate :: restList) =>
        if (presentDate.end.isBefore(nextDate.start))
          mergeWithOverlap(nextDate :: restList, presentDate :: accumulator)
        else {
          val dateRange = DateTimeRange(presentDate.start, getMaxTime(presentDate.end, nextDate.end))
          mergeWithOverlap(dateRange :: restList, accumulator)
        }
      case _                                     =>
        val newListOfDateTimeRange: List[DateTimeRange] = (dateTimeRangeList ::: accumulator).reverse
        newListOfDateTimeRange
    }
  }

  def getMaxTime(dateTime1: DateTime, dateTime2: DateTime): DateTime = {
    if (dateTime1.getMillis > dateTime2.getMillis) dateTime1 else dateTime2
  }

  def filterEmployeeAvailabilityDates(employeeId: Long): Future[List[DateTimeRange]] = {
    for {
      allEmployeeAvailabilitySeq <- findAll
      _ = if (allEmployeeAvailabilitySeq.isEmpty) throw EmptyListException(message = "There Is No Such Employee Availability", exception = new Exception)
      filterEmployeeAvailabilityDates = allEmployeeAvailabilitySeq.filter(_.employeeId == employeeId)
      finalResult = if (filterEmployeeAvailabilityDates.nonEmpty) {
        val listOfDateTimeRange: List[DateTimeRange] = filterEmployeeAvailabilityDates.map {
          availability =>
            DateTimeRange(availability.startDate, availability.endDate)
        }.toList
        mergeWithOverlap(listOfDateTimeRange, Nil)
      } else throw NoSuchEntityException(message = "There Is No Employee Availability For This Employee Id", exception = new Exception)
    } yield finalResult
  }


  def updateEmployeeAvailability(id: Long, updateEntity: EmployeeAvailability): Future[Int] = {
    for {
      allEmployeeAvailabilitySeq <- findAll
      _ = if (allEmployeeAvailabilitySeq.isEmpty) throw EmptyListException(message = "There Is No Such Employee Availability In The Table", exception = new Exception)
      filteredResultBasedOnEmployeeId = allEmployeeAvailabilitySeq.filter(_.employeeId == updateEntity.employeeId)
      filteredNotEmployeeAvailability = filteredResultBasedOnEmployeeId.filterNot(entity => entity.id == id)
      finalResult <- if (filteredNotEmployeeAvailability.nonEmpty) {
        val seqOfAvailabilityOverlapingChecking: Seq[EmployeeAvailability] = filteredNotEmployeeAvailability.filter(dbEntity => checkEmployeeAvailabilityOverlapping(updateEntity, dbEntity))
        if (seqOfAvailabilityOverlapingChecking.size == filteredNotEmployeeAvailability.size) {
          updateById(id, updateEntity)
        } else Future.failed(DuplicateEntityException(message = "Duplicate Exception Is Occur  With Availability  Dates", exception = new Exception))
      } else updateById(id, updateEntity)
    } yield finalResult
  }

  def insertOrUpdateEmployeeAvailability(listEmployeeAvailability: List[EmployeeAvailability]): Future[InsertUpdateContainer] = {
    val (updateEmployeeList, insertEmployeeList): (List[EmployeeAvailability], List[EmployeeAvailability]) = listEmployeeAvailability.partition(_.id > 0)

    val listOfIntForUpdateField: List[Future[Int]] = (updateEmployeeList, insertEmployeeList)._1.map(update => updateEmployeeAvailability(update.id, update))
    val listOfEmployeeAvailabilityForInsertField: List[Future[EmployeeAvailability]] = (updateEmployeeList, insertEmployeeList)._2.map(insert => insertEmployeeAvailabilityEntity(insert))

    val updateResult: Future[List[Int]] = Future.sequence(listOfIntForUpdateField)
    val insertResult: Future[List[EmployeeAvailability]] = Future.sequence(listOfEmployeeAvailabilityForInsertField)

    for {
      listOfIntForUpdateField <- updateResult
      listOfEmployeeAvailabilityForInsertField <- insertResult
      finalResult = InsertUpdateContainer(listOfIntForUpdateField, listOfEmployeeAvailabilityForInsertField)
    } yield finalResult

    //(Future.sequence(listOfIntForUpdateField), Future.sequence(listOfEmployeeAvailabilityForInsertField))
  }

}