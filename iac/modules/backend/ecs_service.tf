locals {
  ecs_service_name = "franchise-app-service"
}

resource "aws_ecs_service" "franchise_app_service" {
  name            = local.ecs_service_name
  cluster         = aws_ecs_cluster.franchise_cluster.id
  task_definition = aws_ecs_task_definition.franchise_app_task.arn
  launch_type     = "FARGATE"
  desired_count   = 1

  network_configuration {
    subnets          = var.service_subnet_ids
    security_groups  = [var.service_security_group_id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.franchise_alb_tg.arn
    container_name   = "franchise-app"
    container_port   = 8080
  }

  depends_on = [aws_ecs_task_definition.franchise_app_task]
}
