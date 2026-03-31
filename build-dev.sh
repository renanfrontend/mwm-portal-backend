#!/usr/bin/env bash
set -euo pipefail

# ============================================================================
# SCRIPT DE BUILD PARA DESENVOLVIMENTO LOCAL
# ============================================================================
# Uso: ./build-dev.sh
#
# O que faz:
# 1. Compila o projeto Maven com profile 'dev'
# 2. Constrói imagem Docker local para desenvolvimento
# 3. Imagem usa H2 em memória (sem BD real)
#
# Resultado:
# - JAR em: target/bioplanta-0.0.1-SNAPSHOT.jar
# - Docker Image: bioplanta-backend:dev (local, não envia para registry)
#
# Como rodar após o build:
#   docker run -p 8080:8080 bioplanta-backend:dev
#   # Backend disponível em: http://localhost:8080/api
#   # Swagger em: http://localhost:8080/swagger-ui/index.html
#   # H2 Console em: http://localhost:8080/h2-console
# ============================================================================

echo "════════════════════════════════════════════════════════════════"
echo "🔨 BUILD DESENVOLVIMENTO LOCAL (H2 + Docker)"
echo "════════════════════════════════════════════════════════════════"
echo ""

# 1) Maven Clean Install com profile 'dev'
echo "📦 Compilando com Maven (profile=dev, H2)..."
mvn clean install \
  -DskipTests \
  -Dspring.profiles.active=dev

echo "✅ Maven compile concluído!"
echo ""

# 2) Docker Build
echo "🐳 Construindo imagem Docker local..."
docker build \
  --no-cache \
  --pull \
  -t bioplanta-backend:dev \
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
echo "📍 Imagem criada:"
echo "   bioplanta-backend:dev"
echo ""
echo "🚀 Para rodar localmente:"
echo "   docker run -p 8080:8080 bioplanta-backend:dev"
echo ""
echo "📂 Acessos disponíveis:"
echo "   🔹 API:            http://localhost:8080/api"
echo "   🔹 Swagger/OpenAPI: http://localhost:8080/swagger-ui/index.html"
echo "   🔹 H2 Console:     http://localhost:8080/h2-console"
echo "   🔹 Login: sa / (sem senha)"
echo ""
echo "⚠️  Nota: Dados H2 são perdidos ao parar o container"
echo "════════════════════════════════════════════════════════════════"
