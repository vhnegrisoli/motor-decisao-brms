# Motor de Decisão - BRMS

API que implementa um motor de decisão de regra de negócios baseada em um BRMS com Java 11, Spring Boot, MongoDB, Swagger e Docker

### Objetivos

O objetivo é implementar uma lógica de uma aplicação BRMS (Business Rule Management System), ou seja, que possua
um sistema para implementar, gerenciar e executar regras de negócio de maneira a ficar com fácil compreensão e manutenção.


### Tecnologias

* Java 11
* Spring Boot
* API REST
* MongoDB
* Docker
* JUnit 5
* AssertJ
* Swagger
* Spring Cloud OpenFeign (API de validação de CEP - ViaCep)
* Spring Data JPA
* Spring Data
* RestTemplate (chamadas REST entre microsserviços)
* RabbitMQ
* PostgreSQL
* Node.js
* Express.js
* Axios

## Arquitetura do sistema

Serão 6 serviços:

* motor-decisao-api: API que realiza a tomada de decisão das regras para os produtos. Java 11, Spring Boot, Spring Data MongoDB, MongoDB, RabbitMQ, RestTemplate.
* service-wrapper: API wrapper que irá receber uma solicitação de algum serviço necessário para uma regra e irá redirecionar a consulta. Node.js 14, Express.js, Axios.
* cpf-limpo-api: API que irá validar se um CPF é válido e está limpo. Java 11, Spring Boot, Spring Data JPA, PostgreSQL, RabbitMQ.
* data-valida-api: API que irá validar uma data de nascimento e calcular a idade. Java 11, Spring Boot, RabbitMQ.
* cep-valido-api: API que irá validar um CEP na API do ViaCep. Java 11, Spring Boot, Spring Cloud OpenFeign, RabbitMQ.
* log-api: API que terá apenas um listener do RabbitMQ e que receberá os logs de todos os serviços, possibilitando vários filtros de consulta. Java 11, Spring Boot, Spring Data MongoDB, MongoDB, RabbitMQ.

![Arquitetura]()

### Workflow de decisão

