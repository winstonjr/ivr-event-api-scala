package cc.atleastonce

import com.eclipsesource.schema.{SchemaType, SchemaValidator}
import com.eclipsesource.schema.drafts.Version7
import com.eclipsesource.schema.drafts.Version7._
import play.api.libs.json.Json

package object controllers {
  val validator = SchemaValidator(Some(Version7))

  private val startAssistanceJsonSchemaValidator = """{
                                         |  "$schema":"http://json-schema.org/draft-07/schema#",
                                         |  "$id":"http://atleastonce.cc/schemas/start-assistances.schema.json",
                                         |  "title":"schema for an IVR startAssistances",
                                         |  "type":"object",
                                         |  "properties":{
                                         |    "id":{
                                         |      "description":"Idenficador artificial da tabela",
                                         |      "type":"integer",
                                         |      "format":"uuid"
                                         |    },
                                         |    "assistanceId":{
                                         |      "description":"id para correlacionar com os dados gerais do atendimento",
                                         |      "type":"string",
                                         |      "format":"uuid"
                                         |    },
                                         |    "channel":{
                                         |      "description":"Qual o canal d interacao com o cliente",
                                         |      "type":"string",
                                         |      "enum":[
                                         |        "IVR",
                                         |        "VDA",
                                         |        "WhatsApp",
                                         |        "WebChat"
                                         |      ]
                                         |    },
                                         |    "platform":{
                                         |      "description":"Em qual plataforma esta desenvolvido o canal",
                                         |      "type":"string"
                                         |    },
                                         |    "project":{
                                         |      "description":"Nome do projeto",
                                         |      "type":"string"
                                         |    },
                                         |    "channelId":{
                                         |      "description":"Pode ser o telefone no formato {+5511977776666} ou outro id para relevante do canal",
                                         |      "type":"string"
                                         |    },
                                         |    "environment":{
                                         |      "description":"Ambiente onde a aplicação está publicada",
                                         |      "type":"string",
                                         |      "enum":[
                                         |        "Production",
                                         |        "Validation",
                                         |        "Tests",
                                         |        "Development"
                                         |      ]
                                         |    },
                                         |    "channelTimestamp":{
                                         |      "description":"Hora do inicio do atendimento na plataforma de atendimento",
                                         |      "type":"string",
                                         |      "format":"date-time"
                                         |    },
                                         |    "unstructured":{
                                         |      "description":"campo para dados relacionados com o evento principal",
                                         |      "type":"string"
                                         |    },
                                         |    "creation":{
                                         |      "description":"Data da criacao do registro",
                                         |      "type":"string",
                                         |      "format":"date-time"
                                         |    }
                                         |  },
                                         |  "additionalProperties": false,
                                         |  "required":[
                                         |    "assistanceId",
                                         |    "channel",
                                         |    "platform",
                                         |    "project",
                                         |    "channelId",
                                         |    "environment",
                                         |    "channelTimestamp"
                                         |  ]
                                         |}""".stripMargin

  val startAssistanceValidator = Json.fromJson[SchemaType](Json.parse(startAssistanceJsonSchemaValidator)).get

}