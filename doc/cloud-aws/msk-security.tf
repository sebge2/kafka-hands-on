resource "aws_security_group" "msk_management_group" {
  name        = var.resources_name
  description = "Allow MSK management inbound traffic"
  vpc_id      = aws_vpc.main.id

  ingress {
    description = "MSK management from this SG"
    from_port   = 9098
    to_port     = 9098
    protocol    = "tcp"
    self        = true
  }

  ingress {
    description = "MSK management from this SG"
    from_port   = 9092
    to_port     = 9092
    protocol    = "tcp"
    self        = true
  }

  ingress {
    description = "MSK management from this SG"
    from_port   = 9094
    to_port     = 9094
    protocol    = "tcp"
    self        = true
  }

  ingress {
    description = "MSK management from this SG"
    from_port   = 9194
    to_port     = 9194
    protocol    = "tcp"
    self        = true
  }

  ingress {
    description = "MSK management from this SG"
    from_port   = 9096
    to_port     = 9096
    protocol    = "tcp"
    self        = true
  }

  ingress {
    description = "MSK management from this SG"
    from_port   = 9196
    to_port     = 9196
    protocol    = "tcp"
    self        = true
  }

  ingress {
    description = "MSK management from this SG"
    from_port   = 9198
    to_port     = 9198
    protocol    = "tcp"
    self        = true
  }

  ingress {
    description = "MSK management from this SG"
    from_port   = 2181
    to_port     = 2181
    protocol    = "tcp"
    self        = true
  }

  ingress {
    description = "MSK management from this SG"
    from_port   = 2182
    to_port     = 2182
    protocol    = "tcp"
    self        = true
  }
}

resource "aws_iam_policy" "msk_allow_all_policy" {
  name = var.resources_name
  path = "/"

  policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Action": [
          "kafka-cluster:*Topic*",
          "kafka-cluster:AlterGroup",
          "kafka-cluster:ReadData",
          "kafka-cluster:DescribeCluster",
          "kafka-cluster:AlterCluster",
          "kafka-cluster:DescribeGroup",
          "kafka-cluster:Connect",
          "kafka-cluster:WriteData"
        ],
        "Resource": "*"
      }
    ]
  })
}