resource "aws_subnet" "subnet-private" {
  vpc_id = aws_vpc.main.id

  count                   = "${length(var.subnet-private-cidr)}"
  cidr_block              = "${var.subnet-private-cidr[count.index]}"
  availability_zone       = "${data.aws_availability_zones.available.names[count.index]}"
  map_public_ip_on_launch = false

  tags = var.tags
}

resource "aws_subnet" "subnet-public" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = var.subnet-public-cidr
  map_public_ip_on_launch = true

  tags = var.tags
}