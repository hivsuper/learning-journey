resource "aws_ecr_repository" "flask" {
  name = local.flask-web
  tags = module.flask-tags.tags

  image_scanning_configuration {
    scan_on_push = true
  }
}

module "common-ecs-ecr-policy-flask" {
  source = "../../modules/ecs/resource/ecr/policy"
  name   = aws_ecr_repository.flask.name
}

variable "flask" {
  type = string
}

locals {
  flask-web = format("%s-web", var.flask)
}