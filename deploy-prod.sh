#!/usr/bin/env bash
################################################################################
# Autor: Antonio Marcos de Souza Santos
# Cargo: Fullstack Developer
# Projeto: BioPlantas Backend
# Data: 2026-05-04
# -----------------------------------------------------------------------------
# Este script foi desenvolvido e mantido por Antonio Marcos de Souza Santos
# (Fullstack Developer) para automação de deploy do ambiente de produção.
################################################################################
set -euo pipefail

# ============================================================================
# SCRIPT DE DEPLOY PARA PRODUÇÃO
# ============================================================================
# Uso: ./deploy-prod.sh <TAG>
# Exemplo: ./deploy-prod.sh 202603301530
#
# ⚠️  CUIDADO: Este script impacta PRODUÇÃO - USE COM CAUTELA!
#
# Pré-requisito:
# - Executou: ./build-prod.sh 202603301530
# - Imagem local existe: pgrsbpacr.azurecr.io/bio-plant-backend:202603301530
# - Autenticação Azure CLI: az login
# - Testou em HML primeiro
#
# O que faz:
# 1. Login no ACR (Azure Container Registry)
# 2. Push da imagem Docker para o registry
# 3. Atualiza Container App de PRODUÇÃO com nova imagem
# 4. Lista revisões ativas do Container App
#
# Resultado:
# - Imagem disponível no ACR
# - Container App PRODUÇÃO atualizado
# - Nova revisão criada automaticamente
# - USUÁRIOS FINAIS SERÃO IMPACTADOS
#
# ⚠️  CHECKLIST ANTES DE EXECUTAR:
#    ☐ Testou em HML e tudo funcionou
#    ☐ Fez code review das mudanças
#    ☐ Verifica a TAG está correta
#    ☐ Informou o time sobre o deploy
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
IMAGE_NAME="bio-plant-backend"
RESOURCE_GROUP="PRGS_BIOPLANTA"
CONTAINERAPP_NAME="pgrsbpcapp-backend"
FULL_IMAGE="${REGISTRY}/${IMAGE_NAME}:${TAG}"

echo "════════════════════════════════════════════════════════════════"
echo "⚠️  DEPLOY PRODUÇÃO (PROD) - ATENÇÃO!"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📊 Configuração:"
echo "   🏷️  TAG:             ${TAG}"
echo "   📦 Imagem:          ${FULL_IMAGE}"
echo "   🏭 Registry:        ${REGISTRY}"
echo "   📱 Container App:   ${CONTAINERAPP_NAME}"
echo "   📍 Resource Group:  ${RESOURCE_GROUP}"
echo "   🎯 Ambiente:        PRODUÇÃO"
echo ""
echo "⚠️  ALERTA:"
echo "   Esta ação vai IMPACTAR USUÁRIOS FINAIS"
echo "   Uma nova revisão será ativada imediatamente"
echo ""

# Checklist de confirmação
echo "✅ CHECKLIST PRÉ-DEPLOY:"
echo "   [?] Você testou em HML e funciona?"
echo "   [?] Você fez code review?"
echo "   [?] A TAG ${TAG} está correta?"
echo "   [?] Você avisou o time?"
echo ""

read -p "Digite 'DEPLOY PRODUÇÃO' para confirmar (ou qualquer outra coisa para cancelar): " confirm
if [ "$confirm" != "DEPLOY PRODUÇÃO" ]; then
  echo "❌ Deploy cancelado!"
  exit 1
fi

echo ""
echo "🔴 INICIANDO DEPLOY EM PRODUÇÃO..."
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
echo "🔄 [3/4] Atualizando Container App PRODUÇÃO..."
az containerapp update \
  --name "${CONTAINERAPP_NAME}" \
  --resource-group "${RESOURCE_GROUP}" \
  --image "${FULL_IMAGE}" \
  --set-env-vars \
    SPRING_PROFILES_ACTIVE=prod \
    SPRING_JPA_HIBERNATE_DDL_AUTO=none \
    SPRING_JPA_PROPERTIES_HIBERNATE_DEFAULT_SCHEMA=dbo \
    SPRING_DATASOURCE_URL="jdbc:sqlserver://prgsazsqldb.database.windows.net:1433;databaseName=bioplanta;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;" \
    SPRINGDOC_SWAGGER_UI_ENABLED=TRUE \
    SPRING_DATASOURCE_DRIVER_CLASS_NAME="com.microsoft.sqlserver.jdbc.SQLServerDriver" \
    SPRING_SQL_INIT_MODE=never \
    CORS_ALLOWED_ORIGINS="*"

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
echo "✅ DEPLOY PRODUÇÃO CONCLUÍDO!"
echo "════════════════════════════════════════════════════════════════"
echo ""
echo "📍 Nova revisão criada:"
echo "   TAG:  ${TAG}"
echo "   URL:  https://pgrsbpcapp-backend.lemonwater-1dd3241c.eastus2.azurecontainerapps.io/api"
echo ""
echo "🔍 Para monitorar logs em TEMPO REAL:"
echo "   az containerapp logs show --name ${CONTAINERAPP_NAME} --resource-group ${RESOURCE_GROUP} --follow"
echo ""
echo "⏱️  Aguarde ~2-3 minutos para a revisão ficar 100% pronta"
echo "💬 Avise o time que o deploy foi realizado"
echo "════════════════════════════════════════════════════════════════"
