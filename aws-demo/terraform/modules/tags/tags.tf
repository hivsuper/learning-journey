variable "tag_service_id" {
  description = "Tags to set for aws resources"
  type        = string
  default     = null
}

locals {
  tags = merge(var.tag_service_id != null ? {
    SERVICE_ID = var.tag_service_id
  } : {})
}

output "tags" {
  value = local.tags
}