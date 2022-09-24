# Creating RT for Private Subnet
resource "aws_route_table" "route-table-private" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.nat-gateway.id
  }

  tags = var.tags
}

# Creating RT for Public Subnet
resource "aws_route_table" "route-table-public" {
  vpc_id = aws_vpc.main.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.internet-gateway.id
  }

  tags = var.tags
}

#Associating the Public RT with the Public Subnets
resource "aws_route_table_association" "route-table-association-public" {
  subnet_id      = aws_subnet.subnet-public.id
  route_table_id = aws_route_table.route-table-public.id
}

#Associating the Private RT with the Private Subnets
resource "aws_route_table_association" "route-table-association-private" {
  count = "${length(var.subnet-private-cidr)}"

  subnet_id      = "${element(aws_subnet.subnet-private.*.id, count.index)}"
  route_table_id = aws_route_table.route-table-private.id
}