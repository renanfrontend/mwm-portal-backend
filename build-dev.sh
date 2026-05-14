#!/usr/bin/env bash
################################################################################
# Autor: Antonio Marcos de Souza Santos
# Cargo: Fullstack Developer
# Projeto: BioPlantas Backend
# Data: 2026-05-04
# -----------------------------------------------------------------------------
# Este script foi desenvolvido e mantido por Antonio Marcos de Souza Santos
# (Fullstack Developer) para automação de build do ambiente de desenvolvimento.
################################################################################
set -euo pipefail

# ============================================================================
# SCRIPT DE BUILD PARA DESENVOLVIMENTO
# ============================================================================
# Uso: ./build-dev.sh <TAG>
# Exemplo: ./build-dev.sh 202605041030
#
# O que faz:
# 1. Valida se TAG foi fornecida
# 2. Compila Maven com profile 'dev'
# 3. Constrói imagem Docker com tag especificada
# 4. Imagem está pronta para PUSH ao ACR
#
# Resultado:
# - Docker Image: pgrsbpacr.azurecr.io/bioplanta-backend-dev:202605041030
#
# Próximo passo:
#   ./deploy-dev.sh 202605041030
# ============================================================================

# Define schema do banco para desenvolvimento
export DB_SCHEMA=dev

if [ "${1-}" = "" ]; then
  echo "❌ Erro: TAG da imagem não fornecida!"
  echo ""
  echo "Uso: $0 <TAG>"
  echo "Exemplo: $0 202605041030"
  echo ""
  echo "Dica: Use formato YYYYMMDDHHmm para versionar"
  echo "      date '+%Y%m%d%H%M' gera: $(date '+%Y%m%d%H%M')"
  exit 1
fi

TAG="$1"
REGISTRY="pgrsbpacr.azurecr.io"
IMAGE_NAME="bioplanta-backend-dev"
FULL_IMAGE="${REGISTRY}/${IMAGE_NAME}:${TAG}"

echo "════════════════════════════════════════════════════════════════"
echo "🔨 BUILD DESENVOLVIMENTO (DEV)"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📊 Configuração:"
echo "   🏷️  TAG:             ${TAG}"
echo "   🏭 Registry:        ${REGISTRY}"
echo "   📦 Imagem:          ${FULL_IMAGE}"
echo "   📍 Profile:         dev"
echo ""

# 1) Maven Clean Install com profile 'dev'
echo "📦 Compilando com Maven (profile=dev)..."
mvn clean install \
  -DskipTests \
  -Dspring.profiles.active=dev

echo "✅ Maven compile concluído!"
echo ""

# 2) Docker Build
echo "🐳 Construindo imagem Docker..."
docker build \
  --no-cache \
  --pull \
  -t "${FULL_IMAGE}" \
  .

echo "✅ Docker build concluído!"
echo ""

# ============================================================================
# PRÓXIMOS PASSOS
# ============================================================================
echo "════════════════════════════════════════════════════════════════"
echo "✅ BUILD DEV CONCLUÍDO COM SUCESSO!"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📍 Imagem criada (pronta para push):"
echo "   ${FULL_IMAGE}"
echo ""
echo "🚀 Próximo passo - Fazer deploy em DEV:"
echo "   ./deploy-dev.sh ${TAG}"
echo ""
echo "⚠️  Lembre-se:"
echo "   - A imagem está APENAS local"
echo "   - Execute deploy-dev.sh para enviar ao Azure ACR"
echo "   - Deploy vai atualizar o Container App de DEV"
echo "   - URL: https://bioplanta-backend-dev.internal.lemonwater-1dd3241c.eastus2.azurecontainerapps.io"
echo "════════════════════════════════════════════════════════════════"
