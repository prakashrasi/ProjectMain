package com.reactore

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import org.json4s.native.Serialization._
import org.json4s.{DefaultFormats, Formats}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

case class EnumValues(id: Long, name: String)

/** Gender Enum Details **/
abstract class GenderEnum(val id: Long, val name: String)

case object Male extends GenderEnum(1, "Male")

case object Female extends GenderEnum(2, "Female")

object GenderEnum {
  def getAllGenders: List[EnumValues] = {
    val enums = List(Male, Female)
    enums.map { gender => EnumValues(gender.id, gender.name) }
  }
}

/** Marital Status Enum Details **/
abstract class MaritalStatusEnum(val id: Long, val name: String)

case object Married extends MaritalStatusEnum(1, "Married Person")

case object UnMarried extends MaritalStatusEnum(2, "UnMarried Person")

object MaritalStatus {
  def getAllMaritalStatus: List[EnumValues] = {
    val enums = List(Married, UnMarried)
    enums.map {
      marital => EnumValues(marital.id, marital.name)
    }
  }
}

/** Employment Status Enum Details **/
abstract class EmploymentTypeStatusEnum(val id: Long, val name: String)

case object Permanent extends EmploymentTypeStatusEnum(1, "Permanent")

case object Temporary extends EmploymentTypeStatusEnum(2, "Temporary")

case object Contract extends EmploymentTypeStatusEnum(3, "Contract")

object EmploymentStatusTypes {
  def getAllEmploymentTypes: List[EnumValues] = {
    val enums = List(Permanent, Temporary, Contract)
    enums.map {
      employment => EnumValues(employment.id, employment.name)
    }
  }
}


/** Blood Group Enum Details **/
abstract class BloodGroupEnum(val id: Long, val name: String)

case object GroupO extends BloodGroupEnum(1, "GroupO")

case object GroupA extends BloodGroupEnum(2, "GroupA")

case object GroupB extends BloodGroupEnum(3, "GroupB")

case object GroupAB extends BloodGroupEnum(4, "GroupAB")

object BloodGroupDetails {
  def getAllBloodGroup: List[EnumValues] = {
    val enums = List(GroupO, GroupA, GroupB, GroupAB)
    enums.map {
      bloodGroup => EnumValues(bloodGroup.id, bloodGroup.name)
    }
  }
}


class EnumRoute extends Directives {

  implicit val formats: Formats = new DefaultFormats {}

  val enumRoute: Route = pathPrefix("enum") {
    path("readGenderEnum") {
      get {
        complete(write(GenderEnum.getAllGenders))
      }
    } ~ path("readMaritalStatusEnum") {
      get {
        complete(write(MaritalStatus.getAllMaritalStatus))
      }
    } ~ path("readEmploymentStatusEnum") {
      get {
        complete(write(EmploymentStatusTypes.getAllEmploymentTypes))
      }
    } ~ path("readBloodGroupEnum") {
      get {
        complete(write(BloodGroupDetails.getAllBloodGroup))
      }
    }
  }
}

object ApiEnumBoot extends Directives with App {

  implicit val system      : ActorSystem       = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val enumRouteInstance: EnumRoute = new EnumRoute

  val finalRouteWithContextRoot: Route = pathPrefix("myEnumApp") {
    enumRouteInstance.enumRoute
  }


  val binding: Future[ServerBinding] = Http().bindAndHandle(finalRouteWithContextRoot, "localhost", 8081)

  binding.onComplete({
    case Success(res) => println("Successfully Bound For Enum Api  Boot ====>>>>> " + res)
    case Failure(ex)  => println("Failed to Bind For Api  Boot "); ex.printStackTrace()
  })
}