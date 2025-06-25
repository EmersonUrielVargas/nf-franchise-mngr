
variable "ecs_cluster_name" {
  type = string
}

variable "bd_schema_name" {
  type = string
}

variable "db_name" {
  type = string
}

variable "db_port" {
  type = string
}

variable "db_endpoint" {
  type = string
}

variable "uri_container_img" {
  type = string
}

variable "service_subnet_ids" {
  type = list(string)
}

variable "service_security_group_id" {
  type = string
}

variable "alb_vpc_id" {
  type = string
}