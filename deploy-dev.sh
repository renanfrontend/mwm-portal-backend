#!/usr/bin/env bash
################################################################################
# Autor: Antonio Marcos de Souza Santos
# Cargo: Fullstack Developer
# Projeto: BioPlantas Backend
# Data: 2026-05-04
# -----------------------------------------------------------------------------
# Este script foi desenvolvido e mantido por Antonio Marcos de Souza Santos
# (Fullstack Developer) para automação de deploy do ambiente de desenvolvimento.
################################################################################
set -euo pipefail

# ============================================================================
# SCRIPT DE DEPLOY PARA DESENVOLVIMENTO (DEV)
# ============================================================================
# Uso: ./deploy-dev.sh <TAG>
# Exemplo: ./deploy-dev.sh 202605041120
#
# Pré-requisito:
# - Executou: ./build-dev.sh 202605041120
# - Imagem local existe: pgrsbpacr.azurecr.io/bioplanta-backend-dev:202605041120
# - Autenticação Azure CLI: az login
#
# O que faz:
# 1. Login no ACR (Azure Container Registry)
# 2. Push da imagem Docker para o registry
# 3. Atualiza Container App DEV com nova imagem
# 4. Lista revisões ativas do Container App
#
# Resultado:
# - Imagem disponível no ACR
# - Container App DEV atualizado
# - Nova revisão criada automaticamente
# ============================================================================

if [ "${1-}" = "" ]; then
  echo "❌ Erro: TAG da imagem não fornecida!"
  echo ""
  echo "Uso: $0 <TAG>"
  echo "Exemplo: $0 202605041120"
  echo ""
  echo "Dica: Use formato YYYYMMDDHHmm para versionar"
  echo "      date '+%Y%m%d%H%M' gera: $(date '+%Y%m%d%H%M')"
  exit 1
fi

TAG="$1"
REGISTRY="pgrsbpacr.azurecr.io"
IMAGE_NAME="bioplanta-backend-dev"
RESOURCE_GROUP="PRGS_BIOPLANTA"
CONTAINERAPP_NAME="bioplanta-backend-dev"
FULL_IMAGE="${REGISTRY}/${IMAGE_NAME}:${TAG}"

echo "════════════════════════════════════════════════════════════════"
echo "🚀 DEPLOY DESENVOLVIMENTO (DEV)"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📊 Configuração:"
echo "   🏷️  TAG:             ${TAG}"
echo "   📦 Imagem:          ${FULL_IMAGE}"
echo "   🏭 Registry:        ${REGISTRY}"
echo "   📱 Container App:   ${CONTAINERAPP_NAME}"
echo "   📍 Resource Group:  ${RESOURCE_GROUP}"
echo ""

# ============================================================================
# ETAPA 1: Login no ACR
# ============================================================================
echo "🔐 [1/4] Autenticando no Azure Container Registry..."
az acr login --name "${REGISTRY%%.azurecr.io}"
echo "✅ Login ACR concluído!"
echo ""

# ============================================================================
# ETAPA 2: Push da imagem
# ============================================================================
echo "📤 [2/4] Enviando imagem para ACR..."
docker push "${FULL_IMAGE}"
echo "✅ Push concluído!"
echo ""

# ============================================================================
# ETAPA 3: Atualizar Container App
# ============================================================================
echo "🔄 [3/4] Atualizando Container App DEV..."
az containerapp update \
  --name "${CONTAINERAPP_NAME}" \
  --resource-group "${RESOURCE_GROUP}" \
  --image "${FULL_IMAGE}" \
  --set-env-vars \
    DB_SCHEMA=dev \
    SPRING_APPLICATION_NAME=bioplanta \
    SPRING_PROFILES_ACTIVE=dev \
    SPRING_JPA_HIBERNATE_DDL_AUTO=none \
    SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA=dev \
    SPRING_DATASOURCE_URL="jdbc:sqlserver://dgrssqlsbioplanta.database.windows.net:1433;databaseName=bioplanta;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" \
    SPRING_DATASOURCE_USERNAME="bioplantausr" \
    SPRING_DATASOURCE_PASSWORD='b10pl@nt@$10' \
    SPRINGDOC_SWAGGER_UI_ENABLED=TRUE \
    SPRING_DATASOURCE_DRIVER_CLASS_NAME="com.microsoft.sqlserver.jdbc.SQLServerDriver" \
    SPRING_SQL_INIT_MODE=never \
    ADMIN_HASH="pgrsbp_secret_2026" \
    JAVA_TOOL_OPTIONS="-Xss2m"

echo "✅ Container App atualizado!"
echo ""

# ============================================================================
# ETAPA 4: Listar revisões
# ============================================================================
echo "📋 [4/4] Revisões ativas do Container App:"
echo ""
az containerapp revision list \
  --name "${CONTAINERAPP_NAME}" \
  --resource-group "${RESOURCE_GROUP}" \
  --output table

echo ""
echo "════════════════════════════════════════════════════════════════"
echo "✅ DEPLOY DEV CONCLUÍDO COM SUCESSO!"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📍 Nova revisão criada:"
echo "   TAG:  ${TAG}"
echo ""
echo "🔍 Para verificar os logs:"
echo "   az containerapp logs show --name ${CONTAINERAPP_NAME} --resource-group ${RESOURCE_GROUP}"
echo ""
echo "⏱️  Aguarde ~2-3 minutos para a revisão ficar 100% pronta"
echo "════════════════════════════════════════════════════════════════"
