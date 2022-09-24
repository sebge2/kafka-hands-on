resource "aws_vpc" "main" {
  cidr_block = var.vpc-cidr
  enable_dns_hostnames = true

  tags = var.tags
}