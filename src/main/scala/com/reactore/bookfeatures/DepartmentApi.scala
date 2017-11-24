package com.reactore.bookfeatures

import akka.http.scaladsl.server.Route
import com.reactore.bookcore._
import org.json4s.native.Serialization._



class DepartmentRoute(departmentRepositry: DepartmentRepositry) extends DirectiveWithGenericErrorHandling {
  val departmentRoute: Route = pathPrefix("department") {
    path("readDepartmentData" / LongNumber) { id =>
      get {
        complete(respond(departmentRepositry.findById(id)))
      }
    } ~ path("deleteDepartmentData" / LongNumber) { id =>
      delete {
        complete(respond(departmentRepositry.deleteById(id)))
      }
    } ~ path("createDepartmentData") {
      post {
        entity(as[String]) { postBody =>
          val entity: Department = read[Department](postBody)
          complete(respond(departmentRepositry.create(entity)))
        }
      }
    } ~ path("updateDepartmentData" / LongNumber) { updatePrimaryId =>
      put {
        entity(as[String]) { postBody =>
          val entity: Department = read[Department](postBody)
          complete(respond(departmentRepositry.updateById(updatePrimaryId, entity)))
        }
      }
    } ~ path("getAllDepartmentData") {
      get {
        complete(respond(departmentRepositry.getAll))
      }
    } ~ path("getAllDepartments") {
      get {
        complete(respond(departmentRepositry.getAllDepartments))
      }
    }
  }
}