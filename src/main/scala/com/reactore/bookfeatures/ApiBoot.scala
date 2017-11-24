package com.reactore.bookfeatures

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ApiBookBoot extends Directives with App {

  implicit val system      : ActorSystem       = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val bookInstance                : BookRoute                 = new BookRoute(ImplBookRoute)
  val employeeInstance            : EmployeeRoute             = new EmployeeRoute(ImplEmployeeRoute)
  val departmentInstance          : DepartmentRoute           = new DepartmentRoute(ImplDepartmentRoute)
  val transactionInstance         : TransactionRoute          = new TransactionRoute(ImplTransactionRoute)
  val employeeAvailabilityInstance: EmployeeAvailabilityRoute = new EmployeeAvailabilityRoute(ImplEmployeeAvailabilityRoute)


  val finalRouteWithContextRoot: Route = pathPrefix("myBigBookApp") {
    bookInstance.bookRoute ~ employeeInstance.employeeRoute ~ departmentInstance.departmentRoute ~ transactionInstance.transactionRoute ~ employeeAvailabilityInstance.employeeAvailabilityRoute
  }

  val binding: Future[ServerBinding] = Http().bindAndHandle(finalRouteWithContextRoot, "localhost", 8081)

  binding.onComplete({
    case Success(res) => println("Successfully bound For Api  Boot ==== " + res)
    case Failure(ex)  => println("failed to bind For Api  Boot "); ex.printStackTrace()
  })
}

object ImplBookRoute extends BookRepositry

object ImplEmployeeRoute extends EmployeeRepositry

object ImplDepartmentRoute extends DepartmentRepositry

object ImplTransactionRoute extends TransactionRepositry

object ImplEmployeeAvailabilityRoute extends EmployeeAvailabilityRepositry