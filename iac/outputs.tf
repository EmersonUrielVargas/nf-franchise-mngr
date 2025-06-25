output "db_endpoint" {
  description = "El endpoint de la instancia RDS PostgreSQL."
  value       =  module.rds.db_endpoint
}

output "db_port" {
  description = "El puerto de la instancia RDS PostgreSQL."
  value       = module.rds.db_port
}

output "db_name" {
  description = "Nombre de la base de datos."
  value = module.rds.db_name
}

output "ecr_repository_url" {
  description = "URL del repositorio ECR"
  value       = module.ecr.ecr_repository_url
}
