package com.reactore.features.testcase

import com.reactore.features._
import org.scalatest.mockito.MockitoSugar

trait MockCompanyFacade extends CompanyFacadeComponent with MockitoSugar {
  override lazy val companyRepository        : CompanyRepository         = mock[CompanyRepository]
  override lazy val countryRepository        : CountryRepository         = mock[CountryRepository]
  override lazy val vehicleRepository        : VehiclesRepository        = mock[VehiclesRepository]
  override lazy val vehicleCategoryRepository: VehicleCategoryRepository = mock[VehicleCategoryRepository]
  override lazy val vehicleTypeRepository    : VehicleTypeRepository     = mock[VehicleTypeRepository]
}

trait MockCountryFacade extends CountryFacadeComponent with MockitoSugar {
  override lazy val companyRepository: CompanyRepository = mock[CompanyRepository]
  override lazy val countryRepository: CountryRepository = mock[CountryRepository]
}

trait MockVehicleCategoryFacade extends VehicleCategoryFacadeComponent with MockitoSugar {
  override lazy val vehicleCategoryRepository: VehicleCategoryRepository = mock[VehicleCategoryRepository]
  override lazy val vehicleTypeRepository    : VehicleTypeRepository     = mock[VehicleTypeRepository]
}

trait MockVehicleTypeFacade extends VehicleTypeFacadeComponent with MockitoSugar {
  override lazy val vehicleTypeRepository    : VehicleTypeRepository     = mock[VehicleTypeRepository]
  override lazy val vehicleCategoryRepository: VehicleCategoryRepository = mock[VehicleCategoryRepository]
  override lazy val vehicleRepository        : VehiclesRepository        = mock[VehiclesRepository]
}

trait MockVehicleFacade extends VehicleFacadeComponent with MockitoSugar {
  override lazy val vehicleTypeRepository    : VehicleTypeRepository     = mock[VehicleTypeRepository]
  override lazy val vehiclesRepository       : VehiclesRepository        = mock[VehiclesRepository]
  override lazy val companyRepository        : CompanyRepository         = mock[CompanyRepository]
  override lazy val vehicleCategoryRepository: VehicleCategoryRepository = mock[VehicleCategoryRepository]
}