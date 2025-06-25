resource "aws_ssm_parameter" "db_host" {
  name  = "/franchise/db/host"
  type  = "String"
  value = var.db_endpoint
}

resource "aws_ssm_parameter" "db_port" {
  name  = "/franchise/db/port"
  type  = "String"
  value = var.db_port
}

resource "aws_ssm_parameter" "db_database" {
  name  = "/franchise/db/database"
  type  = "String"
  value = var.db_name
}

resource "aws_ssm_parameter" "db_schema" {
  name  = "/franchise/db/schema"
  type  = "String"
  value = var.bd_schema_name
}