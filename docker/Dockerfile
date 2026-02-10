FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Copiar arquivos de dependências e build
COPY gradle/ ./gradle/
COPY gradlew build.gradle settings.gradle ./ 
RUN chmod +x ./gradlew

# Download de dependências
RUN ./gradlew dependencies --no-daemon

# Copiar código fonte
COPY src/ ./src/

# Construir o aplicativo AOT (native image)
RUN ./gradlew nativeCompile --no-daemon

# Imagem final
FROM alpine:3.20 AS runtime
WORKDIR /app

# Atualizar ferramentas básicas para health checks
RUN apk add --no-cache curl wget tzdata libc6-compat

# Definir timezone para GMT-3 (America/Sao_Paulo)
RUN cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime && \
    echo "America/Sao_Paulo" > /etc/timezone

# Adicionar usuário não-root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar binário nativo gerado pelo GraalVM
COPY --from=build /app/build/native/nativeCompile/* reports

# Ajustar permissões
RUN chmod +x /app/reports

# Mudar para usuário não-root
USER appuser

# Porta da aplicação (modificável via docker-compose)
ARG APP_PORT
EXPOSE ${APP_PORT}

# Verificação de saúde interna
HEALTHCHECK --interval=30s --timeout=10s --retries=3 \
  CMD wget --spider -q http://localhost:${APP_PORT}/actuator/health || exit 1

# Iniciar aplicação nativa
ENTRYPOINT ["/app/reports"]