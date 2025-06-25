resource "aws_ecs_cluster" "franchise_cluster" {
  name = var.ecs_cluster_name
}