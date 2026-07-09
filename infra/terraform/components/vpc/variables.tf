variable "environment" {
  description = "Nome do ambiente"
  type        = string
}

variable "vpc_cidr_block" {
  description = "CIDR Block para a VPC"
  type        = string
  default     = "10.0.0.0/16"
}
