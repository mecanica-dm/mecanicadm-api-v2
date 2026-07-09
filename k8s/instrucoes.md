# Guia Kubernetes (mecanicadm)

## 0. Build e Publicação da Imagem Docker

Antes de iniciar, certifique-se de limpar e gerar um novo artefato da aplicação e buildar a imagem Docker para que ela
esteja
disponível no seu ambiente:

```bash
docker rmi -f mecanica-dm-api:latest
```

```bash
# Build da imagem
docker build -t mecanica-dm-api:latest .
```

Para que o Kubernetes em produção ou em outros nós consiga baixar a imagem, ela deve estar em um registro:

```bash
# Substitua pelo seu username do Docker Hub
docker tag mecanica-dm-api:latest guilhermemuchon/mecanica-dm-api:latest

# Login e Push
docker login
docker push guilhermemuchon/mecanica-dm-api:latest
```

## 1. Aplicar a Configuração

Utilize o Kustomize para aplicar todos os manifestos de uma vez:

 ```bash
 kubectl apply -k ./k8s/kustomize/
 ```

## 2. Verificar Recursos

Confirme se todos os Pods, Services e Deployments foram criados no namespace correto:

 ```bash
 kubectl get all -n mecanicadm
 ```

### Pegar nomes específicos de pods

 ```bash
 kubectl get pods -n mecanicadm
 ```

## 3. Logs

Como acessar logs

### Logs usando Seletores (Labels)

Como em um Deployment o nome do pod muda toda vez que ele é reiniciado, é muito mais prático usar a label:

```bash
kubectl logs -l app=mecanica-dm-api -n mecanicadm -f
```

### Logs de um pod específico

Se você tiver o nome exato do pod (obtido via kubectl get pods):

```bash
kubectl logs <nome-do-pod> -n mecanicadm
```

### Ver logs do banco de dados:

```bash
kubectl logs -l app=postgres-db -n mecanicadm
```

## 4. Troubleshooting (Resolução de Problemas)

### Se o Pod estiver em CrashLoopBackOff:

1. **Verificar eventos do Kubernetes:**
   ```bash
   kubectl describe pod <nome-do-pod> -n mecanicadm
   ```

2. **Ver logs do crash anterior (muito importante):**
   ```bash
   kubectl logs <nome-do-pod> -n mecanicadm --previous
   ```

3. **Verificar conectividade com o Banco:**
   Certifique-se de que o serviço `postgres-db` está rodando antes da API.

## 5. Limpar tudo

Se quiser recomeçar do zero:

```bash
kubectl delete -k ./k8s/kustomize/
```
