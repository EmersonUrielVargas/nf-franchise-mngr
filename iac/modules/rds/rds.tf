resource "aws_db_instance" "database-franchise" {
  engine              = "postgres"
  storage_type        = "gp2"
  publicly_accessible = true
  engine_version      = var.db_engine_version
  instance_class      = var.db_instance_class
  db_name             = var.database_name
  username            = var.database_username
  password            = var.database_password
  allocated_storage   = var.db_allocated_storage
  skip_final_snapshot = var.db_skip_final_snapshot
  port                = var.db_port
  parameter_group_name = aws_db_parameter_group.franchise_pg.name
}

resource "aws_db_parameter_group" "franchise_pg" {
  name        = "franchise-pg"
  family      = "postgres17"
  description = "Custom parameter group for franchise DB"

  parameter {
    name  = "rds.force_ssl"
    value = "0"
  }
}