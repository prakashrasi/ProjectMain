package com.reactore.features

trait VehicleFacadeComponent {
  def vehicleTypeRepository: VehicleTypeRepository

  def vehiclesRepository: VehiclesRepository

  def companyRepository: CompanyRepository

  def vehicleCategoryRepository: VehicleCategoryRepository
}

trait VehicleFacade extends VehicleFacadeComponent {
  override lazy val vehicleTypeRepository    : VehicleTypeRepository     = ImplVehicleTypeRepository
  override lazy val vehiclesRepository       : VehiclesRepository        = ImplVehiclesRepository
  override lazy val companyRepository        : CompanyRepository         = ImplCompanyRepository
  override lazy val vehicleCategoryRepository: VehicleCategoryRepository = ImplVehicleCategoryRepository
}

trait CompanyFacadeComponent {
  def companyRepository: CompanyRepository

  def countryRepository: CountryRepository

  def vehicleRepository: VehiclesRepository

  def vehicleTypeRepository: VehicleTypeRepository

  def vehicleCategoryRepository: VehicleCategoryRepository
}

trait CompanyFacade extends CompanyFacadeComponent {
  override lazy val companyRepository        : CompanyRepository         = ImplCompanyRepository
  override lazy val countryRepository        : CountryRepository         = ImplCountryRepository
  override lazy val vehicleRepository        : VehiclesRepository        = ImplVehiclesRepository
  override lazy val vehicleTypeRepository    : VehicleTypeRepository     = ImplVehicleTypeRepository
  override lazy val vehicleCategoryRepository: VehicleCategoryRepository = ImplVehicleCategoryRepository


}

trait CountryFacadeComponent {
  def companyRepository: CompanyRepository

  def countryRepository: CountryRepository
}

trait CountryFacade extends CountryFacadeComponent {
  override lazy val companyRepository: CompanyRepository = ImplCompanyRepository
  override lazy val countryRepository: CountryRepository = ImplCountryRepository
}

trait VehicleCategoryFacadeComponent {
  def vehicleCategoryRepository: VehicleCategoryRepository

  def vehicleTypeRepository: VehicleTypeRepository
}

trait VehicleCategoryFacade extends VehicleCategoryFacadeComponent {
  override lazy val vehicleCategoryRepository: VehicleCategoryRepository = ImplVehicleCategoryRepository
  override lazy val vehicleTypeRepository    : VehicleTypeRepository     = ImplVehicleTypeRepository
}

trait VehicleTypeFacadeComponent {
  def vehicleTypeRepository: VehicleTypeRepository

  def vehicleCategoryRepository: VehicleCategoryRepository

  def vehicleRepository: VehiclesRepository

}

trait VehicleTypeFacade extends VehicleTypeFacadeComponent {
  override lazy val vehicleCategoryRepository: VehicleCategoryRepository = ImplVehicleCategoryRepository
  override lazy val vehicleTypeRepository    : VehicleTypeRepository     = ImplVehicleTypeRepository
  override lazy val vehicleRepository        : VehiclesRepository        = ImplVehiclesRepository
}