output "ecs_cluster_arn" {
  description = "ARN del ECS Cluster"
  value       = aws_ecs_cluster.franchise_cluster.arn
}
output "alb_dns_name" {
  description = "DNS público del ALB"
  value       = aws_lb.franchise_alb.dns_name
}