variable "name" {
}

resource "aws_ecr_lifecycle_policy" "policy" {
  policy     = file("${path.module}/templates/aws-ecr-lifecycle-policy.json")
  repository = var.name
}