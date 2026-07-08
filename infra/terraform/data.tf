# Data source para obter o token de autenticação do EKS
data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.eks_cluster_name
}

# Em ambientes de laboratório (AWS Labs), não podemos criar roles.
# Buscamos a LabRole existente que possui as permissões necessárias.
data "aws_iam_role" "lab_role" {
  name = "LabRole"
}
