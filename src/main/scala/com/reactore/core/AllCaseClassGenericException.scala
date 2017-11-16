package com.reactore.core

case class DuplicateEntityException(errorCode: String = "1001", message: String = "DUPLICATE_ENTITY_EXCEPTION", exception: Throwable) extends Exception

case class DuplicateNameException(errorCode: String = "1002", message: String = "DUPLICATE_NAME_EXCEPTION", exception: Throwable) extends Exception

case class UniqueKeyViolationException(errorCode: String = "1003", message: String = "UNIQUE_KEY_VIOLATION_EXCEPTION", exception: Throwable) extends Exception

case class DataBaseException(errorCode: String = "1004", message: String = "DATABASE_EXCEPTION", exception: Throwable) extends Exception

case class GenericException(errorCode: String = "1005", message: String = "GENERIC_EXCEPTION", exception: Throwable) extends Exception

case class InvalidIdException(errorCode: String = "1006", message: String = "INVALID_ID_EXCEPTION", exception: Throwable) extends Exception

case class InvalidListOfIdException(errorCode: String = "1007", message: String = "INVALID_LIST_OF_ID_EXCEPTION", exception: Throwable) extends Exception

case class OtherDatabaseException(errorCode: String = "1008", message: String = "OTHER_DATABASE_EXCEPTION", exception: Throwable) extends Exception

case class NoSuchEntityException(errorCode: String = "1009", message: String = "NO_SUCH_ENTITY_EXCEPTION", exception: Throwable) extends Exception

case class EmptyListException(errorCode: String = "1010", message: String = "EMPTY_LIST_EXCEPTION", exception: Throwable) extends Exception

case class ForeignKeyException(errorCode: String = "1011", message: String = "FOREIGN_KEY_VIOLATION_EXCEPTION", exception: Throwable) extends Exception
