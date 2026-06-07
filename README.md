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
4. Salva os resultados do Allure como artefato para download

### Como ver os resultados no GitHub

Após a execução do pipeline, os resultados ficam disponíveis na aba **Actions** do repositório. Clique na execução desejada e baixe o artefato **allure-results**. Para visualizar o relatório, extraia o arquivo e rode localmente:

```bash
allure serve caminho/para/allure-results
```

O arquivo de configuração do pipeline está em `.github/workflows/tests.yml`.

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

## Observações Importantes sobre os Resultados

Todos os 28 testes passam com sucesso, mas isso **não significa que a API está perfeita**. Durante a execução dos testes, foram identificados diversos comportamentos que em uma API real seriam considerados problemas. Os testes foram construídos para **documentar o comportamento observado** e não simplesmente para "forçar" que tudo passe.

### Como os cenários estão sendo validados?

Cada teste verifica o que a API realmente faz (comportamento observado) e aceita como válido o resultado retornado **ou** o resultado ideal. Por exemplo: se um teste espera status 404 mas a API retorna 200, o teste aceita ambos e registra uma observação de que o comportamento da API difere do esperado em uma aplicação real.

Essa abordagem foi adotada porque a API utilizada (FakeRestAPI) é uma API pública de simulação que não implementa todas as regras de negócio de um sistema real. Os testes passam porque estão validando corretamente o comportamento real da API, e ao mesmo tempo deixam documentado o que deveria ser diferente.

### Observações identificadas durante os testes

#### Exclusão de livros (DeleteBooksTest)

| O que foi testado | O que a API faz | O que deveria fazer em uma API real |
|---|---|---|
| Excluir um livro que já foi removido | Retorna status 200 (sucesso) | Deveria retornar status 404 (não encontrado) |

**Por que o teste passa?** O teste aceita tanto 200 quanto 404 como resposta válida, e registra que a API não diferencia entre um recurso existente e um já removido.

---

#### Fluxo completo E2E (E2ETest)

| O que foi testado | O que a API faz | O que deveria fazer em uma API real |
|---|---|---|
| Criar um livro e depois consultá-lo via GET | A API não persiste os dados criados. O livro criado via POST não fica disponível para consulta posterior | Deveria armazenar o livro e retorná-lo em consultas futuras |
| Excluir um livro e depois consultar via GET | Após exclusão, o livro ainda pode ser encontrado via GET | Deveria retornar status 404 após a exclusão |

**Por que o teste passa?** A criação é validada pela resposta do próprio POST (que retorna os dados corretamente). Para o GET após exclusão, o teste aceita tanto 200 quanto 404, documentando que a FakeRestAPI não persiste alterações.

---

#### Acesso indevido por ID (GetBookByIdTest)

| O que foi testado | O que a API faz | O que deveria fazer em uma API real |
|---|---|---|
| Acessar livros de outros usuários manipulando o ID na URL | Retorna os dados de qualquer livro, independente de quem está acessando | Deveria verificar se o usuário tem permissão para acessar aquele recurso específico |

**Por que o teste passa?** O teste verifica que a API responde com status 200 para qualquer ID válido, e documenta que não existe controle de acesso. Em um sistema real, isso seria uma vulnerabilidade chamada IDOR (Insecure Direct Object Reference).

---

#### Integração entre módulos (IntegrationTest)

| O que foi testado | O que a API faz | O que deveria fazer em uma API real |
|---|---|---|
| Criar um livro associado a um autor que não existe | Aceita a criação normalmente, sem verificar se o autor existe | Deveria rejeitar com status 400 ou 422, informando que o autor não foi encontrado |
| Excluir um livro que possui capa associada | Exclui o livro, mas a capa permanece no sistema sem vínculo (registro órfão) | Deveria excluir a capa junto (cascade delete) ou impedir a exclusão enquanto houver dados vinculados |

**Por que o teste passa?** Os testes aceitam o comportamento observado (criação sem validação e exclusão sem cascade) e documentam que a API não implementa validação de integridade referencial entre módulos.

---

#### Criação de livros com dados incompletos (PostBooksTest)

| O que foi testado | O que a API faz | O que deveria fazer em uma API real |
|---|---|---|
| Enviar um livro sem o campo "title" (título) | Aceita e cria o livro mesmo sem o título | Deveria rejeitar com status 400, informando que o campo é obrigatório |

**Por que o teste passa?** O teste aceita tanto 200 quanto 400 como resposta, e registra que a API não valida campos obrigatórios. Em um sistema real, isso permitiria cadastrar livros com informações incompletas.

---

#### Atualização de livros inexistentes (PutBooksTest)

| O que foi testado | O que a API faz | O que deveria fazer em uma API real |
|---|---|---|
| Enviar uma atualização para um livro com ID que não existe | Retorna status 200 (sucesso) | Deveria retornar status 404 (não encontrado) |

**Por que o teste passa?** O teste aceita tanto 200 quanto 404, e documenta que a API não verifica se o recurso existe antes de "atualizar".

---

#### Segurança - SQL Injection e XSS (SecurityTest)

| O que foi testado | O que a API faz | O que deveria fazer em uma API real |
|---|---|---|
| Enviar um comando SQL malicioso no campo título (`' OR '1'='1`) | Aceita e armazena o texto literalmente, sem executar como comando SQL | Comportamento correto — o texto é tratado apenas como texto |
| Enviar um script malicioso no campo título (`<script>alert('xss')</script>`) | Aceita e armazena o script como texto, sem rejeitá-lo | Deveria sanitizar (limpar) o conteúdo ou rejeitá-lo. A proteção contra execução do script fica por conta do front-end/consumidor da API |

**Por que o teste passa?** Para SQL Injection, a API se comporta corretamente ao tratar o conteúdo como texto puro. Para XSS, a API não rejeita o payload mas também não executa o script — porém em um sistema real seria ideal que a API sanitizasse o conteúdo antes de armazená-lo.

---

### Resumo

Esses comportamentos foram **intencionalmente documentados nos testes** para demonstrar que:

1. A automação não apenas "faz os testes passarem" — ela identifica e registra problemas reais
2. Os cenários foram pensados para cobrir situações que causariam falhas em produção
3. A FakeRestAPI é uma API de simulação e não implementa todas as regras esperadas de um sistema real
4. Em um ambiente de projeto real, cada uma dessas observações geraria um **relatório de bug** para o time de desenvolvimento
