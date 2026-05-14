#!/usr/bin/env bash
################################################################################
# Autor: Antonio Marcos de Souza Santos
# Cargo: Fullstack Developer
# Projeto: BioPlantas Backend
# Data: 2026-05-04
# -----------------------------------------------------------------------------
# Este script foi desenvolvido e mantido por Antonio Marcos de Souza Santos
# (Fullstack Developer) para automação de build do ambiente de produção.
################################################################################
set -euo pipefail

# ============================================================================
# SCRIPT DE BUILD PARA PRODUÇÃO
# ============================================================================
# Uso: ./build-prod.sh <TAG>
# Exemplo: ./build-prod.sh 202603301530
#
# O que faz:
# 1. Valida se TAG foi fornecida
# 2. Compila Maven com profile 'prod' (SQL Server Azure)
# 3. Constrói imagem Docker com tag especificada
# 4. Imagem está pronta para PUSH ao ACR
#
# Resultado:
# - Docker Image: pgrsbpacr.azurecr.io/bio-plant-backend:202603301530
# 
# ⚠️  CUIDADO: Este é um build de PRODUÇÃO!
#   - Alterações afetarão usuários finais
#   - Sempre teste em HML primeiro
#   - Verifique versão antes de fazer deploy
#
# Próximo passo:
#   ./deploy-prod.sh 202603301530
# ============================================================================

# Define schema do banco para produção
export DB_SCHEMA=dbo

if [ "${1-}" = "" ]; then
  echo "❌ Erro: TAG da imagem não fornecida!"
  echo ""
  echo "Uso: $0 <TAG>"
  echo "Exemplo: $0 202603301530"
  echo ""
  echo "Dica: Use formato YYYYMMDDHHmm para versionar"
  echo "      date '+%Y%m%d%H%M' gera: $(date '+%Y%m%d%H%M')"
  exit 1
fi

TAG="$1"
REGISTRY="pgrsbpacr.azurecr.io"
IMAGE_NAME="bio-plant-backend"
FULL_IMAGE="${REGISTRY}/${IMAGE_NAME}:${TAG}"

echo "════════════════════════════════════════════════════════════════"
echo "⚠️  BUILD PRODUÇÃO (PROD) - CUIDADO!"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📊 Configuração:"
echo "   🏷️  TAG:             ${TAG}"
echo "   🏭 Registry:        ${REGISTRY}"
echo "   📦 Imagem:          ${FULL_IMAGE}"
echo "   📍 Profile:         prod (SQL Server Azure)"
echo "   🎯 Destino:         PRODUÇÃO"
echo ""
echo "⚠️  LEMBRETE IMPORTANTE:"
echo "   - Esta versão será deployada em PRODUÇÃO"
echo "   - Afetará usuários finais"
echo "   - Sempre teste em HML ANTES de fazer build"
echo ""

read -p "Deseja continuar? (sim/não): " confirm
if [ "$confirm" != "sim" ]; then
  echo "❌ Build cancelado pelo usuário"
  exit 1
fi

echo ""

# 1) Maven Clean Install com profile 'prod'
echo "📦 Compilando com Maven (profile=prod)..."
mvn clean install \
  -DskipTests \
  -Dspring.profiles.active=prod

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
echo "✅ BUILD PRODUÇÃO CONCLUÍDO COM SUCESSO!"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📍 Imagem criada (pronta para push):"
echo "   ${FULL_IMAGE}"
echo ""
echo "🚀 Próximo passo - Fazer deploy em PRODUÇÃO:"
echo "   ./deploy-prod.sh ${TAG}"
echo ""
echo "⚠️  IMPORTANTE:"
echo "   - Verifique 2x ANTES de executar deploy-prod.sh"
echo "   - A imagem está APENAS local até fazer deploy"
echo "   - Deploy vai IMPACTUAR usuários finais"
echo "════════════════════════════════════════════════════════════════"
