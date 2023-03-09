resource aws_ecs_service "service-ec2-no-scaling" {
  name            = local.service-name
  cluster         = aws_ecs_cluster.common-ecs-cluster-flask.name
  task_definition = aws_ecs_task_definition.common-ecs-task-definition-flask.arn
  placement_constraints {
    type       = "memberOf"
    expression = "attribute:service == ${local.service-name}"
  }
  scheduling_strategy = "DAEMON"
  desired_count       = 1
  lifecycle {
    ignore_changes = [
      desired_count
    ]
  }
  deployment_minimum_healthy_percent = 0
  tags                               = module.flask-tags.tags
  propagate_tags                     = "SERVICE"
  load_balancer {
    target_group_arn = aws_lb_target_group.alb-tg-flask.arn
    container_port   = var.container-port
    container_name   = local.service-name
  }
}

resource "aws_ecs_task_definition" "common-ecs-task-definition-flask" {
  container_definitions = templatefile("${path.module}/templates/ecs.container_definition.tpl", {
    name                  = local.service-name
    image                 = module.common-ecs-ecr.path
    cpu                   = var.cpu
    memory                = var.memory
    containerPort         = var.container-port
    hostPort              = var.flask-port
    awslogs-group         = local.cluster-name
    awslogs-region        = data.aws_region.current.id
    awslogs-stream-prefix = "ecs"
    tags                  = module.flask-tags.tags
  })
  family        = local.service-name
  network_mode  = null
  cpu           = var.cpu
  memory        = var.memory
  tags          = module.flask-tags.tags
  task_role_arn = aws_iam_role.iam-role-flask.arn
}

resource "aws_launch_template" "aws-launch-template-flask" {
  name_prefix          = local.service-name
  image_id             = module.common-ami.id
  instance_type        = var.aws_instance_type
  security_group_names = [aws_security_group.security_group.name]
  iam_instance_profile {
    arn = aws_iam_instance_profile.iam-instance-profile-assume-role-flask.arn
  }
  tags = module.flask-tags.tags
  dynamic "tag_specifications" {
    for_each = toset(["instance", "volume"])
    content {
      resource_type = tag_specifications.key
      tags          = {
        Name = local.service-name
      }
    }
  }
  user_data = module.common-ec2-userdata.ecs_user_data
  lifecycle {
    create_before_destroy = true
    ignore_changes        = [
      image_id,
      user_data,
    ]
  }
}

resource "aws_autoscaling_group" "asg-flask" {
  name               = local.service-name
  availability_zones = var.aws_availability_zones
  desired_capacity   = 1
  max_size           = 1
  min_size           = 1
  dynamic "tag" {
    for_each = [
      for k, v in module.flask-tags.tags : {
        key   = k
        value = v
      }
    ]
    iterator = _tag
    content {
      key                 = _tag.value.key
      value               = _tag.value.value
      propagate_at_launch = true
    }
  }
  launch_template {
    id      = aws_launch_template.aws-launch-template-flask.id
    version = aws_launch_template.aws-launch-template-flask.latest_version
  }
  lifecycle {
    create_before_destroy = true
  }
}