package cc.atleastonce.controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.JsValue

import scala.util.{Failure, Success, Try}
import cc.atleastonce.models.{StartAssistance, StartAssistanceService}
import com.eclipsesource.schema._

@Singleton
class StartAssistanceController @Inject()(val controllerComponents: ControllerComponents,
                                          val sas: StartAssistanceService) extends BaseController {
  private implicit val startAssistanceReads: Reads[StartAssistance] = Json.reads[StartAssistance]

  def insertEvent(): Action[JsValue] = Action(parse.json) { implicit request =>
    val validation = validator.validate(startAssistanceValidator, request.body, startAssistanceReads)

    validation.fold(
      invalid = { errors => PreconditionFailed(errors.toJson) },
      valid = { sa =>
        sas.insertEvent(sa) match {
          case Right(value) =>
            Created(s"""{"id":"${value.id.getOrElse(-1_000L)}"}""").as(JSON)
          case Left(value) =>
            ExpectationFailed(s"""{"message":"${value.getMessage}"}""").as(JSON)
        }
      }
    )

    //    Try(Json.fromJson[StartAssistance](request.body).get) match {
    //      case Success(sa) =>
    //        sas.insertEvent(sa) match {
    //          case Right(value) =>
    //            Created(s"""{"id":"${value.id.getOrElse(-1_000L)}"}""").as(JSON)
    //          case Left(value) =>
    //            ExpectationFailed(s"""{"message":"${value.getMessage}"}""").as(JSON)
    //        }
    //      case Failure(_) =>
    //        PreconditionFailed(s"""{"message":"um ou mais campos obrigatórios não foram enviados"}""").as(JSON)
    //    }
  }
}


