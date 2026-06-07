# Biblioteca Digital - Books API - Automação de Testes

Projeto de automação de testes para a API de gerenciamento de livros da Biblioteca Digital (FakeRestAPI).

## O que este projeto faz?

Este projeto executa testes automatizados contra a API de livros, validando se ela funciona corretamente. São 28 testes cobrindo criação, consulta, atualização e exclusão de livros, além de testes de segurança, performance e integração entre módulos.

## Conteúdo do Repositório

```
prova-tecnica-t2mlab/
├── docs/                        # Documentação do projeto
│   ├── 01-requisitos/           # Análise de requisitos e estratégia de testes
│   │   ├── Analise-Critica-dos-Requisitos.pdf
│   │   ├── Estrategia-de-Testes.pdf
│   │   └── Analise-de-Impacto-e-Integracoes.pdf
│   ├── 02-planejamento/         # Plano de testes e cenários BDD
│   │   ├── Cenarios-Gherkin.pdf
│   │   └── Plano-de-Testes.xlsx
│   ├── 03-postman/              # Collection e Environment do Postman
│   │   ├── Books-API.postman_collection.json
│   │   └── BibliotecaDigital.postman_environment.json
│   └── 04-referencia/           # Documento original da prova técnica
│       └── Prova-Tecnica-QA-Pleno.pdf
├── src/test/java/               # Código dos testes automatizados
│   └── com/bibliotecadigital/
│       ├── clients/             # Chamadas para a API (Client Layer)
│       ├── builders/            # Montagem dos dados enviados (Builder Pattern)
│       ├── fixtures/            # Dados de teste prontos para reutilização
│       ├── schemas/             # Validação da estrutura dos dados da API
│       ├── tests/               # Os testes em si
│       └── utils/               # Configurações e utilitários
├── src/test/resources/          # Arquivos de suporte aos testes
│   ├── environments/            # Configurações de ambiente (URL, timeouts)
│   ├── fixtures/                # Dados em formato JSON
│   └── schemas/                 # Schemas para validação de contrato
├── .github/workflows/           # Pipeline de CI/CD (GitHub Actions)
│   └── tests.yml
├── .mvn/wrapper/                # Maven Wrapper (executa Maven sem instalar)
├── pom.xml                      # Dependências e configuração do projeto
├── mvnw                         # Script para rodar Maven no Linux/Mac
├── mvnw.cmd                     # Script para rodar Maven no Windows
└── README.md                    # Este arquivo
```

### Descrição de cada pasta

| Pasta | O que contém |
|-------|--------------|
| `docs/01-requisitos` | Análise crítica dos requisitos, estratégia de testes e análise de impacto entre os módulos da API |
| `docs/02-planejamento` | Cenários de teste escritos em formato Gherkin (BDD) e a planilha com o plano de testes completo |
| `docs/03-postman` | Arquivos para importar no Postman e executar os testes manualmente |
| `docs/04-referencia` | O enunciado original da prova técnica |
| `src/test/java/.../clients` | Classes que encapsulam as chamadas HTTP para cada recurso da API |
| `src/test/java/.../builders` | Classe que monta os payloads (dados JSON) usando o padrão Builder |
| `src/test/java/.../fixtures` | Dados de teste pré-definidos e reutilizáveis em vários cenários |
| `src/test/java/.../tests` | Classes de teste JUnit 5, uma para cada grupo de cenários |
| `src/test/java/.../utils` | Classe que carrega as configurações de ambiente (URL, timeouts) |
| `src/test/resources/schemas` | Arquivos JSON Schema usados para validar o contrato da API |
| `.github/workflows` | Configuração do pipeline que roda os testes automaticamente no GitHub |

## Pré-requisitos

Para rodar este projeto, você precisa ter instalado na sua máquina:

- **Java 21 (JDK)** — é a linguagem usada para escrever os testes
- **Maven 3.9+** — é a ferramenta que gerencia as dependências e executa os testes (opcional se usar o Maven Wrapper incluso)
- **Git** — para baixar o código do repositório

## Instalação

Abra o terminal (Prompt de Comando ou PowerShell no Windows, Terminal no Mac/Linux) e execute:

```bash
git clone <url-do-repositorio>
cd prova-tecnica-t2mlab
mvn dependency:resolve
```

O último comando baixa todas as bibliotecas necessárias para o projeto funcionar.

## Execução

**Importante:** Você precisa estar dentro da pasta do projeto para os comandos funcionarem.

### Passo a passo

1. Abra o **PowerShell** (no Windows, pressione `Win + R`, digite `powershell` e pressione Enter)

2. Navegue até a pasta do projeto:

```bash
cd c:\caminho\para\prova-tecnica-t2mlab
```

