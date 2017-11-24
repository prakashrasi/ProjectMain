package com.reactore.bookfeatures.testcase.service

import com.reactore.bookcore._
import com.reactore.bookfeatures.EmployeeAvailabilityRepositry
import com.reactore.bookfeatures.testcase.MockDataForGlobalTestFeatures
import org.joda.time.DateTime
import scala.concurrent.ExecutionContext.Implicits.global
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class EmployeeAvailabilityServiceTest extends WordSpec with Matchers with MockitoSugar with ScalaFutures {

  val mockRepo: EmployeeAvailabilityRepositry = mock[EmployeeAvailabilityRepositry]

  "Process The EmployeeAvailability  Test Case Logic" should {

    "findAll" in {
      when(mockRepo.findAll).thenReturn(Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability))
      val actualResult: Future[Seq[EmployeeAvailability]] = mockRepo.findAll
      actualResult.futureValue shouldBe MockDataForGlobalTestFeatures.seqOfEmployeeAvailability
    }

    "getEmployeeAvailabilityByEmployeeId If Valid Input Is Passed " in {
      when(mockRepo.getEmployeeAvailabilityByEmployeeId(any[Long])).thenReturn(Future.successful(Seq(MockDataForGlobalTestFeatures.availability1)))
      val actualResult: Future[Seq[EmployeeAvailability]] = mockRepo.getEmployeeAvailabilityByEmployeeId(1L)
      actualResult.futureValue shouldBe Seq(MockDataForGlobalTestFeatures.availability1)
    }

    "getEmployeeAvailabilityByEmployeeId If Empty List  Is Passed " in {
      val actualResult: Future[Seq[EmployeeAvailability]] = MockEmployeeAvailabilityRepositry2.getEmployeeAvailabilityByEmployeeId(1L)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    "getEmployeeAvailabilityByEmployeeId Handle Exception " in {
      val actualResult: Future[Seq[EmployeeAvailability]] = MockEmployeeAvailabilityRepositry1.getEmployeeAvailabilityByEmployeeId(111L)
      actualResult.failed.futureValue shouldBe an[NoSuchEntityException]
    }

    "insertEmployeeAvailabilityEntity" in {
      val actualResult: Future[EmployeeAvailability] = MockEmployeeAvailabilityRepositry1.insertEmployeeAvailabilityEntity(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntity)
      actualResult.futureValue shouldBe MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntity
    }


    "insertEmployeeAvailabilityEntity For Insert The First Record If Empty List Is Passed" in {
      val actualResult: Future[EmployeeAvailability] = MockEmployeeAvailabilityRepositry2.insertEmployeeAvailabilityEntity(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntity)
      actualResult.futureValue shouldBe MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntity
    }

    "insertEmployeeAvailabilityEntity Handle Exception For Duplicate Exception" in {
      val actualResult: Future[EmployeeAvailability] = MockEmployeeAvailabilityRepositry3.insertEmployeeAvailabilityEntity(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityForDuplicate)
      actualResult.failed.futureValue shouldBe an[DuplicateEntityException]
    }

    "insertEmployeeAvailabilityEntity Valid For Not Matching Of Any  Employee Id" in {
      val actualResult: Future[EmployeeAvailability] = MockEmployeeAvailabilityRepositry4.insertEmployeeAvailabilityEntity(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityForNotMatchingOfEmployeeId)
      actualResult.futureValue shouldBe MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityForNotMatchingOfEmployeeId
    }

    "mergeOverLap " in {
      val listOfRange: List[DateTimeRange] = List(DateTimeRange(DateTime.parse("2014-02-18T02:55:40"), DateTime.parse("2014-02-27T02:55:40")))
      val actualResult: List[DateTimeRange] = MockEmployeeAvailabilityRepositry5.mergeWithOverlap(listOfRange, Nil)
      actualResult shouldBe listOfRange
    }

    "mergeOverLap With More Number Of Dates" in {
      val listOfRange: List[DateTimeRange] = List(DateTimeRange(DateTime.parse("2014-02-18T02:55:40"), DateTime.parse("2014-02-27T02:55:40")), DateTimeRange(DateTime.parse("2014-02-24T02:55:40"), DateTime.parse("2014-02-28T02:55:40")))
      val actualResult: List[DateTimeRange] = MockEmployeeAvailabilityRepositry5.mergeWithOverlap(listOfRange, Nil)
      actualResult shouldBe List(DateTimeRange(DateTime.parse("2014-02-18T02:55:40"), DateTime.parse("2014-02-28T02:55:40")))
    }

    "getMaxTime " in {
      val actualResult: DateTime = MockEmployeeAvailabilityRepositry5.getMaxTime(DateTime.parse("2017-04-22T00:00:00"), DateTime.parse("2017-04-26T00:00:00"))
      actualResult shouldBe DateTime.parse("2017-04-26T00:00:00")
    }

    "checkEmployeeAvailabilityOverlapping " in {
      val newEmployeeAvailability = EmployeeAvailability(5, 3, 2, DateTime.parse("2017-06-22T02:55:40"), DateTime.parse("2017-06-26T02:55:40"))
      val dataBaseAvailability = EmployeeAvailability(4, 3, 2, DateTime.parse("2016-06-22T02:55:40"), DateTime.parse("2016-06-26T02:55:40"))
      val actualResult: Boolean = MockEmployeeAvailabilityRepositry5.checkEmployeeAvailabilityOverlapping(newEmployeeAvailability, dataBaseAvailability)
      actualResult shouldBe true
    }

    "checkEmployeeAvailabilityOverlapping Handle Exception For End Date  Is Overlapping " in {
      val actualResult: Future[EmployeeAvailability] = MockEmployeeAvailabilityRepositry6.insertEmployeeAvailabilityEntity(MockDataForGlobalTestFeatures.newEmployeeAvailabilityForCheckingOverlapping)
      actualResult.failed.futureValue shouldBe an[EmployeeAvailabilityOverlappingException]
    }

    "checkEmployeeAvailabilityOverlapping Handle Exception For Both Dates Are Overlapping " in {
      val actualResult: Future[EmployeeAvailability] = MockEmployeeAvailabilityRepositry7.insertEmployeeAvailabilityEntity(MockDataForGlobalTestFeatures.newEmployeeAvailabilityForCheckingOverlapping1)
      actualResult.failed.futureValue shouldBe an[DuplicateEntityException]
    }

    "checkEmployeeAvailabilityOverlapping Handle Exception For Start Date Is Overlapping " in {
      val actualResult: Future[EmployeeAvailability] = MockEmployeeAvailabilityRepositry8.insertEmployeeAvailabilityEntity(MockDataForGlobalTestFeatures.newEmployeeAvailabilityForCheckingOverlapping2)
      actualResult.failed.futureValue shouldBe an[EmployeeAvailabilityOverlappingException]
    }

    "filterEmployeeAvailabilityDates  For Merge Overlap " in {
      val actualResult: Future[List[DateTimeRange]] = MockEmployeeAvailabilityRepositry9.filterEmployeeAvailabilityDates(3)
      actualResult.futureValue shouldBe List(DateTimeRange(DateTime.parse("2016-06-22T02:55:40"), DateTime.parse("2016-06-26T02:55:40")), DateTimeRange(DateTime.parse("2016-10-22T02:55:40"), DateTime.parse("2016-10-26T02:55:40")))
    }


    "filterEmployeeAvailabilityDates " in {
      val actualResult = MockEmployeeAvailabilityRepositry9.filterEmployeeAvailabilityDates(1111)
      actualResult.failed.futureValue shouldBe an[NoSuchEntityException]
    }

    "filterEmployeeAvailabilityDates For Empty List" in {
      val actualResult = MockEmployeeAvailabilityRepositry10.filterEmployeeAvailabilityDates(1111)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }

    /** insertOrUpdateEmployeeAvailability Repositry Test Case **/

    "[insertOrUpdateEmployeeAvailability] The Date Is Not Overlapping " in {
      val actualResult: Future[InsertUpdateContainer] = MockEmployeeAvailabilityRepositry11.insertOrUpdateEmployeeAvailability(MockDataForGlobalTestFeatures.listOfAvailability)
      actualResult.map(_.updateList).futureValue shouldBe List(1)
      actualResult.map(_.insertList).futureValue shouldBe List(EmployeeAvailability(0, 2, 2, DateTime.parse("2016-05-22T02:55:40"), DateTime.parse("2016-05-26T02:55:40"), false))
    }

    "[insertOrUpdateEmployeeAvailability] Valid Input For  Update The Record For The First EmployeeId In The Availability Table  " in {
      val actualResult: Future[InsertUpdateContainer] = MockEmployeeAvailabilityRepositry12.insertOrUpdateEmployeeAvailability(MockDataForGlobalTestFeatures.listOfAvailability)
      actualResult.map(_.updateList).futureValue shouldBe List(1)
      actualResult.map(_.insertList).futureValue shouldBe List(EmployeeAvailability(0, 2, 2, DateTime.parse("2016-05-22T02:55:40"), DateTime.parse("2016-05-26T02:55:40"), false))
    }


    "[insertOrUpdateEmployeeAvailability] Handle Exception For Empty List Passed " in {
      val actualResult: Future[InsertUpdateContainer] = MockEmployeeAvailabilityRepositry13.insertOrUpdateEmployeeAvailability(MockDataForGlobalTestFeatures.listOfAvailability)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }


    "[insertOrUpdateEmployeeAvailability] Duplicate Exception Occur For The Insert EmployeeAvailability " in {
      val actualResult: Future[InsertUpdateContainer] = MockEmployeeAvailabilityRepositry14.insertOrUpdateEmployeeAvailability(MockDataForGlobalTestFeatures.listOfAvailability1)
      actualResult.failed.futureValue shouldBe an[DuplicateEntityException]
    }


    "[insertOrUpdateEmployeeAvailability] Valid For Insert The  New Employee Id=7 In The Availability Table " in {
      val actualResult: Future[InsertUpdateContainer] = MockEmployeeAvailabilityRepositry15.insertOrUpdateEmployeeAvailability(MockDataForGlobalTestFeatures.listOfAvailability2)
      actualResult.map(_.updateList).futureValue shouldBe List(1)
      actualResult.map(_.insertList).futureValue shouldBe List(EmployeeAvailability(0, 7, 2, DateTime.parse("2017-07-07T02:55:40"), DateTime.parse("2017-07-08T02:55:40"), false))
    }

    "[insertOrUpdateEmployeeAvailability] Valid For Insert First EmployeeAvailability If Empty List And Also Throw Exception For EmptyList For Update Availability " in {
      val actualResult: Future[InsertUpdateContainer] = MockEmployeeAvailabilityRepositry16.insertOrUpdateEmployeeAvailability(MockDataForGlobalTestFeatures.listOfAvailability3)
      actualResult.failed.futureValue shouldBe an[EmptyListException]
    }


    ////////////////////////////
  } //endOfProcess
}

