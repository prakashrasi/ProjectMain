package com.reactore.features

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.util.{Failure, Success}

object ApiBoot extends Directives with App {

  implicit val system      : ActorSystem       = ActorSystem("my-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val countryInstance        : CountryRoute         = new CountryRoute(ImplCountryService)
  val companyInstance        : CompanyRoute         = new CompanyRoute(ImplCompanyService)
  val vehicleCategoryInstance: VehicleCategoryRoute = new VehicleCategoryRoute(ImplVehicleCategory)
  val vehicleTypeService                            = new VehicleTypeRoute(ImplVehicleType)
  val vehicleService                                = new VehicleRoute(ImplVehicleService)

  val finalRouteWithContextRoot: Route = pathPrefix("myBigApp") {
    countryInstance.countryRoute ~ companyInstance.companyRoute ~ vehicleCategoryInstance.vehicleCategoryRoute ~ vehicleTypeService.route1 ~ vehicleService.route1
  }

  val binding: Future[ServerBinding] = Http().bindAndHandle(finalRouteWithContextRoot, "localhost", 8081)

  binding.onComplete({
    case Success(res) => println("Successfully bound For Api Boot ==== " + res)
    case Failure(ex)  => println("failed to bind For Api Boot "); ex.printStackTrace()
  })
}

object ImplCountryService extends CountryService with CountryFacade

object ImplCompanyService extends CompanyService with CompanyFacade

object ImplVehicleCategory extends VehicleCategoryService with VehicleCategoryFacade

object ImplVehicleType extends VehicleTypeService with VehicleTypeFacade

object ImplVehicleService extends VehicleService with VehicleFacade