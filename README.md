![Badge de Build passando ou não](https://github.com/guibgoulart/desafio-votacao/actions/workflows/gradle.yml/badge.svg)

# Projeto Cooperativa de Votação

Este projeto é uma aplicação Spring Boot desenvolvida em Java 21, que utiliza Gradle para gerenciamento de dependências e Docker para a criação de um ambiente de execução consistente. O desenvolvimento foi guiado pela metodologia Test-Driven Development (TDD), com todos os testes unitários relevantes criados antes das funcionalidades e executados com sucesso. A aplicação permite gerenciar e participar de sessões de votação de uma cooperativa, onde cada associado possui um voto e as decisões são tomadas em assembleias.

## Tecnologias Utilizadas

- Java 21
- Spring Boot
- Gradle
- Docker
- MySQL
- JUnit
- Mockito


## Pré-requisitos

Para executar este projeto, é necessário ter instalado:

- JDK 21 ou superior
- Docker

## Como Executar o Projeto Localmente

Siga as instruções abaixo para executar o projeto localmente:

1. Clone o repositório para a sua máquina local.
2. Navegue até o diretório do projeto.
3. Execute o comando `./gradlew build` para compilar o projeto e gerar o arquivo JAR.
4. Após a compilação bem-sucedida, com o Docker executando em sua máquina, execute o comando `docker-compose up --build` para iniciar os contêineres Docker.

Este comando iniciará os contêineres para a aplicação e para o banco de dados MySQL. A aplicação estará disponível na porta 8080 do seu localhost, e o banco de dados MySQL estará acessível na porta 3306.

## Funcionalidades da Aplicação

A aplicação oferece as seguintes funcionalidades por meio de uma API REST:

- **Cadastrar uma nova pauta:** Permite que os usuários registrem tópicos para votação.
- **Abrir uma sessão de votação em uma pauta:** A sessão de votação pode ser aberta por um tempo determinado na chamada de abertura ou, por padrão, por 1 minuto.
- **Receber votos dos associados em pautas:** Os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um ID único e pode votar apenas uma vez por pauta.
- **Contabilizar os votos e dar o resultado da votação na pauta.**