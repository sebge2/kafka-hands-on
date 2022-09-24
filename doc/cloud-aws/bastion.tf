resource "aws_key_pair" "bastion" {
  key_name   = "bastion"
  public_key = "${file("aws_key.pub")}"
}

resource "aws_security_group" "bastion_ssh" {
  name   = "bastion_ssh"
  vpc_id = "${aws_vpc.main.id}"

  ingress {
    cidr_blocks = [
      "0.0.0.0/0"
    ]
    from_port = 22
    to_port   = 22
    protocol  = "tcp"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "bastion" {
  ami           = "ami-0ec7f9846da6b0f61" // Ubuntu Server 22.04 LTS (HVM), SSD Volume Type x86
  instance_type = "t3.small"

  vpc_security_group_ids = [
    aws_security_group.msk_management_group.id,
    aws_security_group.bastion_ssh.id
  ]
  subnet_id = aws_subnet.subnet-public.id

  user_data            = "${file("bastion-init.sh")}"
  iam_instance_profile = aws_iam_instance_profile.ec2_profile.name

  key_name = aws_key_pair.bastion.key_name

  connection {
    type        = "ssh"
    host        = self.public_ip
    user        = "ubuntu"
    private_key = file("./aws_key")
    timeout     = "4m"
  }

  tags = var.tags
}

resource "aws_iam_role" "ec2_role" {
  name = "ec2_role"

  assume_role_policy = jsonencode({
    "Version" : "2012-10-17",
    "Statement" : [
      {
        "Action" : "sts:AssumeRole",
        "Effect" : "Allow",
        "Sid" : "",
        "Principal" : {
          "Service" : "ec2.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_policy_attachment" "ec2_policy_role" {
  name       = "ec2_attachement"
  roles      = [aws_iam_role.ec2_role.name]
  policy_arn = aws_iam_policy.msk_allow_all_policy.arn
}

resource "aws_iam_instance_profile" "ec2_profile" {
  name = "ec2_profile"
  role = aws_iam_role.ec2_role.name
}

output "bastion_public_dns" {
  value       = "${aws_instance.bastion.*.public_dns}"
  description = "Public bastion DNS"
}