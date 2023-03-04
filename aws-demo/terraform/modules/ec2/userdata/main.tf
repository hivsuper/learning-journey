variable "cluster_name" {}

variable "service_name" {}

variable "ecs_reserved_memory" {
  default = 0
}

variable "ecs_container_instance_tags" {
  type    = map(string)
  default = {}
}

locals {
  ecs_user_data = templatefile("${path.module}/templates/init.sh", {
    ecs_cluster             = var.cluster_name
    ecs_instance_attributes = jsonencode({
      service = var.service_name
    })
    ecs_reserved_memory         = var.ecs_reserved_memory
    ecs_container_instance_tags = jsonencode(merge(local.instance_attributes, var.ecs_container_instance_tags))
  })
}

output "ecs_user_data" {
  value = base64encode(local.ecs_user_data)
}

locals {
  instance_attributes = {
    service = var.service_name
  }
}