resource aws_iam_role "iam-role-flask" {
  name               = local.ecs-instance-role-prefix
  path               = var.path
  assume_role_policy = templatefile("${path.module}/templates/assume-role-policy-service.tpl", {
    aws_services = [
      "ec2.amazonaws.com",
      "ecs-tasks.amazonaws.com"
    ]
  })
  tags = module.flask-tags.tags
}

resource "aws_iam_instance_profile" "iam-instance-profile-assume-role-flask" {
  name = local.ecs-instance-role-prefix
  role = aws_iam_role.iam-role-flask.name
  tags = module.flask-tags.tags
  path = "/service-role/"
}

# custom/inline policy
resource aws_iam_role_policy "flask-permissions" {
  name   = local.ecs-instance-role-prefix
  role   = aws_iam_role.iam-role-flask.name
  policy = templatefile("${path.module}/templates/role-policy-flask.tpl", {})
}

# AWS managed policies
resource "aws_iam_role_policy_attachment" "flask-permissions" {
  for_each = toset([
    "arn:aws:iam::aws:policy/service-role/AmazonEC2ContainerServiceforEC2Role"
  ])
  role       = aws_iam_role.iam-role-flask.name
  policy_arn = each.value
}

locals {
  ecs-instance-role-prefix = "${local.service-name}-ecsInstanceRole"
}