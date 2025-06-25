locals {
  ecs_service_name   = "franchise-app-service"
}

resource "aws_ecs_service" "franchise_app_service" {
  name            = local.ecs_service_name
  cluster         = aws_ecs_cluster.franchise_cluster.id
  task_definition = aws_ecs_task_definition.franchise_app_task.arn
  launch_type     = "FARGATE"
  desired_count   = 1
  #iam_role        = aws_iam_role.ecs_service_role.arn

  network_configuration {
    subnets          = var.service_subnet_ids
    security_groups  = [var.service_security_group_id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb.franchise_alb.arn
    container_name   = "franchise-app"
    container_port   = 8080
  }

  depends_on = [aws_ecs_task_definition.franchise_app_task]
}

/*
resource "aws_iam_role" "ecs_service_role" {
  name = "ecs-franchise-app-service-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs.amazonaws.com"
        }
      },
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_service_role_policy_elb" {
  role       = aws_iam_role.ecs_service_role.name
  policy_arn = "arn:aws:iam::aws:policy/ElasticLoadBalancingFullAccess"
}
*/
