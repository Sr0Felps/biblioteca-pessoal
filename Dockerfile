# ==========================================================
# ESTÁGIO 1: BUILD - Usando a tag OFICIAL: maven:<versão>-eclipse-temurin
# ==========================================================
FROM maven:3.9.11-eclipse-temurin AS build # TAG CORRIGIDA E EXPLÍCITA

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo de configuração do Maven (pom.xml) e baixa as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte da aplicação
COPY src /app/src

# Empacota o projeto em um arquivo JAR executável
RUN mvn package -DskipTests

# ==========================================================
# ESTÁGIO 2: RUNTIME - Imagem final mais leve apenas com JRE
# ==========================================================
FROM eclipse-temurin:21-jre-jammy AS final

# O Spring Boot JAR precisa de uma variável de ambiente para funcionar
ENV JAVA_TOOL_OPTIONS="-Xms512m -Xmx1024m"

# O ID de usuário do Spring Boot (1000) é comumente usado em imagens JRE leves.
USER 1000

# Copia o JAR do estágio 'build' para o estágio 'final'
COPY --from=build /app/target/biblioteca-pessoal-0.0.1-SNAPSHOT.jar /app/app.jar

# Define o ponto de entrada para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]