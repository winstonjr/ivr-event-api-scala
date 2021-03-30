package cc.atleastonce

import io.getquill.{MysqlJdbcContext, SnakeCase}

package object models {
  lazy val ctx = new MysqlJdbcContext(SnakeCase, "ctx")
}
