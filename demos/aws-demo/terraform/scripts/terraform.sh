#!/bin/bash

# USAGE: ./terraform.sh $ACTION $BASE $MODULE
PROVIDER_FILE_NAME="provider.aws.tf"
# https://spacelift.io/blog/terraform-tfvars
TF_ARGS="-var-file=../../scripts/terraform.$2.tfvars"

wrap() {
  if [[ -f $PROVIDER_FILE_NAME ]]; then
    rm $PROVIDER_FILE_NAME
  fi
  cat >$PROVIDER_FILE_NAME <<EOF
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.55"
    }
  }

  required_version = ">= 1.3.9"
}

provider "aws" {
  region = var.region
}

variable "region" {
  type = string
}
EOF
}

plan() {
  echo "---------- start to plan $1 ----------"
  wrap &
  terraform init
  terraform plan ${TF_ARGS}
  rm $PROVIDER_FILE_NAME
  echo "---------- end to plan $1 ----------"
}

deploy() {
  echo "---------- start to deploy $1 ----------"
  wrap &
  terraform init
  terraform apply ${TF_ARGS}
  rm $PROVIDER_FILE_NAME
  echo "---------- end to deploy $1 ----------"
}

destroy() {
  echo "---------- start to destroy $1 ----------"
  terraform destroy ${TF_ARGS}
  echo "---------- end to destroy $1 ----------"
}

if [[ -z "$2" ]]; then
  echo "Please specify a base."
  exit 0
else
  if [[ -z "$3" ]]; then
    echo "Please specify your module to proceed."
    exit 0
  else
    if [[ ! -d "../$2/$3" ]]; then
      echo "$2/$3 doesn't exist."
      exit 0
    else
      cd "../$2/$3" || exit
    fi
  fi
fi

if [[ "$1" == "plan" ]]; then
  plan "$3"
elif [[ "$1" == "deploy" ]]; then
  deploy "$3"
elif [[ "$1" == "destroy" ]]; then
  destroy "$3"
else
  echo "invalid action is input. plan|deploy|destroy."
fi
exit 0
