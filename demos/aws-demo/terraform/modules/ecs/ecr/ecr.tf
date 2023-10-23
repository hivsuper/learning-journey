variable repository {}

variable ecr_tag_version {}

data aws_ecr_repository repository {
  name = var.repository
}

output path {
  value = format("%s%s", data.aws_ecr_repository.repository.repository_url, length(trimspace(var.ecr_tag_version)) > 0? format(":%s", trimspace(var.ecr_tag_version)):"")
}