package cc.atleastonce.models

import javax.inject.{Inject, Singleton}
import scala.util.{Failure, Success, Try}

case class StartAssistance(id: Option[Long], assistanceId: String, channel: String, platform: String, project: String,
                          channelId: String, environment: String, channelTimestamp: java.time.LocalDateTime, unstructured: String) {
  require(null != assistanceId && assistanceId.nonEmpty, "assistanceId é obrigatório")
  require(null != channel && channel.nonEmpty, "channel é obrigatório")
  require(null != platform && platform.nonEmpty, "platform é obrigatório")
  require(null != project && project.nonEmpty, "project é obrigatório")
  require(null != channelId && channelId.nonEmpty, "channelId é obrigatório")
  require(null != environment && environment.nonEmpty, "environment é obrigatório")
  require(null != channelTimestamp, "channelTimestamp é obrigatório")

  def toStartAssistanceDB: StartAssistanceDB = {
    StartAssistanceDB(None, assistanceId, channel, platform, project, channelId, environment, channelTimestamp,
      unstructured)
  }

  def validateRules: Either[Throwable, Boolean] = {
    var errors: scala.collection.mutable.Seq[String] = scala.collection.mutable.Seq.empty

    // Valida Channel
    if (!"IVR".equals(channel)) {
      "O valor permitido para o campo 'channel' atualmente é IVR" +: errors
    }

    // Valida Environment
    val validEnvironments = List("Production", "Validation", "Tests", "Development")
    if (!validEnvironments.contains(environment)) {
      "Os valores permitidos para o campo 'environment' atualmente são: [Production, Validation, Tests, Development]" +: errors
    }

    if (errors.nonEmpty)
      Left(new Error(errors.mkString(",")))
    else
      Right(true)
  }
}

case class StartAssistanceDB(id: Option[Long], assistanceId: String, channel: String, platform: String, project: String,
                              channelId: String, environment: String, channelTimestamp: java.time.LocalDateTime, unstructured: String,
                              ) {
  def toStartAssistance(id: Option[Long]): StartAssistance = {
    StartAssistance(id, assistanceId, channel, platform, project, channelId, environment, channelTimestamp, unstructured)
  }
}

@Singleton
class StartAssistanceService @Inject()() {
  import ctx._
  implicit val queryMeta: SchemaMeta[StartAssistanceDB] = schemaMeta[StartAssistanceDB](
     "ivr_events.start_assistances"
  )
  private val logger = play.api.Logger(this.getClass)

  def insertEvent(sa: StartAssistance): Either[Throwable, StartAssistance] = {
    sa.validateRules match {
      case Left(value) =>
        Left(value)
      case Right(_) =>
        Try(sa.toStartAssistanceDB) match {
          case Success(_) => save(sa.toStartAssistanceDB)
          case Failure(err) => Left(new Error(err.getMessage, err))
        }
    }
  }

  private def save(sadb: StartAssistanceDB): Either[Throwable, StartAssistance] = {
    Try {
      run(quote {
        query[StartAssistanceDB].insert(lift(sadb)).returningGenerated(_.id)
      }).headOption
    } match {
      case Success(id) =>
        Right(sadb.toStartAssistance(id))
      case Failure(error) =>
        this.logger.error(s"Erro ao inserir o evento StartAssistance", error)
        Left(error)
    }
  }
}