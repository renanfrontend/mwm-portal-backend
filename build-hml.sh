#!/usr/bin/env bash
set -euo pipefail

# ============================================================================
# SCRIPT DE BUILD PARA HOMOLOGAÇÃO
# ============================================================================
# Uso: ./build-hml.sh <TAG>
# Exemplo: ./build-hml.sh 202603301530
#
# O que faz:
# 1. Valida se TAG foi fornecida
# 2. Compila Maven com profile 'hml' (SQL Server)
# 3. Constrói imagem Docker com tag especificada
# 4. Imagem está pronta para PUSH ao ACR
#
# Resultado:
# - Docker Image: pgrsbpacr.azurecr.io/bioplanta-backend-hml:202603301530
# 
# Próximo passo:
#   ./deploy-hml.sh 202603301530
# ============================================================================

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
IMAGE_NAME="bioplanta-backend-hml"
FULL_IMAGE="${REGISTRY}/${IMAGE_NAME}:${TAG}"

echo "════════════════════════════════════════════════════════════════"
echo "🔨 BUILD HOMOLOGAÇÃO (HML)"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📊 Configuração:"
echo "   🏷️  TAG:             ${TAG}"
echo "   🏭 Registry:        ${REGISTRY}"
echo "   📦 Imagem:          ${FULL_IMAGE}"
echo "   📍 Profile:         hml (SQL Server)"
echo ""

# 1) Maven Clean Install com profile 'hml'
echo "📦 Compilando com Maven (profile=hml)..."
mvn clean install \
  -DskipTests \
  -Dspring.profiles.active=hml

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
echo "✅ BUILD HML CONCLUÍDO COM SUCESSO!"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📍 Imagem criada (pronta para push):"
echo "   ${FULL_IMAGE}"
echo ""
echo "🚀 Próximo passo - Fazer deploy em HML:"
echo "   ./deploy-hml.sh ${TAG}"
echo ""
echo "⚠️  Lembre-se:"
echo "   - A imagem está APENAS local"
echo "   - Execute deploy-hml.sh para enviar ao Azure ACR"
echo "   - Deploy vai atualizar o Container App de HML"
echo "════════════════════════════════════════════════════════════════"