object MockEmployeeAvailabilityRepositry1 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntity)
}

object MockEmployeeAvailabilityRepositry2 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(Nil)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntity)
}


object MockEmployeeAvailabilityRepositry3 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityForDuplicate)
}

object MockEmployeeAvailabilityRepositry4 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityForNotMatchingOfEmployeeId)
}

object MockEmployeeAvailabilityRepositry5 extends EmployeeAvailabilityRepositry with MockitoSugar


object MockEmployeeAvailabilityRepositry6 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.newEmployeeAvailabilityForCheckingOverlapping)
}

object MockEmployeeAvailabilityRepositry7 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.newEmployeeAvailabilityForCheckingOverlapping1)
}

object MockEmployeeAvailabilityRepositry8 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.newEmployeeAvailabilityForCheckingOverlapping2)
}

object MockEmployeeAvailabilityRepositry9 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability)
}

object MockEmployeeAvailabilityRepositry10 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(Nil)
}

object MockEmployeeAvailabilityRepositry11 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability)

  override def updateById(id: Long, entity: EmployeeAvailability): Future[Int] = Future.successful(1)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityWithIdZero)
}

object MockEmployeeAvailabilityRepositry12 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailabilityOneToFour)

  override def updateById(id: Long, entity: EmployeeAvailability): Future[Int] = Future.successful(1)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityWithIdZero)
}


object MockEmployeeAvailabilityRepositry13 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(Nil)

  override def updateById(id: Long, entity: EmployeeAvailability): Future[Int] = Future.successful(1)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityWithIdZero)
}

object MockEmployeeAvailabilityRepositry14 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability)

  override def updateById(id: Long, entity: EmployeeAvailability): Future[Int] = Future.successful(1)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityWithIdZero1)
}

object MockEmployeeAvailabilityRepositry15 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(MockDataForGlobalTestFeatures.seqOfEmployeeAvailability)

  override def updateById(id: Long, entity: EmployeeAvailability): Future[Int] = Future.successful(1)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityWithIdZero2)
}

object MockEmployeeAvailabilityRepositry16 extends EmployeeAvailabilityRepositry with MockitoSugar {
  override def findAll: Future[Seq[EmployeeAvailability]] = Future.successful(Nil)

  override def updateById(id: Long, entity: EmployeeAvailability): Future[Int] = Future.successful(1)

  override def create(entity: EmployeeAvailability): Future[EmployeeAvailability] = Future.successful(MockDataForGlobalTestFeatures.inserEmployeeAvailabilityEntityWithIdZero3)
}