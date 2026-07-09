variable "environment" {
  description = "Nome do ambiente"
  type        = string
}

variable "vpc_id" {
  description = "ID da VPC"
  type        = string
}

variable "all_subnet_ids" {
  description = "Lista de todos os IDs de sub-rede (publicas e privadas)"
  type        = list(string)
}

variable "private_subnet_ids" {
  description = "Lista de IDs de sub-redes privadas"
  type        = list(string)
}

variable "eks_cluster_iam_role_arn" {
  description = "ARN da role IAM do cluster EKS"
  type        = string
}

variable "eks_nodes_iam_role_arn" {
  description = "ARN da role IAM dos worker nodes do EKS"
  type        = string
}

variable "kubernetes_version" {
  description = "Versao do Kubernetes para o cluster EKS"
  type        = string
  default     = "1.32"
}

variable "desired_size" {
  description = "Valor desejável de nodes"
  type        = number
  default     = 2
}

variable "min_size" {
  description = "Valor mínimo de nodes"
  type        = number
  default     = 1
}

variable "max_size" {
  description = "Valor máximo de nodes"
  type        = number
  default     = 3
}

variable "max_unavailable" {
  description = "Máximo de nodes indisponiveis"
  type        = number
  default     = 1
}
