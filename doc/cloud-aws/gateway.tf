# Create Internet Gateway resource and attach it to the VPC
resource "aws_internet_gateway" "internet-gateway" {
  vpc_id =  aws_vpc.main.id

  tags = var.tags
}

# Create EIP for the IGW
resource "aws_eip" "eip" {
  vpc   = true

  tags = var.tags
}

# Create NAT Gateway resource and attach it to the VPC
resource "aws_nat_gateway" "nat-gateway" {
  allocation_id = aws_eip.eip.id
  subnet_id = aws_subnet.subnet-public.id

  tags = var.tags
}