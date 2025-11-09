# CareLink API

API RESTful para um sistema de gestão de saúde, desenvolvida com Quarkus, com foco em agendamento e acompanhamento de pacientes.

## Visão Geral

A **CareLink API** fornece um backend robusto para gerenciar o fluxo de agendamentos, desde a criação (via importação de planilhas) até a consulta e modificação, além de manter um registro detalhado do histórico e das interações com os pacientes.

## Funcionalidades Principais

-   **Gestão de Agendamentos:**
    -   Importação de agendamentos em lote a partir de planilhas.
    -   Consulta de agendamentos por período.
    -   Atualização de status e detalhes de consultas.
-   **Acompanhamento do Paciente:**
    -   Consulta do histórico completo de um paciente.
    -   Criação e alteração de anotações manuais para registrar observações.
-   **Alertas do Sistema:**
    -   Endpoint para obter os alertas (consultas e tarefas) agendados para o dia atual.

## Tabela de Endpoints da API

| Verbo HTTP | URI (Caminho do Recurso) | Descrição | Códigos de Status Esperados |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/upload/receber` | Envia um arquivo de planilha para processamento e retorna os dados extraídos. | **200** (OK), **400** (Requisição Inválida), **500** (Erro Interno) |
| **POST** | `/api/upload/salvar` | Salva no banco de dados a lista de agendamentos extraída da planilha. | **200** (OK), **500** (Erro Interno) |
| **GET** | `/api/alertas/hoje` | Obtém a lista de alertas agendados para o dia atual. | **200** (OK), **500** (Erro Interno) |
| **GET** | `/agendamentos` | Busca agendamentos em um período. Requer `?dataInicio=...` e `?dataFim=...`. | **200** (OK), **400** (Parâmetros Inválidos), **500** (Erro Interno) |
| **PUT** | `/agendamentos/{idConsulta}` | Atualiza os dados de uma consulta específica. | **200** (OK), **404** (Não Encontrado), **500** (Erro Interno) |
| **POST** | `/api/anotacoes` | Cria uma nova anotação manual. | **200** (OK), **400** (Requisição Inválida), **500** (Erro Interno) |
| **PUT** | `/api/anotacoes` | Altera uma anotação manual existente. | **200** (OK), **404** (Não Encontrado), **500** (Erro Interno) |
| **GET** | `/api/paciente/{idPaciente}/historico` | Obtém o histórico completo de um paciente. | **200** (OK), **404** (Não Encontrado), **500** (Erro Interno) |

## Tecnologias Utilizadas

-   [Quarkus](https://quarkus.io/)
-   Java
-   Jakarta REST (JAX-RS)
-   Hibernate ORM com Panache
-   JDBC Driver - Oracle
-   Maven

## Executando a Aplicação em Modo de Desenvolvimento

Você pode executar a aplicação em modo de desenvolvimento, que ativa o live coding, usando:

```shell script
./mvnw quarkus:dev
```

> **_NOTA:_** O Quarkus inclui uma Dev UI, disponível apenas no modo de desenvolvimento em <http://localhost:8080/q/dev/>.

## Empacotando e Executando a Aplicação

A aplicação pode ser empacotada usando:

```shell script
./mvnw package
```

Isso produz o arquivo `quarkus-run.jar` no diretório `target/quarkus-app/`. As dependências são copiadas para o diretório `target/quarkus-app/lib/`.

A aplicação pode ser executada com:

```shell
java -jar target/quarkus-app/quarkus-run.jar
```

Para construir um _über-jar_, execute o seguinte comando:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

A aplicação, empacotada como um _über-jar_, pode ser executada com `java -jar target/*-runner.jar`.

## Criando um Executável Nativo

Você pode criar um executável nativo usando:

```shell script
./mvnw package -Dnative
```

Ou, se você não tiver o GraalVM instalado, pode construir o executável nativo em um contêiner usando:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

Você pode então executar seu executável nativo com: `./target/carelink-api-1.0.0-SNAPSHOT-runner`
