output "ecr_repository_url" {
  description = "URL del repositorio ECR"
  value       = aws_ecr_repository.franchise_app_registry.repository_url
}