resource "aws_lb" "lb-flask-web" {
  name               = "${local.flask-web}-lb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.security_group.id]
  subnets            = module.common-vpc.default_subnet_ids

  enable_deletion_protection = false
  tags                       = module.flask-tags.tags
}

resource "aws_lb_target_group" "alb-tg-flask-web" {
  name        = "${local.flask-web}-alb-tg"
  target_type = "ip"
  port        = var.flask-port
  protocol    = "HTTP"
  vpc_id      = module.common-vpc.default_vpc
  stickiness {
    type = "lb_cookie"
  }
  health_check {
    interval            = 90
    path                = "/"
    port                = "traffic-port"
    protocol            = "HTTP"
    timeout             = 60
    healthy_threshold   = 5
    unhealthy_threshold = 2
    matcher             = "200-499"
  }
  tags = module.flask-tags.tags
}

resource "aws_lb_listener" "lb-listener-flask-web" {
  load_balancer_arn = aws_lb.lb-flask-web.arn
  port              = var.flask-port
  protocol          = aws_lb_target_group.alb-tg-flask-web.protocol

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.alb-tg-flask-web.arn
  }
  tags = module.flask-tags.tags
}

resource "aws_lb_listener_rule" "lb-listener-rule-flask-web" {
  listener_arn = aws_lb_listener.lb-listener-flask-web.arn
  priority     = 100

  action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.alb-tg-flask-web.arn
  }

  condition {
    path_pattern {
      values = ["/", "/*"]
    }
  }
  tags = module.flask-tags.tags
}