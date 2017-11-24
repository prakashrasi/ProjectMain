package com.reactore.bookfeatures

import akka.http.scaladsl.server.Route
import com.reactore.bookcore._
import org.json4s.native.Serialization._

class EmployeeAvailabilityRoute(employeeAvailabilityRepositry: EmployeeAvailabilityRepositry) extends DirectiveWithGenericErrorHandling {
  val employeeAvailabilityRoute: Route = pathPrefix("employeeAvailability") {
    path("readEmployeeAvailabilityData" / LongNumber) { employeeAvailabilityId =>
      get {
        complete(respond(employeeAvailabilityRepositry.findById(employeeAvailabilityId)))
      }
    } ~ path("deleteEmployeeAvailabilityData" / LongNumber) { id =>
      delete {
        complete(respond(employeeAvailabilityRepositry.deleteById(id)))
      }
    } ~ path("createEmployeeAvailabilityData") {
      post {
        entity(as[String]) { postBody =>
          val employeeAvailabilityEntity: EmployeeAvailability = read[EmployeeAvailability](postBody)
          complete(respond(employeeAvailabilityRepositry.create(employeeAvailabilityEntity)))
        }
      }
    } ~ path("updateEmployeeAvailabilityData" / LongNumber) { updatePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val employeeAvailabilityEntity: EmployeeAvailability = read[EmployeeAvailability](postBody)
          complete(respond(employeeAvailabilityRepositry.updateById(updatePrimaryId, employeeAvailabilityEntity)))
        }
      }
    } ~ path("getAllEmployeeAvailabilityData") {
      get {
        complete(respond(employeeAvailabilityRepositry.getAll))
      }
    } ~ path("getEmployeeAvailabilityByEmployeeId" / LongNumber) { employeeId =>
      get {
        complete(respond(employeeAvailabilityRepositry.getEmployeeAvailabilityByEmployeeId(employeeId)))
      }
    } ~ path("insertEmployeeAvailabilityEntity") {
      post {
        entity(as[String]) { postBody =>
          val entity: EmployeeAvailability = read[EmployeeAvailability](postBody)
          complete(respond(employeeAvailabilityRepositry.insertEmployeeAvailabilityEntity(entity)))
        }
      }
    } ~ path("filterEmployeeAvailabilityDates" / LongNumber) { employeeId =>
      get {
        complete(respond(employeeAvailabilityRepositry.filterEmployeeAvailabilityDates(employeeId)))
      }
    } ~ path("insertOrUpdateEmployeeAvailability") {
      post {
        entity(as[String]) { postBody =>
          val listOfInsertOrUpdate: List[EmployeeAvailability] = read[List[EmployeeAvailability]](postBody)
          complete(respond(employeeAvailabilityRepositry.insertOrUpdateEmployeeAvailability(listOfInsertOrUpdate)))
        }
      }
    }
  }

  //////////////////
}