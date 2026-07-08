output "eks_cluster_iam_role_arn" {
  description = "ARN da role IAM do cluster EKS"
  value       = aws_iam_role.eks_cluster.arn
}

output "eks_nodes_iam_role_arn" {
  description = "ARN da role IAM dos worker nodes do EKS"
  value       = aws_iam_role.eks_nodes.arn
}

output "eks_nodes_iam_role_name" {
  description = "Nome da role IAM dos worker nodes do EKS"
  value       = aws_iam_role.eks_nodes.name
}