
variable "database_name" {
  type    = string
  default = "franchises_db"
}

variable "database_username" {
  type    = string
  default = "franchises_app_user"
}

variable "db_instance_class" {
  type    = string
  default = "db.t3.micro"
}

variable "db_allocated_storage" {
  type    = number
  default = 10
}

variable "db_engine_version" {
  type    = string
  default = "17.4"
}

variable "db_port" {
  type    = number
  default = 5432
}

variable "db_skip_final_snapshot" {
  type    = bool
  default = true
}