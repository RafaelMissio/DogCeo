# 🔧 Troubleshooting - Problemas Comuns e Soluções

## 🚨 Problemas Durante o Setup

### ❌ Erro ao fazer push: "permission denied"

**Problema:**
```
remote: Permission to username/repo.git denied to username.
fatal: unable to access 'https://github.com/...': The requested URL returned error: 403
```

**Solução:**
1. Verifique se você está autenticado no GitHub:
   ```bash
   git config --global user.name "Seu Nome"
   git config --global user.email "seu@email.com"
   ```

2. Use um Personal Access Token:
   - Acesse: https://github.com/settings/tokens
   - Generate new token (classic)
   - Marque `repo` scope
   - Use o token como senha ao fazer push

3. Ou configure SSH:
   ```bash
   ssh-keygen -t ed25519 -C "seu@email.com"
   cat ~/.ssh/id_ed25519.pub  # Adicione em GitHub Settings → SSH Keys
   git remote set-url origin git@github.com:USUARIO/dogCeo.git
   ```

---

### ❌ Workflow não aparece na aba Actions

**Solução:**
1. Verifique se os arquivos estão em `.github/workflows/`
2. Confirme que têm extensão `.yml` (não `.yaml`)
3. Verifique se Actions está habilitado:
   - Settings → Actions → General
   - "Allow all actions and reusable workflows"

---

### ❌ Workflow falha com "gradlew: permission denied"

**Problema:**
```
Error: Process completed with exit code 126.
/home/runner/work/dogCeo/dogCeo/gradlew: Permission denied
```

**Solução:**
1. Dê permissão ao gradlew localmente:
   ```bash
   chmod +x gradlew
   git add gradlew
   git commit -m "fix: Add execute permission to gradlew"
   git push
   ```

2. Ou verifique se o step no workflow está presente:
   ```yaml
   - name: Conceder permissões de execução ao Gradle Wrapper
     run: chmod +x ./gradlew
   ```

---

### ❌ GitHub Pages não publica (404)

**Problema:** Link do GitHub Pages retorna 404

**Solução:**
1. Aguarde o workflow criar a branch `gh-pages` (primeira execução)
2. Vá em Settings → Pages
3. Verifique se está configurado:
   - Source: Deploy from a branch
   - Branch: `gh-pages`
   - Folder: `/ (root)`
4. Aguarde 5-10 minutos após salvar
5. Force rebuild visitando: Settings → Pages → Save novamente

---

### ❌ Erro: "Read-only file system"

**Problema:**
```
Error: Unable to create directory /home/runner/work/dogCeo/gh-pages/.git
```

**Solução:**
Verifique permissões do workflow:
- Settings → Actions → General
- Workflow permissions → "Read and write permissions" ✅
- Save

---

## 🧪 Problemas com os Testes

### ❌ Testes passam localmente mas falham no CI

**Possíveis causas e soluções:**

1. **Timeout da API:**
   ```java
   // Adicione timeout nos testes
   RestAssured.config = RestAssured.config()
       .httpClient(HttpClientConfig.httpClientConfig()
           .setParam("http.connection.timeout", 10000)
           .setParam("http.socket.timeout", 10000));
   ```

2. **Encoding diferente:**
   - Já está configurado no build.gradle.kts ✅

3. **Versão do Java diferente:**
   - Workflow usa Java 17 (mesmo da configuração) ✅

---

### ❌ Relatório Allure vazio ou sem dados

**Solução:**
1. Verifique se `build/allure-results/` contém arquivos JSON:
   ```bash
   ls -la build/allure-results/
   ```

2. Se não houver arquivos, verifique as anotações nos testes:
   ```java
   @Test
   @Story("Nome da Story")
   @Description("Descrição do teste")
   public void meuTeste() { ... }
   ```

3. Verifique se o plugin Allure está configurado no build.gradle.kts ✅

---

## 📊 Problemas com Relatórios

### ❌ Histórico do Allure não aparece

**Solução:**
1. O histórico começa a aparecer após 2+ execuções
2. Verifique se `keep_reports: 20` está configurado no workflow ✅
3. A branch `gh-pages` mantém o histórico automaticamente

---

### ❌ Artefatos não são gerados

**Solução:**
1. Verifique se o step de upload existe no workflow ✅
2. Confirme que `if: always()` está presente para gerar mesmo com falhas ✅
3. Artefatos expiram após 30 dias (comportamento normal)

---

## 🔄 Problemas com Scheduled Tests

### ❌ Testes agendados não executam

**Solução:**
1. Scheduled workflows só funcionam na branch padrão (main/master)
2. Aguarde até 1 hora após o horário agendado (delay do GitHub)
3. Repositórios privados inativos podem ter schedule desabilitado
4. Execute manualmente primeiro: Actions → Workflow → Run workflow

---

## 🐛 Problemas de Configuração

### ❌ Gradle não encontra dependências

**Solução:**
1. Limpe o cache:
   ```bash
   ./gradlew clean --refresh-dependencies
   ```

2. Verifique conexão com Maven Central

3. No workflow, cache do Gradle está configurado ✅

---

### ❌ Erro "Could not find or load main class"

**Solução:**
1. Limpe e rebuild:
   ```bash
   ./gradlew clean build
   ```

2. Verifique encoding no build.gradle.kts (já configurado) ✅

---

## 💡 Dicas para Debug

### Ver logs detalhados do workflow

1. Acesse: Actions → Selecione a execução
2. Clique no job "Executar Testes de API"
3. Expanda cada step para ver logs completos
4. Use `showStandardStreams = true` no teste (já configurado) ✅

### Testar workflow localmente

Instale act (GitHub Actions localmente):
```bash
brew install act  # macOS
act -l            # Listar workflows
act push          # Simular push
```

### Forçar nova execução

1. Faça um commit vazio:
   ```bash
   git commit --allow-empty -m "trigger: Force CI rebuild"
   git push
   ```

2. Ou execute manualmente:
   - Actions → Workflow → Run workflow

---

## 📞 Recursos Adicionais

### Links Úteis

- [GitHub Actions Docs](https://docs.github.com/actions)
- [Allure Report Issues](https://github.com/allure-framework/allure2/issues)
- [RestAssured Docs](https://rest-assured.io/)
- [Gradle Troubleshooting](https://docs.gradle.org/current/userguide/troubleshooting.html)

### Verificar Status do GitHub

- [GitHub Status](https://www.githubstatus.com/)
- [GitHub Actions Status](https://www.githubstatus.com/incidents)

### Comandos de Diagnóstico

```bash
# Verificar versão do Java
java -version

# Verificar versão do Gradle
./gradlew --version

# Testar build localmente
./gradlew clean build --info

# Verificar configuração Git
git remote -v
git config --list

# Verificar status dos arquivos
git status
git ls-files
```

---

## ✅ Checklist de Verificação

Antes de abrir uma issue, verifique:

- [ ] Arquivos estão em `.github/workflows/`
- [ ] Permissões do workflow estão como "Read and write"
- [ ] GitHub Pages está configurado para branch `gh-pages`
- [ ] Gradlew tem permissão de execução (`chmod +x gradlew`)
- [ ] Workflow executou pelo menos uma vez
- [ ] Actions está habilitado no repositório
- [ ] Branch padrão é main ou master
- [ ] Não há typos nos nomes dos arquivos

---

**Última atualização:** 03/03/2026

Se o problema persistir, abra uma issue no repositório com:
- Logs completos do workflow
- Mensagem de erro específica
- Passos para reproduzir
- Versões (Java, Gradle, etc.)

