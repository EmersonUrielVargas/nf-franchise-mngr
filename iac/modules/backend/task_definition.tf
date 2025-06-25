locals {
  task_definition_family   = "franchise-app-task"
  task_SO_family = "LINUX"
  task_cpu_architecture = "ARM64"
}

resource "aws_ecs_task_definition" "franchise_app_task" {
  family                   = local.task_definition_family
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name      = "franchise-app"
      image     = "${var.uri_container_img}:2.0"
      essential = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
        }
      ]
      secrets = [
        {
          name      = "DB_USER_PASSWORD"
          valueFrom = "arn:aws:secretsmanager:us-east-1:348800178380:secret:franchise-secrets-58Ao0M:DB_USER_PASSWORD::"
        },
        {
          name      = "DB_USERNAME"
          valueFrom = "arn:aws:secretsmanager:us-east-1:348800178380:secret:franchise-secrets-58Ao0M:DB_USERNAME::"
        },
         {
          name      = "DB_SCHEMA"
          valueFrom = aws_ssm_parameter.db_schema.arn
        },
        {
          name      = "DB_DATABASE"
          valueFrom = aws_ssm_parameter.db_database.arn
        },
        {
          name      = "DB_PORT"
          valueFrom = aws_ssm_parameter.db_port.arn
        },
        {
          name      = "DB_HOST"
          valueFrom = aws_ssm_parameter.db_host.arn
        }
      ]
    }
  ])

  runtime_platform {
    operating_system_family = local.task_SO_family
    cpu_architecture        = local.task_cpu_architecture
  }
}

resource "aws_iam_role" "ecs_task_execution_role" {
  name = "ecsTaskExecutionRoleIac"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution_role_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy_attachment" "ecs_secretsmanager_access" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/SecretsManagerReadWrite"
}

resource "aws_iam_role_policy_attachment" "ecs_ssm_access" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMReadOnlyAccess"
}