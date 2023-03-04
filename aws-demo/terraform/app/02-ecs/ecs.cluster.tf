resource "aws_ecs_cluster" "common-ecs-cluster-flask" {
  name = local.cluster-name
  tags = module.flask-tags.tags
}

resource "aws_cloudwatch_log_group" "cloudwatch-log-group-flask" {
  name              = aws_ecs_cluster.common-ecs-cluster-flask.name
  retention_in_days = 7
  tags              = module.flask-tags.tags
}