![Workflow](https://github.com/vhnegrisoli/motor-decisao-brms/blob/master/Decision%20Engine%20Worflow.png)

### Possibilidade de habilitar/desabilitar regras por produtos

Na classe **RuleDefinition** (br.com.decisao.motordecisao.config.rule.RuleDefinition) é possível
adicionar uma regra para um produto definindo se está habilitada ou desabilitada no método **getRules()**.

```java
public static List<RuleExecution> getRules() {
    return List.of(
        define(ALUGUEL, REGRA_AVALIAR_CPF_VALIDO, HABILITADA),
        define(COMPRA, REGRA_AVALIAR_CPF_VALIDO, HABILITADA),
        define(ALUGUEL, REGRA_AVALIAR_CPF_LIMPO, HABILITADA),
        define(ALUGUEL, REGRA_AVALIAR_IDADE_PERMITIDA, HABILITADA)
    );
}
```

Para criar uma nova regra, adicione um novo ID para ela no enum **RuleId** e adicione na lista acima.

### Orquestração das regras

A orquestração fica na classe **EngineOrchestrationService** no método **evaluateRules**, como pode ser visto abaixo:

```java
private void evaluateRules(PayloadProduct payloadProduto) {
    var keepRunning = true;
    var rules = payloadProduto.getProduto().getRegras();
    if (shouldEvaluate(keepRunning, REGRA_AVALIAR_CPF_VALIDO, rules, payloadProduto)) {
        keepRunning = executeAndDefineNext(REGRA_AVALIAR_CPF_VALIDO, payloadProduto);
    }
    if (shouldEvaluate(keepRunning, REGRA_AVALIAR_CPF_LIMPO, rules, payloadProduto)) {
        keepRunning = executeAndDefineNext(REGRA_AVALIAR_CPF_LIMPO, payloadProduto);
    }
    if (shouldEvaluate(keepRunning, REGRA_AVALIAR_IDADE_PERMITIDA, rules, payloadProduto)) {
        keepRunning = executeAndDefineNext(REGRA_AVALIAR_IDADE_PERMITIDA, payloadProduto);
    }
}
```

Ao criar uma nova regra (anotar com **@Component** cada classe de regra), é necessário incluir 
na service responsável por gerir a execução das regras, em **RuleExecutorService**, na seguinte estrutura:


Obs.: no momento apenas uma regra foi desenvolvida, para verificar se o CPF do cliente é válido:

```java
@Service
public class RuleExecutorService {

    @Autowired
    private RegraCpfValido regraCpfValido;

    public Rule executeRule(RuleId ruleId,
                            PayloadProduct payloadProduct) {
        switch (ruleId) {
            case REGRA_AVALIAR_CPF_VALIDO:
                return regraCpfValido.avaliarRegra(payloadProduct);
            case REGRA_AVALIAR_CPF_LIMPO:
            case REGRA_AVALIAR_IDADE_PERMITIDA:
            default:
                return new Rule();
        }
    }
}
```

São 4 condições para avaliar uma regra:

1 - É necessário que a variável booleana **keepRunning** seja **true**.
2 - É necessário que o ID da regra a ser avaliada esteja disponível para o produto da iteração atual
3 - É necessário que o ID da regra a ser avaliada esteja habilitada para o produto da iteração atual
4 - É necessário que o ID da regra a ser avaliada para o produto da iteração atual não exista na lista de regras do produto, caso contrário,
entende-se que ela já foi avaliada.

Após a avaliação da regra, é chamado o método **rule.isApproved()**, que apenas 
retorna um valor booleano caso o status da regra que foi avaliada está como APROVADA, e esse
valor booleano é definido à variável keepRunning. Ou seja, se uma regra reprova, a variável
keepRunning fica false, e para toda a execução das próximas regras.

### Descrição dos dados da tomada de decisão

As regras de negócios possuem **IDs**, **descrições** e **status**, e cada produto avaliado possui uma lista de regras avaliadas.

```json
[
  {
    "id": "REGRA_AVALIAR_CPF_VALIDO",
    "status": "APROVADA",
    "description": "O CPF do cliente está válido."
  }
]
```

A implementação das regras de negócios ficam no diretório:

```
motordecisao
  |--- modules
    |--- rules
       |--- rule
         |--- RegraCpfValido
```

Existe uma lista chamada **consultedApis** que armazenam informações das APIs que foram consultadas, no seguinte formato:

```json
[
  {
    "id": "CPF",
    "reason": "",
    "status": 200,
    "success": true
  }
]
```

Quando há falhas:

```json
[
  {
    "id": "CPF",
    "reason": "Não foi encontrado dados para este CPF.",
    "status": 400,
    "success": false
  }
]
```

Já os dados retornados por essa API ficam no objeto **apiData**:

```json
{
  "apiData": {
    "cpfService": {
      "cpf": null,
      "validCpf": false
    }
  }
}
```

Cada objeto existente dentro de apiData é um objeto que mapeia os dados de retorno de alguma API, portanto, não possuem um 
formato padrão no JSON.

Os dados de entrada da pessoa a ser analisada fica em **person**:

```json
{
  "person": {
    "document": "97552035080",
    "birthday": "1998-03-31"
  }
}
```

E, claro, por fim, após executado o motor, temos algumas estatísticas referentes ao tempo de execução
da avaliação pelo motor inteiro, temos mapeado o tempo inicial, final e total da avaliaçã.

```json
{
  "evaluationStart": "2021-07-30T12:14:14.406903",
  "evaluationFinish": "2021-07-30T12:14:14.409528",
  "totalTime": 2
}
```

### Entendendo as entradas e saídas

A entrada totalmente limpa é um payload no seguinte formato:

```json
{
  "person": {
    "birthday": "1998-03-31",
    "document": "97552035080"
  },
  "products": [
    {
      "id": "ALUGUEL"
    }
  ]
}
```

A resposta será:

```json
{
  "id": "6103e9f634f8b037d7d7b9db",
  "engineId": "f289be04-9e61-42e4-813c-44fd58d12082",
  "products": [
    {
      "id": "ALUGUEL",
      "rules": [
        {
          "id": "REGRA_AVALIAR_CPF_VALIDO",
          "status": "APROVADA",
          "description": "O CPF do cliente está válido."
        },
        {
          "id": "REGRA_AVALIAR_CPF_VALIDO",
          "status": "API_PENDENTE",
          "description": "O serviço de CPF ainda não foi consultado."
        }
      ]
    }
  ],
  "disapprovedProducts": [],
  "person": {
    "document": "97552035080",
    "birthday": "1998-03-31"
  },
  "consultedApis": [
    {
      "id": "CPF",
      "status": 200,
      "success": true,
      "reason": null
    }
  ],
  "apiData": {
    "cpfService": {
      "cpf": "10332458954",
      "validCpf": true
    }
  },
  "evaluationStart": "2021-07-30T12:00:54.195544",
  "evaluationFinish": "2021-07-30T12:00:54.203128",
  "totalTime": 7
}
```

Caso você queira testar outros resultados, basta apenas informar dados da saída na entrada, por exemplo,
ao informar que o resultado da consulta à API de CPF deu falha (consultedApis#0.success=false), teremos uma regra reprovada:

Entrada já informando o resultado da consulta à API de CPF como reprovada:

```json
{
  "consultedApis": [
    {
      "id": "CPF",
      "status": 200,
      "success": false <-----
    }
  ],
  "person": {
    "birthday": "1998-03-31",
    "document": "97552035080"
  },
  "products": [
    {
      "id": "ALUGUEL"
    }
  ]
}
```

Resposta:

```json
{
  "id": "6103eaaf34f8b037d7d7b9dc",
  "engineId": "af8dd10b-800f-4e56-947e-9304cce2ee0c",
  "products": [],
  "disapprovedProducts": [
    {
      "id": "ALUGUEL",
      "rules": [
        {
          "id": "REGRA_AVALIAR_CPF_VALIDO",
          "status": "REPROVADA",
          "description": "O serviço de CPF falhou na consulta."
        }
      ]
    }
  ],
  "person": {
    "document": "97552035080",
    "birthday": "1998-03-31"
  },
  "consultedApis": [
    {
      "id": "CPF",
      "status": 200,
      "success": false,
      "reason": null
    }
  ],
  "apiData": {
    "cpfService": {
      "cpf": null,
      "validCpf": false
    }
  },
  "evaluationStart": "2021-07-30T12:03:59.697998",
  "evaluationFinish": "2021-07-30T12:03:59.69831",
  "totalTime": 0
}
```

Sempre que um produto for reprovado, ele sairá na lista **disapprovedProducts**, caso contrário, sairá em **products**.

A mesma lógica pode ser feita com a lista de **rules** de cada produto, caso uma regra seja REPROVADA, e você envie na entrada
essa mesma regra como APROVADA, o motor entenderá que essa política já foi avaliada, e que está aprovada, apenas partindo para a próxima avaliação.

Essa estrutura permite que dados de APIs sejam consultados e regras avaliadas, e, em caso de
necessidade de troubleshooting, é possível simular pela entrada todo o processamento interno do motor para
testar diversos cenários, como mostrado acima manipulando o resultado da consulta da API para 
gerar outro status para uma regra.

### Execução

É possível executar localmente ou via **docker-compose**.

#### Executando localmente

Para executar localmente, é necessário uma instância do MongoDB rodando, e basta dar um build no projeto com:

`gradle build`

E em sequência:

`gradle bootRun`

#### Executando via docker-compose

Basta apenas executar o comando abaixo:

`docker-compose up --build`

Caso não queira que os logs fiquem no seu terminal, adicione a flag `-d` ao final do comando acima.

### Documentação

Toda a documentação está presente no Swagger.

Hoje, a API conta com 3 endpoints, um POST para salvar uma avaliação, e dois
GET para recuperar tanto pelo ID gerado na avaliação, quanto o ID gerado pelo MongoDB.

A documentação localiza-se em:

http://localhost:8080/swagger-ui.html

Porém, apenas ao acessar a URL base da API (http://localhost:8080) você já será redirecionado à documentação.

### Testes unitários

O projeto conta com testes unitários para cada arquivo Java que contém métodos que executam e processam algo,
seja uma função void ou uma função que retorne um valor.

Os testes podem ser executados com:

`gradle build` ou apenas `gradle test`.

### Autor

#### Victor Hugo Negrisoli
#### Desenvolvedor de Software Back-End
