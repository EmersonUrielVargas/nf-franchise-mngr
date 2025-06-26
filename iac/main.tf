data "aws_secretsmanager_secret_version" "secret_franchise" {
  secret_id = "arn:aws:secretsmanager:us-east-1:348800178380:secret:franchise-secrets-58Ao0M"
}

module "rds" {
  source                 = "./modules/rds"
  database_name          = var.database_name
  database_username      = var.database_username
  database_password      = jsondecode(data.aws_secretsmanager_secret_version.secret_franchise.secret_string)["DB_USER_PASSWORD"]
  db_instance_class      = var.db_instance_class
  db_allocated_storage   = var.db_allocated_storage
  db_engine_version      = var.db_engine_version
  db_port                = var.db_port
  db_skip_final_snapshot = var.db_skip_final_snapshot
}

module "ecr" {
  source   = "./modules/ecr"
  ecr_name = "franchise-app-registry"
}

module "backend" {
  source                    = "./modules/backend"
  ecs_cluster_name          = "franchise-cluster"
  bd_schema_name            = "franchises_app"
  db_endpoint               = module.rds.db_endpoint
  db_name                   = module.rds.db_name
  db_port                   = module.rds.db_port
  uri_container_img         = module.ecr.ecr_repository_url
  service_security_group_id = data.aws_security_group.default.id
  service_subnet_ids        = data.aws_subnets.default.ids
  alb_vpc_id                = data.aws_vpc.default.id
}