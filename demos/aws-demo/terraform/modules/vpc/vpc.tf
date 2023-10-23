variable "vpc_id" {
  type = string
}

data "aws_vpc" "vpc-aws-default" {
  id = var.vpc_id
}

output default_vpc {
  value = data.aws_vpc.vpc-aws-default.id
}