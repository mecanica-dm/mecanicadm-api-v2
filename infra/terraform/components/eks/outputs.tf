output "eks_cluster_name" {
  description = "Nome do cluster EKS"
  value       = aws_eks_cluster.main.name
}

output "eks_cluster_endpoint" {
  description = "Endpoint do cluster EKS"
  value       = aws_eks_cluster.main.endpoint
}

output "eks_cluster_certificate_authority_data" {
  description = "Dados do certificado codificados em base64 necessários para a comunicacao com o cluster"
  value       = aws_eks_cluster.main.certificate_authority[0].data
}

output "eks_nodes_security_group_id" {
  description = "ID do security group para os worker nodes do EKS"
  value       = aws_security_group.eks_security_group.id
}
