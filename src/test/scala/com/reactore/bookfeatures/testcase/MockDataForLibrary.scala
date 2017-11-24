package com.reactore.bookfeatures.testcase

import com.reactore.bookcore._
import org.joda.time.DateTime

object MockDataForGlobalTestFeatures {

  val book1: Book = Book(1, "DSP", None, "Danraj", None, 600, DateTime.parse("2016-11-10T02:55:40"), None, 6, "Good", None)
  val book2: Book = Book(2, "Signals Systems", None, "Sanraj", None, 800, DateTime.parse("2016-12-10T02:55:40"), None, 8, "Good", None)
  val book3: Book = Book(3, "M2", None, "Kumar", None, 1000, DateTime.parse("2016-12-14T03:55:40.11"), None, 10, "Fair", None)
  val book4: Book = Book(4, "M3", None, "Vignesh", None, 1200, DateTime.parse("2016-10-10T02:55:40"), None, 12, "Good", None)
  val seqOfBook   = Seq(book1, book2, book3, book4)

  val employee1: Employee = Employee(1L, "Alex", None, DateTime.parse("1990-02-25T02:55:40"), None, "alex123@gmail.com", 7894563215L, "Chennai", DateTime.parse("2014-11-10T02:55:40"), None, "Permanent")
  val employee2: Employee = Employee(2L, "Brick", None, DateTime.parse("1991-02-25T02:55:40"), None, "brick123@gmail.com", 7898596215L, "Bangalore", DateTime.parse("2015-11-10T02:55:40"), None, "Contract")
  val employee3: Employee = Employee(3L, "Canor", None, DateTime.parse("1992-02-25T02:55:40"), None, "canor123@gmail.com", 7894512855L, "Delhi", DateTime.parse("2016-11-10T02:55:40"), None, "Contract")
  val employee4: Employee = Employee(4L, "Dummy", None, DateTime.parse("1993-02-25T02:55:40"), None, "dummy23@gmail.com", 7874263215L, "West Bengal", DateTime.parse("2017-10-10T02:55:40"), None, "Permanent")
  val employee5: Employee = Employee(5L, "Eiummy", None, DateTime.parse("1994-02-25T02:55:40"), None, "eiummy23@gmail.com", 7744263215L, "Andhra", DateTime.parse("2017-11-11T02:55:40"), None, "Permanent")
  val seqOfEmployee       = Seq(employee1, employee2, employee3, employee4, employee5)

  val department1: Department = Department(1L, None, None)
  val department2: Department = Department(2L, None, None)
  val department3: Department = Department(3L, None, None)
  val department4: Department = Department(4L, None, None)
  val department5: Department = Department(5L, None, None)
  val department6: Department = Department(6L, None, Some("CIVIL HOD"))
  val seqOfDepartment         = Seq(department1, department2, department3, department4, department5, department6)

  val transaction1     = Transaction(1L, 1L, 2L, DateTime.parse("2016-11-24T02:55:40"), DateTime.parse("2016-11-25T02:55:40"), Some(55), None)
  val transaction2     = Transaction(2L, 2L, 3L, DateTime.parse("2016-12-24T02:55:40"), DateTime.parse("2016-12-26T02:55:40"), Some(65), None)
  val transaction3     = Transaction(3L, 3L, 4L, DateTime.parse("2016-12-24T02:55:40"), DateTime.parse("2016-12-25T02:55:40"), Some(75), None)
  val transaction4     = Transaction(4L, 4L, 1L, DateTime.parse("2016-10-25T02:55:40"), DateTime.parse("2016-10-26T02:55:40"), Some(85), None)
  val transaction5     = Transaction(5L, 4L, 1L, DateTime.parse("2017-10-10T00:00:00"), DateTime.parse("2017-10-11T02:55:40"), Some(0), None)
  val seqOfTransaction = Seq(transaction1, transaction2, transaction3, transaction4, transaction5)

  val availability1                      = EmployeeAvailability(1, 1, 2, DateTime.parse("2014-02-18T02:55:40"), DateTime.parse("2014-02-24T02:55:40"))
  val availability2                      = EmployeeAvailability(2, 1, 2, DateTime.parse("2014-02-22T02:55:40"), DateTime.parse("2014-02-27T02:55:40"))
  val availability3                      = EmployeeAvailability(3, 2, 2, DateTime.parse("2015-04-18T02:55:40"), DateTime.parse("2015-04-28T02:55:40"))
  val availability4                      = EmployeeAvailability(4, 3, 2, DateTime.parse("2016-06-22T02:55:40"), DateTime.parse("2016-06-26T02:55:40"))
  val availability5                      = EmployeeAvailability(5, 3, 2, DateTime.parse("2016-10-22T02:55:40"), DateTime.parse("2016-10-26T02:55:40"))
  val seqOfEmployeeAvailability          = Seq(availability1, availability2, availability3, availability4, availability5)
  val seqOfEmployeeAvailabilityOneToFour = Seq(availability1, availability2, availability3, availability4)

