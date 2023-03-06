module common-ecs-ecr {
  source          = "../../modules/ecs/ecr"
  repository      = local.flask-web
  ecr_tag_version = var.ecr_tag_version
}

# Ensure the ami-name is in the Supported operating systems and system architectures on
# https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ecs-anywhere.html
module common-ami {
  source   = "../../modules/ami"
  ami-name = "ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server"
}

module "common-vpc" {
  source = "../../modules/vpc"
  vpc_id = var.vpc_id
}

module "common-ec2-userdata" {
  source       = "../../modules/ec2/userdata"
  cluster_name = local.cluster-name
  service_name = local.service-name
}

data "http" "my-ip" {
  url = "https://ipv4.icanhazip.com"
}

data aws_region current {}

variable aws_instance_type {
  default = "t2.micro"
}

variable ecr_tag_version {}

variable "vpc_id" {
  type = string
}

variable "flask" {
  type = string
}

variable "aws_availability_zones" {
  type = list(string)
}

variable "path" {
  type    = string
  default = "/service-role/"
}

variable "cpu" {
  default = 1024
}

variable "memory" {
  default = 768
}

variable "flask-port" {
  default = 80
}

locals {
  cluster-name   = format("%s-cluster", var.flask)
  service-name   = format("%s-service", var.flask)
  security-group = format("%s-sg", var.flask)
  flask-web = format("%s-web", var.flask)
}