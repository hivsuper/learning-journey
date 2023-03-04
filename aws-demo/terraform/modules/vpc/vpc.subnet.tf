data aws_subnets "subnets-aws-default" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.vpc-aws-default.id]
  }
}

data "aws_subnet" "subnet-aws-default" {
  for_each = toset(data.aws_subnets.subnets-aws-default.ids)
  id       = each.value
}

output "default_subnet_ids" {
  value = [for s in data.aws_subnet.subnet-aws-default : s.id]
}