package com.reactore.bookfeatures

import akka.http.scaladsl.server.Route
import com.reactore.bookcore._
import org.json4s.native.Serialization._


class EmployeeRoute(employeeRepositry: EmployeeRepositry) extends DirectiveWithGenericErrorHandling {
  val employeeRoute: Route = pathPrefix("employee") {
    path("readEmployeeData" / LongNumber) { id =>
      get {
        complete(respond(employeeRepositry.findById(id)))
      }
    } ~ path("deleteEmployeeData" / LongNumber) { id =>
      delete {
        complete(respond(employeeRepositry.deleteById(id)))
      }
    } ~ path("createEmployeeData") {
      post {
        entity(as[String]) { postBody =>
          val entity: Employee = read[Employee](postBody)
          complete(respond(employeeRepositry.create(entity)))
        }
      }
    } ~ path("updateEmployeeData" / LongNumber) { updatePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val entity: Employee = read[Employee](postBody)
          complete(respond(employeeRepositry.updateById(updatePrimaryId, entity)))
        }
      }
    } ~ path("getAllEmployeeData") {
      get {
        complete(respond(employeeRepositry.getAll))
      }
    } ~ path("getEmployeesComparedWithDateOfJoiningAndPermanent") {
      post {
        entity(as[String]) { postBody =>
          val timeStampFormat: DateTimeOnly = read[DateTimeOnly](postBody)
          complete(respond(employeeRepositry.getEmployeesComparedWithDateOfJoiningAndPermanent(timeStampFormat.jodaDateTime)))
        }
      }
    }
  }
}