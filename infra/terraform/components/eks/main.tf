resource "aws_security_group" "eks_security_group" {
  name        = "${var.environment}-eks-cluster-sg"
  description = "Comunicacao do cluster com os worker nodes"
  vpc_id      = var.vpc_id

  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    self        = true
    description = "Permite que os nodes se comuniquem entre si e com o control plane"
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_eks_cluster" "main" {
  name = "${var.environment}-eks-cluster"

  access_config {
    authentication_mode                         = "API"
    bootstrap_cluster_creator_admin_permissions = true
  }

  role_arn = var.eks_cluster_iam_role_arn
  version  = var.kubernetes_version

  vpc_config {
    subnet_ids         = var.all_subnet_ids
    security_group_ids = [aws_security_group.eks_security_group.id]
  }

  # Para AWS LABS não funciona
  # Garante que as permissões do IAM estejam prontas antes do cluster ser criado
  # depends_on = [var.eks_cluster_iam_role_arn]
}

resource "aws_eks_node_group" "eks_node_group" {
  cluster_name    = aws_eks_cluster.main.name
  node_group_name = "${var.environment}-node-group"
  node_role_arn   = var.eks_nodes_iam_role_arn
  subnet_ids      = var.all_subnet_ids

  scaling_config {
    desired_size = var.desired_size
    max_size     = var.max_size
    min_size     = var.min_size
  }

  update_config {
    max_unavailable = var.max_unavailable
  }

  instance_types = ["t3.micro"]
  capacity_type  = "ON_DEMAND"

  # Para AWS LABS não funciona
  # Crucial: O Node Group falha se as policies não estiverem anexadas à role
  # depends_on = [var.eks_nodes_iam_role_arn]
}
