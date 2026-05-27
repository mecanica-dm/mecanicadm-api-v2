# Guia Prático: Aplicando Clean Architecture Purista em Features

## 🎯 Objetivo
Refatorar features para seguir **Clean Architecture Purista com DDD**, como feito em Vehicle.

## 📋 Checklist de Refatoração

### Passo 1: Enriquecer a Entidade de Domínio

- [ ] Adicione factory method `create()` estático
- [ ] Adicione método(s) de operação de negócio (ex: `updateInfo()`)
- [ ] Mova todas as validações para a entidade
- [ ] Torne o construtor `protected` (acesso apenas via factory)
- [ ] Adicione comentários javadoc explicando o padrão

**Exemplo**:
```java
public class Entity extends AuditEntity {
    protected Entity() { }
    
    private Entity(...) {
        // ...
        validate();
    }
    
    public static Entity create(...) {
        return new Entity(...);
    }
    
    public void update(...) {
        // ...
        validate();
    }
    
    private void validate() {
        // Validações de negócio
    }
}
```

### Passo 2: Criar Interface de Porta (Gateway)

- [ ] Crie pasta: `core/{feature}/domain/port/`
- [ ] Crie interface: `{Entity}Gateway.java`
- [ ] Defina métodos de persistência
- [ ] Adicione comentários explicando ser uma porta de saída

**Exemplo**:
```java
public interface VehicleGateway {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(String id);
    Page<Vehicle> findAll(Pageable pageable);
    void delete(Vehicle vehicle);
}
```

### Passo 3: Adaptar o Repository

- [ ] Repository agora implementa a interface Gateway
- [ ] Mantenha compatibilidade com Spring Data JPA
- [ ] Adicione comentários explicando ser um adapter

**Exemplo**:
```java
@Repository
public interface VehicleRepository extends VehicleGateway,
    JpaRepository<Vehicle, String>, JpaSpecificationExecutor<Vehicle> {
}
```

### Passo 4: Atualizar Services

Para **cada** service:
- [ ] Substitua `{Entity}Repository` por `{Entity}Gateway` na injeção
- [ ] Remova validações (já estão na entidade)
- [ ] Use factory method: `Entity.create(...)` 
- [ ] Use métodos de operação: `entity.update(...)`
- [ ] Adicione comentários javadoc

**Exemplo**:
```java
@Service
public class CreateVehicleService implements CreateVehicleUseCase {
    private final VehicleGateway gateway; // Gateway, não Repository
    
    public CreateVehicleService(VehicleGateway gateway) {
        this.gateway = gateway;
    }
    
    @Override
    @Transactional
    public String handle(CreateVehicleCommand cmd) {
        // Usa factory method que executa validações
        Vehicle vehicle = Vehicle.create(...);
        return gateway.save(vehicle).getId();
    }
}
```

### Passo 5: Expandir Exceções

- [ ] Revise todas as validações possíveis
- [ ] Crie exceção para cada regra de negócio
- [ ] Todas herdam de `DomainException`
- [ ] Adicione i18n keys

**Exemplo**:
```java
public class VehicleExceptions {
    public static class LicensePlateNotEmpty extends DomainException {
        public LicensePlateNotEmpty() {
            super("vehicle.license.plate.not.empty", HttpStatus.BAD_REQUEST);
        }
    }
}
```

### Passo 6: Atualizar Response DTOs

- [ ] Adicione método estático `fromEntity()`
- [ ] Use em controllers: `dto.fromEntity(entity)`
- [ ] Adicione comentários javadoc

**Exemplo**:
```java
public class VehicleResponse {
    public static VehicleResponse fromEntity(Vehicle vehicle) {
        return new VehicleResponse(vehicle);
    }
}
```

### Passo 7: Atualizar Controllers

- [ ] Converta entidades em DTOs: `VehicleResponse.fromEntity(vehicle)`
- [ ] Não retorne entidades de domínio diretamente
- [ ] Adicione comentários javadoc na classe

**Exemplo**:
```java
@GetMapping("/{id}")
public ResponseEntity<VehicleResponse> get(@PathVariable String id) {
    Vehicle vehicle = useCase.handle(...);
    return ResponseEntity.ok(VehicleResponse.fromEntity(vehicle));
}
```

### Passo 8: Atualizar Testes

- [ ] Atualize imports de Repository para Gateway
- [ ] Mocke Gateway em vez de Repository
- [ ] Teste factory method sem Spring
- [ ] Teste validações na entidade

**Exemplo**:
```java
@Test
void shouldCreateWithValidation() {
    // Teste sem Spring
    Vehicle v = Vehicle.create("Model", "123", "Brand", (short) 2023);
    assertEquals("123", v.getLicensePlate());
}

@Test
void shouldThrowOnInvalidData() {
    assertThrows(VehicleExceptions.class,
        () -> Vehicle.create("", "", "", null));
}
```

