# [011]: [Implementação de Clean Architecture Purista no Vehicle]

* **Status**: Aceito
* **Data**: 26/05/2026

## 1. Contexto e Problema

A feature Vehicle estava sendo implementada com padrões inconsistentes em relação ao Clean Architecture definido no projeto. Especificamente:

1. **Domínio Anêmico**: A entidade Vehicle era um simples container de dados sem lógica de negócio
2. **Validações Dispersas**: Validações estavam no Service em vez de na entidade
3. **Inversão de Dependência Incompleta**: Services dependiam diretamente de JpaRepository (concreto) em vez de uma abstração
4. **Sem DTOs de Resposta**: Controllers retornavam entidades de domínio diretamente
5. **Exceções Incompletas**: Faltavam exceções específicas de validação

Isso violava os princípios de **Clean Architecture** e **DDD** já adotados no projeto.

## 2. Decisão

Refatoramos completamente a feature Vehicle para seguir **Clean Architecture Purista com Domain-Driven Design**:

### 2.1 Rich Domain Model
A entidade Vehicle agora encapsula toda a lógica de negócio:
- Factory method `create()` garante estado válido na criação
- Método `updateInfo()` garante validações em alterações
- Todas as regras de validação residem na entidade

```java
public class Vehicle extends AuditEntity {
    
    private Vehicle(String model, String licensePlate, String brand, Short modelYear) {
        // ...
        validate(); // Garante estado válido
    }
    
    public static Vehicle create(...) {
        return new Vehicle(...);
    }
    
    public void updateInfo(...) {
        // ...
        validate(); // Garante estado válido
    }
    
    private void validate() {
        validateLicensePlate();
        validateModel();
        validateBrand();
        validateModelYear();
    }
}
```

### 2.2 Interface de Porta (Gateway)
Criamos uma interface `VehicleGateway` no pacote `domain/port` que define o contrato para persistência:

```java
public interface VehicleGateway {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(String licensePlate);
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    Page<Vehicle> findAll(Pageable pageable);
    void delete(Vehicle vehicle);
}
```

**Benefício**: Inversão de dependência - o domínio não depende de nenhum framework.

### 2.3 Repository como Adapter
O `VehicleRepository` agora implementa a interface `VehicleGateway`:

```java
@Repository
public interface VehicleRepository extends VehicleGateway, 
    JpaRepository<Vehicle, String>, JpaSpecificationExecutor<Vehicle> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
}
```

Isso converte o Repository em um **Adapter** que implementa uma **Porta** do domínio.

### 2.4 Services Dependem de Gateway
Todos os Services (CreateVehicleService, UpdateVehicleService, etc.) agora dependem de `VehicleGateway` em vez de `VehicleRepository`:

```java
@Service
public class CreateVehicleService implements CreateVehicleUseCase {
    private final VehicleGateway vehicleGateway; // Abstração, não implementação
    
    @Override
    public String handle(CreateVehicleCommand cmd) {
        vehicleGateway.findByLicensePlate(cmd.licensePlate()).ifPresent(v -> {
            throw new VehicleExceptions.VehicleExists();
        });
        
        // Factory method garante estado válido
        Vehicle vehicle = Vehicle.create(...);
        
        return vehicleGateway.save(vehicle).getLicensePlate();
    }
}
```

### 2.5 DTOs de Resposta
O `VehicleResponse` agora possui método `fromEntity()` para conversão:

```java
public record VehicleResponse(...) {
    public static VehicleResponse fromEntity(Vehicle vehicle) {
        return new VehicleResponse(vehicle);
    }
}
```

Controllers convertem entidades em DTOs antes de retornar.

### 2.6 Exceções Específicas
Expandimos `VehicleExceptions` com todas as validações de negócio:

```java
public class VehicleExceptions {
    public static class LicensePlateNotEmpty extends DomainException { ... }
    public static class InvalidLicensePlate extends DomainException { ... }
    public static class ModelNotEmpty extends DomainException { ... }
    public static class BrandNotEmpty extends DomainException { ... }
    public static class InvalidModelYear extends DomainException { ... }
    public static class ModelYearCannotBeInFuture extends DomainException { ... }
}
```

## 3. Consequências

### 3.1 Benefícios
- ✅ **Inversão de Dependência Completa**: Domínio isolado de frameworks
- ✅ **Rich Domain Model**: Lógica de negócio no lugar certo
- ✅ **Melhor Testabilidade**: Services testáveis sem Spring
- ✅ **Separação de Responsabilidades**: Cada camada com seu propósito
- ✅ **Estado Garantido**: Entidade nunca fica em estado inválido
- ✅ **Padrão Consistente**: Alinhado com feature Client

### 3.2 Desvantagens
- ⚠️ Mais arquivos/classes (VehicleGateway adicionada)
- ⚠️ Ligeiramente mais verboso

### 3.3 Trade-off
O custo de uma estrutura mais complexa é compensado pela testabilidade, manutenibilidade e conformidade com os princípios de Clean Architecture.

## 4. Exemplos de Uso

### 4.1 Criação Segura
```java
// Factory method executa todas as validações
Vehicle vehicle = Vehicle.create("Civic", "ABC1234", "Honda", (short) 2023);
// Lança exceção se alguma validação falhar
```

### 4.2 Teste Unitário Sem Spring
```java
@Test
void shouldCreateValidVehicle() {
    Vehicle vehicle = Vehicle.create("Civic", "ABC1234", "Honda", (short) 2023);
    assertEquals("ABC1234", vehicle.getLicensePlate());
}

@Test
void shouldThrowExceptionForEmptyLicensePlate() {
    assertThrows(VehicleExceptions.LicensePlateNotEmpty.class, 
        () -> Vehicle.create("Civic", "", "Honda", (short) 2023));
}
```

### 4.3 Inversão de Dependência em Testes
```java
@Test
void shouldCreateVehicleUsingMockedGateway() {
    VehicleGateway gateway = mock(VehicleGateway.class);
    CreateVehicleService service = new CreateVehicleService(gateway);
    
    // Teste sem dependência do Spring Data
    service.handle(new CreateVehicleCommand(...));
    
    verify(gateway).save(any(Vehicle.class));
}
```

## 5. Referências
- **004 - Padrão de Exceções Modulares**: Exceções específicas de domínio
- **003 - Lógica de Negócio no Domínio**: Rich Domain Model
- **002 - Padrão de UseCases e Commands**: Services como orquestrador

## 6. Status de Adoção

| Feature | Status | Notas |
|---------|--------|-------|
| Vehicle | ✅ Completo | Implementado |
| Client | ✅ Similar | Padrão de referência |
| Labor | ⏳ Pendente | Próximo a refatorar |
| Order | ⏳ Pendente | Próximo a refatorar |