  val inserEmployeeAvailabilityEntity                          : EmployeeAvailability = EmployeeAvailability(5, 3, 2, DateTime.parse("2017-08-22T02:55:40"), DateTime.parse("2017-08-26T02:55:40"))
  val inserEmployeeAvailabilityEntityForDuplicate              : EmployeeAvailability = EmployeeAvailability(5, 3, 2, DateTime.parse("2016-06-23T02:55:40"), DateTime.parse("2016-06-25T02:55:40"))
  val inserEmployeeAvailabilityEntityForNoSuchEntity           : EmployeeAvailability = EmployeeAvailability(5, 3333, 2, DateTime.parse("2016-06-23T02:55:40"), DateTime.parse("2016-06-25T02:55:40"))
  val inserEmployeeAvailabilityEntityForNotMatchingOfEmployeeId: EmployeeAvailability = EmployeeAvailability(5, 5, 2, DateTime.parse("2016-06-23T02:55:40"), DateTime.parse("2016-06-25T02:55:40"))
  val newEmployeeAvailabilityForCheckingOverlapping                                   = EmployeeAvailability(5, 3, 2, DateTime.parse("2016-06-20T02:55:40"), DateTime.parse("2016-06-24T02:55:40"))
  val newEmployeeAvailabilityForCheckingOverlapping1                                  = EmployeeAvailability(5, 3, 2, DateTime.parse("2016-06-24T02:55:40"), DateTime.parse("2016-06-25T02:55:40"))
  val newEmployeeAvailabilityForCheckingOverlapping2                                  = EmployeeAvailability(5, 3, 2, DateTime.parse("2016-06-23T02:55:40"), DateTime.parse("2016-06-28T02:55:40"))

  val listOfAvailability                       : List[EmployeeAvailability] = List(EmployeeAvailability(4, 3, 222, DateTime.parse("2016-07-22T02:55:40"), DateTime.parse("2016-07-26T02:55:40")), EmployeeAvailability(0, 2, 2, DateTime.parse("2016-05-22T02:55:40"), DateTime.parse("2016-05-26T02:55:40")))
  val inserEmployeeAvailabilityEntityWithIdZero: EmployeeAvailability       = EmployeeAvailability(0, 2, 2, DateTime.parse("2016-05-22T02:55:40"), DateTime.parse("2016-05-26T02:55:40"))

  val listOfAvailability1                       : List[EmployeeAvailability] = List(EmployeeAvailability(4, 3, 222, DateTime.parse("2016-10-23T02:55:40"), DateTime.parse("2016-10-24T02:55:40")), EmployeeAvailability(0, 2, 2, DateTime.parse("2015-04-22T02:55:40"), DateTime.parse("2015-04-23T02:55:40")))
  val inserEmployeeAvailabilityEntityWithIdZero1: EmployeeAvailability       = EmployeeAvailability(0, 2, 2, DateTime.parse("2015-04-22T02:55:40"), DateTime.parse("2015-04-23T02:55:40"))

  val listOfAvailability2                       : List[EmployeeAvailability] = List(EmployeeAvailability(4, 3, 222, DateTime.parse("2016-09-22T02:55:40"), DateTime.parse("2016-09-26T02:55:40")), EmployeeAvailability(0, 2, 2, DateTime.parse("2017-07-07T02:55:40"), DateTime.parse("2017-07-08T02:55:40")))
  val inserEmployeeAvailabilityEntityWithIdZero2: EmployeeAvailability       = EmployeeAvailability(0, 7, 2, DateTime.parse("2017-07-07T02:55:40"), DateTime.parse("2017-07-08T02:55:40"))

  val listOfAvailability3                       : List[EmployeeAvailability] = List(EmployeeAvailability(4, 3, 222, DateTime.parse("2016-09-22T02:55:40"), DateTime.parse("2016-09-26T02:55:40")), EmployeeAvailability(0, 2, 2, DateTime.parse("2017-07-07T02:55:40"), DateTime.parse("2017-07-08T02:55:40")))
  val inserEmployeeAvailabilityEntityWithIdZero3: EmployeeAvailability       = EmployeeAvailability(0, 1, 2, DateTime.parse("2017-07-07T02:55:40"), DateTime.parse("2017-07-08T02:55:40"))
}