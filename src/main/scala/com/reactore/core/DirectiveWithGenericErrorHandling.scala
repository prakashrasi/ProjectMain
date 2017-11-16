package com.reactore.core

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives
import com.reactore.core.CustomSerializers.{JodaDateSerializer, JodaDateTimeSerializer}
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, LocalDate}
import org.json4s.JsonAST.{JNull, JString}
import org.json4s.native.Serialization._
import org.json4s.{CustomSerializer, DefaultFormats, Formats}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object CustomSerializers {

  case object JodaDateSerializer extends CustomSerializer[LocalDate](date => ( {
    case JString(date) => LocalDate.parse(date)
    case JNull         => null
  }, {
    case ld: LocalDate => JString(ld.toString("yyyy-MM-dd"))
  }))

  case object JodaDateTimeSerializer extends CustomSerializer[DateTime](date => ( {
    case JString(date) => DateTime.parse(date, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
    case JNull         => null
  }, {
    case localDateTime: DateTime => JString(localDateTime.toString("yyyy-MM-dd HH:mm:ss"))
  }))

}

trait DirectiveWithGenericErrorHandling extends Directives {
  implicit val formats: Formats = new DefaultFormats {
  } ++ List(JodaDateSerializer, JodaDateTimeSerializer)

  /** Generic Method For Error Handling **/
  def respond(result: Future[_]): Future[HttpResponse] = {
    result.map { value =>
      HttpResponse(entity = HttpEntity(ContentType(MediaTypes.`application/json`), toJson(value)))
    }.recover {
      case ex => val (statusCode, message) = handleErrorMessages(ex)
        if (statusCode == StatusCodes.NoContent)
          HttpResponse(status = statusCode)
        else
          HttpResponse(status = statusCode, entity = HttpEntity(MediaTypes.`application/json`, message.asInstanceOf[AnyRef].asJson))
    }
  }


  def toJson(value: Any): String = {
    if (value.isInstanceOf[String]) value.asInstanceOf[String] else value.asInstanceOf[AnyRef].asJson
  }

  implicit class JsonConvertion(value: AnyRef) {
    def asJson: String = {
      write(value)
    }
  }

  def handleErrorMessages(ex: Throwable) = {
    ex match {
      case cmd: NoSuchEntityException       => (StatusCodes.BadRequest, cmd.message)
      case cmd: DuplicateEntityException    => (StatusCodes.BadRequest, cmd.message)
      case cmd: InvalidIdException          => (StatusCodes.Conflict, cmd.message)
      case cmd: GenericException            => (StatusCodes.Conflict, cmd.message)
      case cmd: EmptyListException          => (StatusCodes.Conflict, cmd.message)
      case cmd: ForeignKeyException         => (StatusCodes.Conflict, cmd.message)
      case cmd: UniqueKeyViolationException => (StatusCodes.Conflict, cmd.message)
      case cmd: DataBaseException           => (StatusCodes.Conflict, cmd.message)
      case any: Exception                   => {
        val errMsg: String = if (ex.getCause != null) ex.getCause.getMessage
        else if (ex.getMessage != null) ex.getMessage
        else "Internal Server Error"
        any.printStackTrace()
        (StatusCodes.InternalServerError, errMsg)
      }
    }
  }
}