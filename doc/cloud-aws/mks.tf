resource "aws_msk_configuration" "msk-cluster-configuration" {
  name          = "msk-cluster-configuration"
  kafka_versions = [var.kafka_version]

  server_properties = <<PROPERTIES
auto.create.topics.enable = true
delete.topic.enable = true
PROPERTIES
}

resource "aws_msk_serverless_cluster" "msk_cluster" {
  cluster_name = var.resources_name

  vpc_config {
    subnet_ids = aws_subnet.subnet-private[*].id
    security_group_ids = [aws_security_group.msk_management_group.id]
  }

  client_authentication {
    sasl {
      iam {
        enabled = true
      }
    }
  }

  tags = var.tags
}