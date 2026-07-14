module "vpc" {
  source      = "./components/vpc"
  environment = var.environment
}

module "eks" {
  source                   = "./components/eks"
  environment              = var.environment
  vpc_id                   = module.vpc.vpc_id
  all_subnet_ids           = concat(module.vpc.public_subnet_ids, module.vpc.private_subnet_ids)
  private_subnet_ids       = module.vpc.private_subnet_ids
  eks_cluster_iam_role_arn = data.aws_iam_role.lab_role.arn
  eks_nodes_iam_role_arn   = data.aws_iam_role.lab_role.arn
}

resource "time_sleep" "wait_for_eks" {
  depends_on = [module.eks]

  create_duration = "30s"
}

# Kube: Fazemos os deploys dos manifestos
resource "kubectl_manifest" "k8s_resources" {
  for_each = fileset("${path.module}/../../k8s/manifests", "**/*.yaml")

  yaml_body = file("${path.module}/../../k8s/manifests/${each.value}")

  depends_on = [time_sleep.wait_for_eks]
}
