package com.reactore.bookcore

import com.github.tminglei.slickpg._
import org.joda.time.format.{DateTimeFormat, ISODateTimeFormat}
import slick.jdbc.PostgresProfile

class CustomPostgresDriver extends PostgresProfile with PgPostGISSupport with PgDateSupport with PgDateSupportJoda with PgHStoreSupport with PgSearchSupport with PgArraySupport {

  override val api = new API with PostGISImplicits with PostGISAssistants with DateTimeImplicits with HStoreImplicits
    with SearchAssistants with ArrayImplicits with SearchImplicits {

    override val jodaDateFormatter = ISODateTimeFormat.date()
    override val jodaTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss.SSS")
    override val jodaTimeFormatter_NoFraction = DateTimeFormat.forPattern("HH:mm:ss")
    override val jodaDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS").withZoneUTC()
    override val jodaDateTimeFormatter_NoFraction = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC()
    override val jodaTzTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss.SSS")
    override val jodaTzTimeFormatter_NoFraction = DateTimeFormat.forPattern("HH:mm:ss")
    override val jodaTzDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSSSSS").withZoneUTC()
    override val jodaTzDateTimeFormatter_NoFraction = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").withZoneUTC()
  }

  val plainAPI = new API with PostGISPlainImplicits

}

object CustomPostgresDriver extends CustomPostgresDriver