output "db_endpoint" {
  description = "El endpoint de la instancia RDS PostgreSQL."
  value       = aws_db_instance.database-franchise.address
}

output "db_port" {
  description = "El puerto de la instancia RDS PostgreSQL."
  value       = aws_db_instance.database-franchise.port
}

output "db_name" {
  description = "Nombre de la base de datos."
  value       = var.database_name
}