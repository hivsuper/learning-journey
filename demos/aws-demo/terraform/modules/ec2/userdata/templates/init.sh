#!/usr/bin/env bash
# Install docker in EC2
# https://www.cnblogs.com/Can-daydayup/p/16472375.html
sudo -s
apt-get install ca-certificates curl gnupg lsb-release
mkdir -p /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
apt-get update
apt-get --yes install docker-ce docker-ce-cli containerd.io docker-compose-plugin
# Install ecs agent in EC2
# https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ecs-agent-install.html
curl -O https://s3.us-west-2.amazonaws.com/amazon-ecs-agent-us-west-2/amazon-ecs-init-latest.amd64.deb
dpkg -i amazon-ecs-init-latest.amd64.deb
rm amazon-ecs-init-latest.amd64.deb

# Set userdata
# https://dev.to/awscommunity-asean/new-ecs-instance-registration-with-ecs-cluster-using-cmd-and-console-19f5
# https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ecs-agent-config.html
cat <<EOF >/etc/ecs/ecs.config
ECS_CLUSTER=${ecs_cluster}
ECS_ENABLE_CONTAINER_METADATA=true
ECS_INSTANCE_ATTRIBUTES=${ecs_instance_attributes}
ECS_RESERVED_MEMORY=${ecs_reserved_memory}
ECS_CONTAINER_INSTANCE_TAGS=${ecs_container_instance_tags}

EOF
# Start ECS
sudo systemctl start ecs