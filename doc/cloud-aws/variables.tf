variable "region" {
  default = "eu-central-1"
  description = "AWS Region"
}

variable "resources_name" {
  default = "kafka-test"
}

variable "tags" {
  default = {
   "Name": "kafka-test"
  }
  description = "Applied tags"
}

variable "vpc-cidr" {
  default = "172.168.0.0/16"
}

variable "subnet-public-cidr" {
  default = "172.168.0.0/24"
}

variable "subnet-private-cidr" {
  default = ["172.168.1.0/24", "172.168.2.0/24"]
}

variable "kafka_version" {
  default = "3.3.2"
}