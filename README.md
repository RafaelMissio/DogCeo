# 🐕 Dog CEO API - Testes Automatizados


Projeto de automação de testes para a API Dog CEO (https://dog.ceo/dog-api/) utilizando Java, JUnit 5, RestAssured e Allure Reports.

## 📋 Pré-requisitos

- Java 17 ou superior
- Gradle 8.14 ou superior (wrapper incluído)

## 🚀 Executando os Testes

### Localmente

```bash
# Executar todos os testes
./gradlew clean test

# Gerar e visualizar relatório Allure
./gradlew allureServe

# Gerar relatório Allure (sem abrir o navegador)
./gradlew allureReport
```

### No CI/CD

Os testes são executados automaticamente via GitHub Actions em três situações:

1. **Push/Pull Request**: Ao fazer push ou criar PR nas branches `main`, `master` ou `develop`
2. **Execução Manual**: Via workflow_dispatch no GitHub Actions
3. **Agendamento**: Diariamente às 9h UTC (6h BRT)

## 📊 Relatórios

### Allure Report

Os relatórios do Allure são gerados automaticamente e publicados no GitHub Pages:

🔗 **[Ver Relatório Allure](https://rafaelmissio.github.io/DogCeo/4/index.html)**

### Artefatos

Os resultados dos testes ficam disponíveis como artefatos no GitHub Actions por 30 dias.

## 🔧 Estrutura do Projeto

```
dogCeo/
├── .github/
│   └── workflows/          # Workflows do GitHub Actions
│       ├── test.yml        # Pipeline principal de testes
│       └── scheduled-tests.yml  # Testes agendados
├── src/
│   ├── main/java/
│   └── test/java/
│       ├── base/           # Classes base para testes
│       ├── models/         # Modelos de resposta
│       ├── services/       # Serviços da API
│       └── tests/          # Testes
├── build.gradle.kts        # Configuração do Gradle
└── gradlew                 # Gradle Wrapper
```

## 🧪 Testes Implementados

### RandomImageTest
- ✅ Validação de imagem aleatória
- ✅ Validação de aleatoriedade
- ✅ Validação de parâmetros extras

## 🛠️ Tecnologias Utilizadas

- **Java 17**: Linguagem de programação
- **JUnit 5**: Framework de testes
- **RestAssured**: Biblioteca para testes de API REST
- **Allure**: Framework de relatórios
- **Gradle**: Ferramenta de build
- **GitHub Actions**: CI/CD

## 📦 Dependências Principais

```kotlin
- io.rest-assured:rest-assured:6.0.0
- org.junit.jupiter:junit-jupiter:5.10.0
- io.qameta.allure:allure-junit5:2.32.0
- org.hamcrest:hamcrest:2.2
```

## 🔄 CI/CD Pipeline

### Workflow Principal (`test.yml`)

1. **Checkout**: Faz checkout do código
2. **Setup Java**: Configura Java 17
3. **Execute Tests**: Roda os testes com Gradle
4. **Generate Report**: Gera relatório Allure
5. **Deploy Pages**: Publica relatório no GitHub Pages
6. **Upload Artifacts**: Salva resultados como artefatos

### Workflow Agendado (`scheduled-tests.yml`)

- Executa testes diariamente para monitorar a saúde da API
- Notifica em caso de falhas

## 🌐 Configurando GitHub Pages

Para visualizar os relatórios Allure no GitHub Pages:

1. Vá em **Settings** → **Pages** do seu repositório
2. Em **Source**, selecione a branch `gh-pages`
3. Clique em **Save**
4. Os relatórios estarão disponíveis em: `https://USUARIO.github.io/dogCeo/`

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📝 Notas

- Os testes utilizam UTF-8 encoding para suportar caracteres acentuados
- A configuração regional está definida como pt-BR
- Os relatórios Allure mantêm histórico dos últimos 20 execuções

---

**Desenvolvido com ☕ e 🐕**

