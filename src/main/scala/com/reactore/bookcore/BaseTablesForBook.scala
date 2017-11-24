package com.reactore.bookcore

import org.joda.time.DateTime
import slick.jdbc.PostgresProfile.api._
import slick.lifted.Tag
import slick.sql.SqlProfile.ColumnOption

abstract class BaseTable[T](tag: Tag, schemaName: Option[String], tableName: String) extends Table[T](tag, schemaName, tableName) {
  val id       : Rep[Long]    = column[Long]("id", O.PrimaryKey, O.AutoInc)
  val isRemoved: Rep[Boolean] = column[Boolean]("isRemoved", O.Default(false))
}

trait Persistable[TId <: AnyVal] {
  val id: TId
}


object MyTables extends {
  val profile = com.reactore.bookcore.CustomPostgresDriver
  val driver  = MyPostgresDriver
} with MyTables


trait MyTables {
  val profile: com.reactore.bookcore.CustomPostgresDriver

  import profile.api._

  class BookTable(_tableTag: Tag) extends BaseTable[Book](_tableTag, Some("library"), "Book") {

    def * = (id, bookTitle, publication, authorName, categoryOrSubject, price, entryDate, edition, numberOfCopies, condition, remarks, isReferencecopy, isRemoved) <> (Book.tupled, Book.unapply)

    override val id: Rep[Long] = column[Long]("BookId", O.AutoInc, O.PrimaryKey)
    val bookTitle        : Rep[String]         = column[String]("BookTitle")
    val publication      : Rep[Option[String]] = column[Option[String]]("Publication", O.Default(Some("NO DETAILS FOR PUBLICATION")))
    val authorName       : Rep[String]         = column[String]("AuthorName")
    val categoryOrSubject: Rep[Option[String]] = column[Option[String]]("CategoryOrSubject", O.Default(Some("NO DETAILS FOR Category")))
    val price            : Rep[Long]           = column[Long]("Price")
    val entryDate        : Rep[DateTime]       = column[DateTime]("EntryDate", ColumnOption.SqlType("timestamp without time zone"))
    val edition          : Rep[Option[String]] = column[Option[String]]("Edition", O.Default(Some("NO EDITION")))
    val numberOfCopies   : Rep[Long]           = column[Long]("NumberOfCopies")
    val condition        : Rep[String]         = column[String]("Condition")
    val remarks          : Rep[Option[String]] = column[Option[String]]("Remarks", O.Default(Some("NO REMARKS")))
    val isReferencecopy  : Rep[Boolean]        = column[Boolean]("IsReferenceCopy", O.Default(false))
    override val isRemoved: Rep[Boolean] = column[Boolean]("IsRemoved", O.Default(false))
  }

  lazy val book = new TableQuery(tag => new BookTable(tag))

  class DepartmentTable(_tableTag: Tag) extends BaseTable[Department](_tableTag, Some("library"), "Department") {
    def * = (id, departmentName, departmentHead, isRemoved) <> (Department.tupled, Department.unapply)

    override val id: Rep[Long] = column[Long]("DepartmentId", O.AutoInc, O.PrimaryKey)
    val departmentName: Rep[Option[String]] = column[Option[String]]("DepartmentName", O.Default(Some("None")))
    val departmentHead: Rep[Option[String]] = column[Option[String]]("DepartmentHead", O.Default(Some("None")))
    override val isRemoved: Rep[Boolean] = column[Boolean]("IsRemoved", O.Default(false))
  }

  lazy val department = new TableQuery(tag => new DepartmentTable(tag))


  class EmployeeTable(_tableTag: Tag) extends BaseTable[Employee](_tableTag, Some("library"), "Employee") {
    def * = (id, firstName, lastName, dateOfBirth, gender, emailId, mobileNumber, address, dateOfJoining, employeeType, employeeStatus, isRemoved) <> (Employee.tupled, Employee.unapply)

    override val id: Rep[Long] = column[Long]("EmployeeId", O.AutoInc, O.PrimaryKey)
    val firstName     : Rep[String]         = column[String]("FirstName")
    val lastName      : Rep[Option[String]] = column[Option[String]]("LastName", O.Default(Some("None")))
    val dateOfBirth   : Rep[DateTime]       = column[DateTime]("DateOfBirth")
    val gender        : Rep[Option[String]] = column[Option[String]]("Gender", O.Default(Some("None")))
    val emailId       : Rep[String]         = column[String]("EmailId")
    val mobileNumber  : Rep[Long]           = column[Long]("MobileNumber")
    val address       : Rep[String]         = column[String]("Address")
    val dateOfJoining : Rep[DateTime]       = column[DateTime]("DateOfJoining", ColumnOption.SqlType("timestamp without time zone"))
    val employeeType  : Rep[Option[String]] = column[Option[String]]("EmployeeType", O.Default(Some("None")))
    val employeeStatus: Rep[String]         = column[String]("EmployeeStatus")
    override val isRemoved: Rep[Boolean] = column[Boolean]("IsRemoved", O.Default(false))
  }

  lazy val employee = new TableQuery(tag => new EmployeeTable(tag))

  class TransactionTable(_tableTag: Tag) extends BaseTable[Transaction](_tableTag, Some("library"), "Transaction") {
    def * = (id, bookId, issuedTo, issueDate, receivedDate, fine, remarks, isRemoved) <> (Transaction.tupled, Transaction.unapply)

    override val id: Rep[Long] = column[Long]("TransactionId", O.AutoInc, O.PrimaryKey)
    val bookId      : Rep[Long]           = column[Long]("BookId")
    val issuedTo    : Rep[Long]           = column[Long]("IssuedTo")
    val issueDate   : Rep[DateTime]       = column[DateTime]("IssueDate", ColumnOption.SqlType("timestamp without time zone"))
    val receivedDate: Rep[DateTime]       = column[DateTime]("ReceivedDate", ColumnOption.SqlType("timestamp without time zone"))
    val fine        : Rep[Option[Long]]   = column[Option[Long]]("Fine", O.Default(Some(0L)))
    val remarks     : Rep[Option[String]] = column[Option[String]]("Remarks", O.Default(Some("NO REMARKS")))
    override val isRemoved: Rep[Boolean] = column[Boolean]("IsRemoved", O.Default(false))

  }

  lazy val transaction = new TableQuery(tag => new TransactionTable(tag))


  class EmployeeAvailabilityTable(_tableTag: Tag) extends BaseTable[EmployeeAvailability](_tableTag, Some("library"), "EmployeeAvailability") {
    def * = (id, employeeId, category, startDate, endDate, isRemoved) <> (EmployeeAvailability.tupled, EmployeeAvailability.unapply)

    override val id: Rep[Long] = column[Long]("EmployeeAvailabilityId", O.AutoInc, O.PrimaryKey)
    val employeeId: Rep[Long]     = column[Long]("EmployeeId")
    val category  : Rep[Long]     = column[Long]("Category")
    val startDate : Rep[DateTime] = column[DateTime]("StartDate", ColumnOption.SqlType("timestamp without time zone"))
    val endDate   : Rep[DateTime] = column[DateTime]("EndDate", ColumnOption.SqlType("timestamp without time zone"))
    override val isRemoved: Rep[Boolean] = column[Boolean]("IsRemoved", O.Default(false))
  }

  lazy val employeeavailability = new TableQuery(tag => new EmployeeAvailabilityTable(tag))
}