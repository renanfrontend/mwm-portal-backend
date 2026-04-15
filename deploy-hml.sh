#!/usr/bin/env bash
set -euo pipefail

# ============================================================================
# SCRIPT DE DEPLOY PARA HOMOLOGAÇÃO (HML)
# ============================================================================
# Uso: ./deploy-hml.sh <TAG>
# Exemplo: ./deploy-hml.sh 202603301530
#
# Pré-requisito:
# - Executou: ./build-hml.sh 202603301530
# - Imagem local existe: pgrsbpacr.azurecr.io/bioplanta-backend-hml:202603301530
# - Autenticação Azure CLI: az login
#
# O que faz:
# 1. Login no ACR (Azure Container Registry)
# 2. Push da imagem Docker para o registry
# 3. Atualiza Container App de HML com nova imagem
# 4. Lista revisões ativas do Container App
#
# Resultado:
# - Imagem disponível no ACR
# - Container App HML atualizado
# - Nova revisão criada automaticamente
# ============================================================================

if [ "${1-}" = "" ]; then
  echo "❌ Erro: TAG da imagem não fornecida!"
  echo ""
  echo "Uso: $0 <TAG>"
  echo "Exemplo: $0 202603301530"
  exit 1
fi

TAG="$1"
REGISTRY="pgrsbpacr.azurecr.io"
IMAGE_NAME="bioplanta-backend-hml"
RESOURCE_GROUP="PRGS_BIOPLANTA"
CONTAINERAPP_NAME="bioplanta-backend-hml"
FULL_IMAGE="${REGISTRY}/${IMAGE_NAME}:${TAG}"

echo "════════════════════════════════════════════════════════════════"
echo "🚀 DEPLOY HOMOLOGAÇÃO (HML)"
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
echo "🔄 [3/4] Atualizando Container App HML..."
az containerapp update \
  --name "${CONTAINERAPP_NAME}" \
  --resource-group "${RESOURCE_GROUP}" \
  --image "${FULL_IMAGE}" \
  --set-env-vars \
    SPRING_PROFILES_ACTIVE=hml \
    SPRING_JPA_HIBERNATE_DDL_AUTO=none \
    SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA=dbo \
    SPRING_DATASOURCE_URL="jdbc:sqlserver://dgrssqlsbioplanta.database.windows.net:1433;databaseName=bioplanta;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" \
    SPRINGDOC_SWAGGER_UI_ENABLED=TRUE \
    SPRING_DATASOURCE_DRIVER_CLASS_NAME="com.microsoft.sqlserver.jdbc.SQLServerDriver" \
    SPRING_SQL_INIT_MODE=never

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
echo "✅ DEPLOY HML CONCLUÍDO COM SUCESSO!"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📍 Nova revisão criada:"
echo "   TAG:  ${TAG}"
echo "   URL:  https://bioplanta-backend-hml.lemonwater-1dd3241c.eastus2.azurecontainerapps.io/api"
echo ""
echo "🔍 Para verificar os logs:"
echo "   az containerapp logs show --name ${CONTAINERAPP_NAME} --resource-group ${RESOURCE_GROUP}"
echo ""
echo "⏱️  Aguarde ~2-3 minutos para a revisão ficar 100% pronta"
echo "════════════════════════════════════════════════════════════════"
