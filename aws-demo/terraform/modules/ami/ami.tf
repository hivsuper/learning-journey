# https://cloud-images.ubuntu.com/locator/ec2/
# https://skundunotes.com/2021/10/24/create-ec2-instance-from-an-aws-ami-using-terraform/
variable "ami-name" {
  type    = string
  default = null
}

variable "owner" {
  type    = string
  default = "amazon"
}

data "aws_ami" "image" {
  most_recent = true
  name_regex  = "^${var.ami-name}-\\d{8}"
  owners      = [
    var.owner
  ]

  filter {
    name   = "name"
    values = [
      "${var.ami-name}*"
    ]
  }
  filter {
    name   = "root-device-type"
    values = [
      "ebs"
    ]
  }

  filter {
    name   = "virtualization-type"
    values = [
      "hvm"
    ]
  }
}

output id {
  value = data.aws_ami.image.id
}