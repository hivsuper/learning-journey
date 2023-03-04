resource "aws_security_group" "security_group" {
  name_prefix = local.security-group
  vpc_id      = module.common-vpc.default_vpc
  tags        = module.flask-tags.tags
}

variable "sg_ingress_rules" {
  type = map(object({
    from_port   = number
    to_port     = number
    cidr_blocks = string
    protocol    = string
    type        = string
  }))
  default = {}
}

locals {
  rules = {
    # inbound rules
    flask : {
      from_port   = var.flask-port
      to_port     = var.flask-port
      cidr_blocks = "${chomp(data.http.my-ip.response_body)}/32"
      protocol    = "tcp"
      type        = "ingress"
    },
    ssh : {
      from_port   = 22
      to_port     = 22
      cidr_blocks = "0.0.0.0/0"
      protocol    = "tcp"
      type        = "ingress"
    },
    # outbound rules
    https : {
      from_port   = 443
      to_port     = 443
      cidr_blocks = "0.0.0.0/0"
      protocol    = "tcp"
      type        = "egress"
    },
    http : {
      from_port   = 80
      to_port     = 80
      cidr_blocks = "0.0.0.0/0"
      protocol    = "tcp"
      type        = "egress"
    }
  }
}

resource "aws_security_group_rule" "security-group-flask" {
  for_each    = local.rules
  from_port   = each.value.from_port
  to_port     = each.value.to_port
  cidr_blocks = [each.value.cidr_blocks]
  protocol    = each.value.protocol
  type        = each.value.type
  security_group_id = aws_security_group.security_group.id
}