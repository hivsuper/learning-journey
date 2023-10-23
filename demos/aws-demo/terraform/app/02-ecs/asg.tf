resource aws_autoscaling_group "asg-flask" {
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
  instance_refresh {
    strategy = var.strategy
    preferences {
      min_healthy_percentage = var.min_healthy_percentage
      instance_warmup        = var.instance_warmup
    }
    triggers = local.refresh_triggers
  }
}

variable strategy {
  default = "Rolling"
}

variable min_healthy_percentage {
  default = 90
}

variable instance_warmup {
  default = 300
}

locals {
  refresh_triggers = var.terminate_protection ? [] : ["tag", "tags"]
}