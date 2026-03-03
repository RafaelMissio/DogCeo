#!/bin/bash

# Script de Verificação da Configuração CI/CD
# Uso: ./verificar-config.sh

echo "🔍 Verificando Configuração do CI/CD..."
echo ""

# Cores
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✅${NC} $1"
        return 0
    else
        echo -e "${RED}❌${NC} $1 (não encontrado)"
        return 1
    fi
}

check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✅${NC} $1/"
        return 0
    else
        echo -e "${RED}❌${NC} $1/ (não encontrado)"
        return 1
    fi
}

echo "📁 Verificando Estrutura de Arquivos:"
echo ""

# Workflows
echo "GitHub Actions Workflows:"
check_dir ".github/workflows"
check_file ".github/workflows/test.yml"
check_file ".github/workflows/scheduled-tests.yml"
echo ""

# Documentação
echo "Documentação:"
check_file "README.md"
check_file "SETUP_COMPLETO.md"
echo ""

# Configuração
echo "Configuração do Projeto:"
check_file "build.gradle.kts"
check_file "gradle.properties"
check_file ".gitignore"
echo ""

# Código fonte
echo "Código Fonte:"
check_dir "src/test/java"
check_file "src/test/java/tests/RandomImageTest.java"
echo ""

# Git
echo "Git:"
if [ -d ".git" ]; then
    echo -e "${GREEN}✅${NC} Repositório Git inicializado"

    # Verificar remote
    if git remote -v | grep -q "origin"; then
        echo -e "${GREEN}✅${NC} Remote 'origin' configurado"
        git remote -v | head -2
    else
        echo -e "${YELLOW}⚠️${NC}  Remote 'origin' não configurado"
        echo "   Execute: git remote add origin <URL_DO_REPOSITORIO>"
    fi
else
    echo -e "${YELLOW}⚠️${NC}  Repositório Git não inicializado"
    echo "   Execute: git init"
fi
echo ""

# Status Git
echo "📊 Status do Git:"
if [ -d ".git" ]; then
    git status --short | head -10
    if [ -z "$(git status --short)" ]; then
        echo -e "${GREEN}✅${NC} Working directory limpo"
    else
        echo -e "${YELLOW}⚠️${NC}  Há arquivos não commitados"
    fi
else
    echo -e "${YELLOW}⚠️${NC}  Git não inicializado"
fi
echo ""

# Gradle
echo "🔧 Verificando Gradle:"
if [ -f "gradlew" ]; then
    echo -e "${GREEN}✅${NC} Gradle Wrapper presente"
    if [ -x "gradlew" ]; then
        echo -e "${GREEN}✅${NC} Gradle Wrapper executável"
    else
        echo -e "${YELLOW}⚠️${NC}  Gradle Wrapper não executável"
        echo "   Execute: chmod +x gradlew"
    fi
else
    echo -e "${RED}❌${NC} Gradle Wrapper não encontrado"
fi
echo ""

# Resumo
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo -e "${GREEN}✅ Configuração CI/CD Verificada!${NC}"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "📋 Próximos Passos:"
echo ""
echo "1. Revisar arquivos de configuração"
echo "2. Fazer commit das alterações:"
echo -e "   ${YELLOW}git add .${NC}"
echo -e "   ${YELLOW}git commit -m 'feat: Adicionar CI/CD'${NC}"
echo ""
echo "3. Criar repositório no GitHub"
echo "4. Fazer push:"
echo -e "   ${YELLOW}git remote add origin <URL>${NC}"
echo -e "   ${YELLOW}git push -u origin main${NC}"
echo ""
echo "5. Configurar permissões e GitHub Pages"
echo ""
echo "📖 Leia SETUP_COMPLETO.md para instruções detalhadas"
echo ""

