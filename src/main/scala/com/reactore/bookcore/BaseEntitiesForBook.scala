package com.reactore.bookcore

import org.joda.time.DateTime

case class Book(override val id: Long, bookTitle: String, publication: Option[String] = Some("NO DETAILS FOR PUBLICATION"), authorName: String, categoryOrSubject: Option[String] = Some("NO DETAILS FOR Category"), price: Long, entryDate: DateTime, edition: Option[String] = Some("NO EDITION"), numberOfCopies: Long, condition: String, remarks: Option[String] = Some("NO REMARKS"), isReferenceCopy: Boolean = false, isRemoved: Boolean = false) extends Persistable[Long]

case class Department(override val id: Long, departmentName: Option[String] = Some("None"), departmentHead: Option[String] = Some("None"), isRemoved: Boolean = false) extends Persistable[Long]

case class Employee(override val id: Long, firstName: String, lastName: Option[String] = Some("None"), dateOfBirth: DateTime, gender: Option[String] = Some("None"), emailId: String, mobileNumber: Long, address: String, dateOfJoining: DateTime, employeeType: Option[String] = Some("None"), employeeStatus: String, isRemoved: Boolean = false) extends Persistable[Long]

case class Transaction(override val id: Long, bookId: Long, issuedTo: Long, issueDate: DateTime, receivedDate: DateTime, fine: Option[Long] = Some(0L), remarks: Option[String] = Some("NO REMARKS"), isRemoved: Boolean = false) extends Persistable[Long]

case class DateTimeOnly(jodaDateTime: DateTime)

case class EmployeeAvailability(override val id: Long, employeeId: Long,
                                category: Long, startDate: DateTime, endDate: DateTime, isRemoved: Boolean = false) extends Persistable[Long]

case class DateTimeRange(start: DateTime, end: DateTime)

case class InsertUpdateContainer(updateList: List[Int], insertList: List[EmployeeAvailability])