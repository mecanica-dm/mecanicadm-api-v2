resource "aws_vpc" "main" {
  # Delimita os limites lógicos da VPC e permite o roteamento de subredes
  cidr_block = var.vpc_cidr_block
  # Necessário para que serviços e ferramentas que dependem de hostnames funcionem corretamente
  enable_dns_hostnames = true
  # Nodes e pods precisam resolver nomes de API da AWS e serviços internos do Kubernetes
  enable_dns_support = true

  tags = {
    # Identificação do recurso no console
    Name = "${var.environment}-main-vpc"
  }
}

# Necessário para comunicação da VPC com a internet
resource "aws_internet_gateway" "main" {
  vpc_id = aws_vpc.main.id

  tags = {
    Name = "${var.environment}-igw"
  }
}

resource "aws_subnet" "public" {
  count  = 2
  vpc_id = aws_vpc.main.id
  # Subnets públicas usarão os índices 0 e 1 (ex: 10.0.0.0/24 e 10.0.1.0/24)
  cidr_block = cidrsubnet(var.vpc_cidr_block, 8, count.index)
  # Precisamos de pelo menos duas AZs para que o terraform seja aplicado
  availability_zone       = data.aws_availability_zones.available.names[count.index]
  map_public_ip_on_launch = true

  tags = {
    Name = "${var.environment}-public-subnet-${count.index}"
    # Tags fundamentais para que o EKS consiga provisionar Load Balancers automaticamente
    "kubernetes.io/cluster/${var.environment}-eks-cluster" = "shared"
    "kubernetes.io/role/elb"                               = "1"
  }
}

resource "aws_subnet" "private" {
  count  = 2
  vpc_id = aws_vpc.main.id
  # Subnets privadas usarão os índices 2 e 3 (ex: 10.0.2.0/24 e 10.0.3.0/24)
  cidr_block        = cidrsubnet(var.vpc_cidr_block, 8, count.index + 2)
  availability_zone = data.aws_availability_zones.available.names[count.index]

  tags = {
    Name                                                   = "${var.environment}-private-subnet-${count.index}"
    "kubernetes.io/cluster/${var.environment}-eks-cluster" = "shared"
    "kubernetes.io/role/internal-elb"                      = "1"
  }
}



resource "aws_route_table" "public" {
  # Atua como a tabela de roteamento principal para permitir acesso externo direto
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }
  tags = {
    Name = "${var.environment}-public-rt"
  }
}

resource "aws_route_table" "private" {
  # Controla o fluxo de saída dos recursos internos, garantindo que passem pelo NAT Gateway por segurança
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.main.id
  }
  tags = {
    Name = "${var.environment}-private-rt"
  }
}

# Responsável por conectar a aws_route_table com as subnets
resource "aws_route_table_association" "public" {
  count          = 2
  subnet_id      = aws_subnet.public[count.index].id
  route_table_id = aws_route_table.public.id
}

# Responsável por conectar a aws_route_table com as subnets
resource "aws_route_table_association" "private" {
  count          = 2
  subnet_id      = aws_subnet.private[count.index].id
  route_table_id = aws_route_table.private.id
}

data "aws_availability_zones" "available" {
  state = "available"
}