---

## 🔄 Ordem de Refatoração Recomendada

1. **Vehicle** ✅ (Já feito)
2. **Labor** (Próximo - similar a Vehicle)
3. **Order** (Depois de Labor)
4. **Serviços Transversais** (TokenService, etc.)

---

## 📊 Estrutura de Pastas Esperada

```
core/
├── vehicle/
│   ├── adapter/
│   │   ├── api/
│   │   │   ├── VehicleController.java
│   │   │   ├── dto/
│   │   │   │   └── VehicleResponse.java
│   │   │   └── openapi/
│   │   │       └── VehicleOpenApi.java
│   │   └── repository/
│   │       └── VehicleRepository.java
│   ├── domain/
│   │   ├── Vehicle.java           ← Rich Domain Model
│   │   └── port/
│   │       └── VehicleGateway.java ← Porta de saída
│   ├── exception/
│   │   └── VehicleExceptions.java
│   ├── service/
│   │   ├── CreateVehicleService.java
│   │   ├── UpdateVehicleService.java
│   │   ├── DeleteVehicleService.java
│   │   ├── GetVehicleByIdService.java
│   │   └── GetAllVehicleService.java
│   └── usecase/
│       ├── CreateVehicleUseCase.java
│       ├── UpdateVehicleUseCase.java
│       ├── DeleteVehicleUseCase.java
│       ├── GetVehicleByIdUseCase.java
│       ├── GetAllVehicleUseCase.java
│       ├── command/
│       │   ├── CreateVehicleCommand.java
│       │   ├── UpdateVehicleCommand.java
│       │   └── DeleteVehicleCommand.java
│       └── query/
│           ├── GetVehicleByIdQuery.java
│           └── GetAllVehiclesQuery.java
```

---

## 💡 Dicas Importantes

### ✅ Faça:
- Use **factory methods** para criar entidades
- Mantenha **validações na entidade**
- Dependa de **abstrações (interfaces)** não implementações
- Crie **DTOs** para respostas HTTP
- Adicione **comentários javadoc** explicando o padrão
- Use **métodos privados** para validações
- Execute **validações no construtor**

### ❌ Não Faça:
- Não exponha **setters públicos** na entidade
- Não coloque **lógica de negócio** no Service
- Não dependa **diretamente de JpaRepository**
- Não retorne **entidades de domínio** do controller
- Não crie **construtores públicos** além da factory
- Não misture **responsabilidades** de camadas

---

## 🧪 Template de Teste para Rich Domain Model

```java
public class VehicleTest {
    
    @Test
    void shouldCreateValidVehicle() {
        Vehicle v = Vehicle.create("Civic", "ABC1234", "Honda", (short) 2023);
        assertEquals("ABC1234", v.getLicensePlate());
    }
    
    @Test
    void shouldThrowExceptionForInvalidLicensePlate() {
        assertThrows(VehicleExceptions.InvalidLicensePlate.class,
            () -> Vehicle.create("Civic", "ABC", "Honda", (short) 2023));
    }
    
    @Test
    void shouldUpdateWithValidation() {
        Vehicle v = Vehicle.create("Civic", "ABC1234", "Honda", (short) 2023);
        v.updateInfo("Accord", "Honda", (short) 2023);
        assertEquals("Accord", v.getModel());
    }
    
    @Test
    void shouldThrowExceptionOnUpdateWithInvalidData() {
        Vehicle v = Vehicle.create("Civic", "ABC1234", "Honda", (short) 2023);
        assertThrows(VehicleExceptions.ModelNotEmpty.class,
            () -> v.updateInfo("", "Honda", (short) 2023));
    }
}
```

---

## 🔗 Referências ADR

- **002**: Padrão de UseCases e Commands
- **003**: Lógica de Negócio no Domínio (Rich Domain Model)
- **004**: Padrão de Exceções Modulares
- **011**: Clean Architecture Purista no Vehicle

---

## ❓ Perguntas Frequentes

**P: Por que precisamos de uma Gateway se o Repository já faz tudo?**
R: A Gateway é uma abstração que permite que o domínio não dependa de frameworks. Repository é a implementação da abstração.

**P: O factory method é obrigatório?**
R: Sim, garante que a entidade sempre esteja em estado válido e centraliza a lógica de criação.

**P: Posso ter múltiplas factories?**
R: Sim! Exemplo: `Vehicle.createNew()` e `Vehicle.recreateFromDatabase()` com validações diferentes.

**P: Validações sempre devem estar na entidade?**
R: Sim, se forem regras de domínio. Validações técnicas (ex: campos do HTTP) podem estar em Command validators.

---

## 📞 Suporte

Dúvidas? Consulte o arquivo `/docs/adr/011-clean_arch_purista_vehicle.md` ou veja o exemplo completo em Vehicle!