3. Execute os testes:

```bash
.\mvnw.cmd clean test
```

> **Nota:** O projeto inclui um Maven Wrapper (`mvnw.cmd`) que baixa o Maven automaticamente. Você só precisa ter o **Java** instalado. Não precisa instalar o Maven separadamente.

> **No Linux/Mac**, o comando equivalente é: `./mvnw clean test`

> **Se você já tem o Maven instalado** e disponível no terminal, pode usar diretamente: `mvn clean test`

### Como saber se funcionou?

Ao final da execução, o terminal mostrará um resumo como este:

```
[INFO] Tests run: 28, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

- **Tests run: 28** = quantidade de testes executados
- **Failures: 0** = nenhum teste falhou
- **BUILD SUCCESS** = tudo rodou com sucesso

## Estrutura do Projeto

Abaixo está a organização das pastas e arquivos. Cada pasta tem uma responsabilidade:

```
src/test/java/com/bibliotecadigital/
├── clients/          # Responsável por fazer as chamadas para a API
│   ├── BooksClient.java
│   ├── AuthorsClient.java
│   └── CoverPhotosClient.java
├── builders/         # Monta os dados (JSON) enviados para a API
│   └── BookBuilder.java
├── fixtures/         # Dados de teste prontos para reutilização
│   └── BookFixtures.java
├── schemas/          # Validação da estrutura dos dados retornados pela API
├── tests/            # Os testes em si, organizados por funcionalidade
│   ├── GetBooksTest.java          (listar livros)
│   ├── GetBookByIdTest.java       (buscar livro por ID)
│   ├── PostBooksTest.java         (criar livros)
│   ├── PutBooksTest.java          (atualizar livros)
│   ├── DeleteBooksTest.java       (excluir livros)
│   ├── SecurityTest.java          (segurança: SQL Injection e XSS)
│   ├── IntegrationTest.java       (integração entre módulos)
│   ├── E2ETest.java               (fluxo completo ponta a ponta)
│   ├── ParameterizedBooksTest.java (mesmo teste com vários dados)
│   └── ConcurrencyTest.java       (várias requisições ao mesmo tempo)
└── utils/            # Configurações e utilitários gerais
    └── Environment.java

src/test/resources/
├── environments/     # Configurações do ambiente (URL da API, etc.)
│   └── environment.properties
├── fixtures/         # Arquivos JSON com dados de teste
└── schemas/          # Arquivos que definem a estrutura esperada da API
    ├── book-schema.json
    └── books-list-schema.json
```

## Relatórios Allure

O Allure gera relatórios visuais (em HTML) mostrando o resultado de cada teste com detalhes.

Para gerar e abrir o relatório no navegador:

```bash
mvn clean test
allure serve target/allure-results
```

Para instalar o Allure CLI no Windows (via Scoop):

```bash
scoop install allure
```

O relatório abre automaticamente no navegador e mostra cada teste com status (passou/falhou), tempo de execução e detalhes da requisição.

## Pipeline GitHub Actions

O projeto possui uma automação configurada no GitHub que executa os testes automaticamente sempre que:

- Alguém envia código para a branch `main` ou `master`
- Alguém abre um Pull Request
- Alguém dispara manualmente pela interface do GitHub

O que o pipeline faz, passo a passo:

1. Baixa o código do repositório
2. Instala o Java 21
3. Executa todos os testes (`mvn clean test`)
4. Salva os resultados como artefato para download
5. Gera e publica o relatório Allure no GitHub Pages

O arquivo de configuração está em `.github/workflows/tests.yml`.

## Como Adicionar Novos Testes

1. **Criar dados de teste** (se necessário): adicione um novo método em `BookFixtures.java` com os dados que o teste precisa.

2. **Criar o teste**: crie um novo método em uma classe existente dentro de `tests/`, ou crie uma classe nova se for uma funcionalidade diferente.

3. **Padrão a seguir**:
   - Use `BooksClient` para fazer as chamadas à API.
   - Use `BookBuilder` para montar os dados enviados.
   - Use `BookFixtures` para reaproveitar dados de teste.
   - Anote com `@Test`, `@DisplayName` (nome curto) e `@Description` (descrição detalhada).
   - Anote a classe com `@Epic` e `@Feature` para organização no relatório.

4. **Validar**: rode `mvn clean test` e confirme que o novo teste aparece e passa.

Exemplo:

```java
@Test
@DisplayName("Novo teste - Descrição curta")
@Description("Descrição detalhada do cenário")
void shouldDoSomething() {
    var payload = BookFixtures.validBook();
    Response response = booksClient.createBook(payload);
    response.then().statusCode(200);
}
```
