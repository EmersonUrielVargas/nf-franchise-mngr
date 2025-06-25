output "ecs_cluster_arn" {
  description = "ARN del ECS Cluster"
  value       = aws_ecs_cluster.franchise_cluster.arn
}