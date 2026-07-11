# Guia Terraform (mecanicadm)

## 1. Inicializar o Diretório

O primeiro passo é baixar os providers necessários e iniciar o backend:

```bash
terraform init
```

## 2. Planejamento

O terraform plan mostra o que será alterado:

```bash
terraform plan
```

## 3. Aplicar as Configurações

Comando que aplica efetivamente os arquivos terraform:

```bash
terraform apply
```

## 4. Reset

Para destruir os recursos criados:

```bash
terraform destroy
```