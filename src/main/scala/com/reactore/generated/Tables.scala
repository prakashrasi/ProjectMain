package generated

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.PostgresProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile

  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Book.schema ++ Department.schema ++ Employee.schema ++ Employeeavailability.schema ++ Transaction.schema

  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Book
    *
    * @param bookid            Database column BookId SqlType(bigserial), AutoInc, PrimaryKey
    * @param booktitle         Database column BookTitle SqlType(varchar)
    * @param publication       Database column Publication SqlType(varchar), Default(Some(NO DETAILS FOR PUBLICATION))
    * @param authorname        Database column AuthorName SqlType(varchar)
    * @param categoryorsubject Database column CategoryOrSubject SqlType(varchar), Default(Some(NO DETAILS FOR Category))
    * @param price             Database column Price SqlType(int8)
    * @param entrydate         Database column EntryDate SqlType(timestamp)
    * @param edition           Database column Edition SqlType(varchar), Default(Some(NO EDITION))
    * @param numberofcopies    Database column NumberOfCopies SqlType(int8)
    * @param condition         Database column Condition SqlType(varchar)
    * @param remarks           Database column Remarks SqlType(varchar), Default(Some(NO REMARKS))
    * @param isreferencecopy   Database column IsReferenceCopy SqlType(bool), Default(false)
    * @param isremoved         Database column IsRemoved SqlType(bool), Default(false) */
  final case class BookRow(bookid: Long, booktitle: String, publication: Option[String] = Some("NO DETAILS FOR PUBLICATION"), authorname: String, categoryorsubject: Option[String] = Some("NO DETAILS FOR Category"), price: Long, entrydate: java.sql.Timestamp, edition: Option[String] = Some("NO EDITION"), numberofcopies: Long, condition: String, remarks: Option[String] = Some("NO REMARKS"), isreferencecopy: Boolean = false, isremoved: Boolean = false)

  /** GetResult implicit for fetching BookRow objects using plain SQL queries */
  implicit def GetResultBookRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Timestamp], e4: GR[Boolean]): GR[BookRow] = GR {
    prs =>
      import prs._
      BookRow.tupled((<<[Long], <<[String], <<?[String], <<[String], <<?[String], <<[Long], <<[java.sql.Timestamp], <<?[String], <<[Long], <<[String], <<?[String], <<[Boolean], <<[Boolean]))
  }

  /** Table description of table Book. Objects of this class serve as prototypes for rows in queries. */
  class Book(_tableTag: Tag) extends profile.api.Table[BookRow](_tableTag, Some("library"), "Book") {
    def * = (bookid, booktitle, publication, authorname, categoryorsubject, price, entrydate, edition, numberofcopies, condition, remarks, isreferencecopy, isremoved) <> (BookRow.tupled, BookRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(bookid), Rep.Some(booktitle), publication, Rep.Some(authorname), categoryorsubject, Rep.Some(price), Rep.Some(entrydate), edition, Rep.Some(numberofcopies), Rep.Some(condition), remarks, Rep.Some(isreferencecopy), Rep.Some(isremoved)).shaped.<>({ r => import r._; _1.map(_ => BookRow.tupled((_1.get, _2.get, _3, _4.get, _5, _6.get, _7.get, _8, _9.get, _10.get, _11, _12.get, _13.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column BookId SqlType(bigserial), AutoInc, PrimaryKey */
    val bookid           : Rep[Long]               = column[Long]("BookId", O.AutoInc, O.PrimaryKey)
    /** Database column BookTitle SqlType(varchar) */
    val booktitle        : Rep[String]             = column[String]("BookTitle")
    /** Database column Publication SqlType(varchar), Default(Some(NO DETAILS FOR PUBLICATION)) */
    val publication      : Rep[Option[String]]     = column[Option[String]]("Publication", O.Default(Some("NO DETAILS FOR PUBLICATION")))
    /** Database column AuthorName SqlType(varchar) */
    val authorname       : Rep[String]             = column[String]("AuthorName")
    /** Database column CategoryOrSubject SqlType(varchar), Default(Some(NO DETAILS FOR Category)) */
    val categoryorsubject: Rep[Option[String]]     = column[Option[String]]("CategoryOrSubject", O.Default(Some("NO DETAILS FOR Category")))
    /** Database column Price SqlType(int8) */
    val price            : Rep[Long]               = column[Long]("Price")
    /** Database column EntryDate SqlType(timestamp) */
    val entrydate        : Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("EntryDate")
    /** Database column Edition SqlType(varchar), Default(Some(NO EDITION)) */
    val edition          : Rep[Option[String]]     = column[Option[String]]("Edition", O.Default(Some("NO EDITION")))
    /** Database column NumberOfCopies SqlType(int8) */
    val numberofcopies   : Rep[Long]               = column[Long]("NumberOfCopies")
    /** Database column Condition SqlType(varchar) */
    val condition        : Rep[String]             = column[String]("Condition")
    /** Database column Remarks SqlType(varchar), Default(Some(NO REMARKS)) */
    val remarks          : Rep[Option[String]]     = column[Option[String]]("Remarks", O.Default(Some("NO REMARKS")))
    /** Database column IsReferenceCopy SqlType(bool), Default(false) */
    val isreferencecopy  : Rep[Boolean]            = column[Boolean]("IsReferenceCopy", O.Default(false))
    /** Database column IsRemoved SqlType(bool), Default(false) */
    val isremoved        : Rep[Boolean]            = column[Boolean]("IsRemoved", O.Default(false))
  }

  /** Collection-like TableQuery object for table Book */
  lazy val Book = new TableQuery(tag => new Book(tag))

  /** Entity class storing rows of table Department
    *
    * @param departmentid   Database column DepartmentId SqlType(bigserial), AutoInc, PrimaryKey
    * @param departmentname Database column DepartmentName SqlType(varchar), Default(Some(None))
    * @param departmenthead Database column DepartmentHead SqlType(varchar), Default(Some(None))
    * @param isremoved      Database column IsRemoved SqlType(bool), Default(false) */
  final case class DepartmentRow(departmentid: Long, departmentname: Option[String] = Some("None"), departmenthead: Option[String] = Some("None"), isremoved: Boolean = false)

  /** GetResult implicit for fetching DepartmentRow objects using plain SQL queries */
  implicit def GetResultDepartmentRow(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Boolean]): GR[DepartmentRow] = GR {
    prs =>
      import prs._
      DepartmentRow.tupled((<<[Long], <<?[String], <<?[String], <<[Boolean]))
  }

  /** Table description of table Department. Objects of this class serve as prototypes for rows in queries. */
  class Department(_tableTag: Tag) extends profile.api.Table[DepartmentRow](_tableTag, Some("library"), "Department") {
    def * = (departmentid, departmentname, departmenthead, isremoved) <> (DepartmentRow.tupled, DepartmentRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(departmentid), departmentname, departmenthead, Rep.Some(isremoved)).shaped.<>({ r => import r._; _1.map(_ => DepartmentRow.tupled((_1.get, _2, _3, _4.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column DepartmentId SqlType(bigserial), AutoInc, PrimaryKey */
    val departmentid  : Rep[Long]           = column[Long]("DepartmentId", O.AutoInc, O.PrimaryKey)
    /** Database column DepartmentName SqlType(varchar), Default(Some(None)) */
    val departmentname: Rep[Option[String]] = column[Option[String]]("DepartmentName", O.Default(Some("None")))
    /** Database column DepartmentHead SqlType(varchar), Default(Some(None)) */
    val departmenthead: Rep[Option[String]] = column[Option[String]]("DepartmentHead", O.Default(Some("None")))
    /** Database column IsRemoved SqlType(bool), Default(false) */
    val isremoved     : Rep[Boolean]        = column[Boolean]("IsRemoved", O.Default(false))
  }

  /** Collection-like TableQuery object for table Department */
  lazy val Department = new TableQuery(tag => new Department(tag))

  /** Entity class storing rows of table Employee
    *
    * @param employeeid     Database column EmployeeId SqlType(bigserial), AutoInc, PrimaryKey
    * @param firstname      Database column FirstName SqlType(varchar)
    * @param lastname       Database column LastName SqlType(varchar), Default(Some(None))
    * @param dateofbirth    Database column DateOfBirth SqlType(timestamp)
    * @param gender         Database column Gender SqlType(varchar), Default(Some(None))
    * @param emailid        Database column EmailId SqlType(varchar)
    * @param mobilenumber   Database column MobileNumber SqlType(int8)
    * @param address        Database column Address SqlType(varchar)
    * @param dateofjoining  Database column DateOfJoining SqlType(timestamp)
    * @param employeetype   Database column EmployeeType SqlType(varchar), Default(Some(None))
    * @param employeestatus Database column EmployeeStatus SqlType(varchar)
    * @param isremoved      Database column IsRemoved SqlType(bool), Default(false) */
  final case class EmployeeRow(employeeid: Long, firstname: String, lastname: Option[String] = Some("None"), dateofbirth: java.sql.Timestamp, gender: Option[String] = Some("None"), emailid: String, mobilenumber: Long, address: String, dateofjoining: java.sql.Timestamp, employeetype: Option[String] = Some("None"), employeestatus: String, isremoved: Boolean = false)

  /** GetResult implicit for fetching EmployeeRow objects using plain SQL queries */
  implicit def GetResultEmployeeRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Timestamp], e4: GR[Boolean]): GR[EmployeeRow] = GR {
    prs =>
      import prs._
      EmployeeRow.tupled((<<[Long], <<[String], <<?[String], <<[java.sql.Timestamp], <<?[String], <<[String], <<[Long], <<[String], <<[java.sql.Timestamp], <<?[String], <<[String], <<[Boolean]))
  }

  /** Table description of table Employee. Objects of this class serve as prototypes for rows in queries. */
  class Employee(_tableTag: Tag) extends profile.api.Table[EmployeeRow](_tableTag, Some("library"), "Employee") {
    def * = (employeeid, firstname, lastname, dateofbirth, gender, emailid, mobilenumber, address, dateofjoining, employeetype, employeestatus, isremoved) <> (EmployeeRow.tupled, EmployeeRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(employeeid), Rep.Some(firstname), lastname, Rep.Some(dateofbirth), gender, Rep.Some(emailid), Rep.Some(mobilenumber), Rep.Some(address), Rep.Some(dateofjoining), employeetype, Rep.Some(employeestatus), Rep.Some(isremoved)).shaped.<>({ r => import r._; _1.map(_ => EmployeeRow.tupled((_1.get, _2.get, _3, _4.get, _5, _6.get, _7.get, _8.get, _9.get, _10, _11.get, _12.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column EmployeeId SqlType(bigserial), AutoInc, PrimaryKey */
    val employeeid    : Rep[Long]               = column[Long]("EmployeeId", O.AutoInc, O.PrimaryKey)
    /** Database column FirstName SqlType(varchar) */
    val firstname     : Rep[String]             = column[String]("FirstName")
    /** Database column LastName SqlType(varchar), Default(Some(None)) */
    val lastname      : Rep[Option[String]]     = column[Option[String]]("LastName", O.Default(Some("None")))
    /** Database column DateOfBirth SqlType(timestamp) */
    val dateofbirth   : Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("DateOfBirth")
    /** Database column Gender SqlType(varchar), Default(Some(None)) */
    val gender        : Rep[Option[String]]     = column[Option[String]]("Gender", O.Default(Some("None")))
    /** Database column EmailId SqlType(varchar) */
    val emailid       : Rep[String]             = column[String]("EmailId")
    /** Database column MobileNumber SqlType(int8) */
    val mobilenumber  : Rep[Long]               = column[Long]("MobileNumber")
    /** Database column Address SqlType(varchar) */
    val address       : Rep[String]             = column[String]("Address")
    /** Database column DateOfJoining SqlType(timestamp) */
    val dateofjoining : Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("DateOfJoining")
    /** Database column EmployeeType SqlType(varchar), Default(Some(None)) */
    val employeetype  : Rep[Option[String]]     = column[Option[String]]("EmployeeType", O.Default(Some("None")))
    /** Database column EmployeeStatus SqlType(varchar) */
    val employeestatus: Rep[String]             = column[String]("EmployeeStatus")
    /** Database column IsRemoved SqlType(bool), Default(false) */
    val isremoved     : Rep[Boolean]            = column[Boolean]("IsRemoved", O.Default(false))
  }

  /** Collection-like TableQuery object for table Employee */
  lazy val Employee = new TableQuery(tag => new Employee(tag))

  /** Entity class storing rows of table Employeeavailability
    *
    * @param employeeavailabilityid Database column EmployeeAvailabilityId SqlType(bigserial), AutoInc, PrimaryKey
    * @param employeeid             Database column EmployeeId SqlType(int8)
    * @param category               Database column Category SqlType(int8)
    * @param startdate              Database column StartDate SqlType(timestamp)
    * @param enddate                Database column EndDate SqlType(timestamp)
    * @param isremoved              Database column IsRemoved SqlType(bool), Default(false) */
  final case class EmployeeavailabilityRow(employeeavailabilityid: Long, employeeid: Long, category: Long, startdate: java.sql.Timestamp, enddate: java.sql.Timestamp, isremoved: Boolean = false)

  /** GetResult implicit for fetching EmployeeavailabilityRow objects using plain SQL queries */
  implicit def GetResultEmployeeavailabilityRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp], e2: GR[Boolean]): GR[EmployeeavailabilityRow] = GR {
    prs =>
      import prs._
      EmployeeavailabilityRow.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Boolean]))
  }

  /** Table description of table EmployeeAvailability. Objects of this class serve as prototypes for rows in queries. */
  class Employeeavailability(_tableTag: Tag) extends profile.api.Table[EmployeeavailabilityRow](_tableTag, Some("library"), "EmployeeAvailability") {
    def * = (employeeavailabilityid, employeeid, category, startdate, enddate, isremoved) <> (EmployeeavailabilityRow.tupled, EmployeeavailabilityRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(employeeavailabilityid), Rep.Some(employeeid), Rep.Some(category), Rep.Some(startdate), Rep.Some(enddate), Rep.Some(isremoved)).shaped.<>({ r => import r._; _1.map(_ => EmployeeavailabilityRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column EmployeeAvailabilityId SqlType(bigserial), AutoInc, PrimaryKey */
    val employeeavailabilityid: Rep[Long]               = column[Long]("EmployeeAvailabilityId", O.AutoInc, O.PrimaryKey)
    /** Database column EmployeeId SqlType(int8) */
    val employeeid            : Rep[Long]               = column[Long]("EmployeeId")
    /** Database column Category SqlType(int8) */
    val category              : Rep[Long]               = column[Long]("Category")
    /** Database column StartDate SqlType(timestamp) */
    val startdate             : Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("StartDate")
    /** Database column EndDate SqlType(timestamp) */
    val enddate               : Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("EndDate")
    /** Database column IsRemoved SqlType(bool), Default(false) */
    val isremoved             : Rep[Boolean]            = column[Boolean]("IsRemoved", O.Default(false))

    /** Foreign key referencing Employee (database name Fkey1) */
    lazy val employeeFk = foreignKey("Fkey1", employeeid, Employee)(r => r.employeeid, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  /** Collection-like TableQuery object for table Employeeavailability */
  lazy val Employeeavailability = new TableQuery(tag => new Employeeavailability(tag))

  /** Entity class storing rows of table Transaction
    *
    * @param transactionid Database column TransactionId SqlType(bigserial), AutoInc, PrimaryKey
    * @param bookid        Database column BookId SqlType(int8)
    * @param issuedto      Database column IssuedTo SqlType(int8)
    * @param issuedate     Database column IssueDate SqlType(timestamp)
    * @param receiveddate  Database column ReceivedDate SqlType(timestamp)
    * @param fine          Database column Fine SqlType(int8), Default(Some(0))
    * @param remarks       Database column Remarks SqlType(varchar), Default(Some(NO REMARKS))
    * @param isremoved     Database column IsRemoved SqlType(bool), Default(false) */
  final case class TransactionRow(transactionid: Long, bookid: Long, issuedto: Long, issuedate: java.sql.Timestamp, receiveddate: java.sql.Timestamp, fine: Option[Long] = Some(0L), remarks: Option[String] = Some("NO REMARKS"), isremoved: Boolean = false)

  /** GetResult implicit for fetching TransactionRow objects using plain SQL queries */
  implicit def GetResultTransactionRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp], e2: GR[Option[Long]], e3: GR[Option[String]], e4: GR[Boolean]): GR[TransactionRow] = GR {
    prs =>
      import prs._
      TransactionRow.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<?[Long], <<?[String], <<[Boolean]))
  }

  /** Table description of table Transaction. Objects of this class serve as prototypes for rows in queries. */
  class Transaction(_tableTag: Tag) extends profile.api.Table[TransactionRow](_tableTag, Some("library"), "Transaction") {
    def * = (transactionid, bookid, issuedto, issuedate, receiveddate, fine, remarks, isremoved) <> (TransactionRow.tupled, TransactionRow.unapply)

    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(transactionid), Rep.Some(bookid), Rep.Some(issuedto), Rep.Some(issuedate), Rep.Some(receiveddate), fine, remarks, Rep.Some(isremoved)).shaped.<>({ r => import r._; _1.map(_ => TransactionRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6, _7, _8.get))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column TransactionId SqlType(bigserial), AutoInc, PrimaryKey */
    val transactionid: Rep[Long]               = column[Long]("TransactionId", O.AutoInc, O.PrimaryKey)
    /** Database column BookId SqlType(int8) */
    val bookid       : Rep[Long]               = column[Long]("BookId")
    /** Database column IssuedTo SqlType(int8) */
    val issuedto     : Rep[Long]               = column[Long]("IssuedTo")
    /** Database column IssueDate SqlType(timestamp) */
    val issuedate    : Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("IssueDate")
    /** Database column ReceivedDate SqlType(timestamp) */
    val receiveddate : Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("ReceivedDate")
    /** Database column Fine SqlType(int8), Default(Some(0)) */
    val fine         : Rep[Option[Long]]       = column[Option[Long]]("Fine", O.Default(Some(0L)))
    /** Database column Remarks SqlType(varchar), Default(Some(NO REMARKS)) */
    val remarks      : Rep[Option[String]]     = column[Option[String]]("Remarks", O.Default(Some("NO REMARKS")))
    /** Database column IsRemoved SqlType(bool), Default(false) */
    val isremoved    : Rep[Boolean]            = column[Boolean]("IsRemoved", O.Default(false))

    /** Foreign key referencing Book (database name Fkey1) */
    lazy val bookFk     = foreignKey("Fkey1", bookid, Book)(r => r.bookid, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
    /** Foreign key referencing Employee (database name Fkey2) */
    lazy val employeeFk = foreignKey("Fkey2", issuedto, Employee)(r => r.employeeid, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)
  }

  /** Collection-like TableQuery object for table Transaction */
  lazy val Transaction = new TableQuery(tag => new Transaction(tag))
}
