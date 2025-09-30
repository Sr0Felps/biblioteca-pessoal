# ==========================================================
# ESTÁGIO 1: BUILD - Para compilar a aplicação e gerar o JAR
# ==========================================================
# Use uma imagem com JDK e Maven para o processo de build.
FROM eclipse-temurin:21-jdk-jammy AS build

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo de configuração do Maven (pom.xml) e baixa as dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte da aplicação
COPY src /app/src

# Empacota o projeto em um arquivo JAR executável
# O '-DskipTests' é usado aqui apenas para acelerar o Docker build local.
# No pipeline de CI, rodaremos os testes no Job 'build' separadamente.
RUN mvn package -DskipTests

# O JAR resultante estará em /app/target/biblioteca-pessoal-0.0.1-SNAPSHOT.jar
# (O nome do JAR é baseado no <artifactId>-<version> do seu pom.xml)

# ==========================================================
# ESTÁGIO 2: RUNTIME - Imagem final mais leve apenas com JRE
# ==========================================================
# Use uma imagem com JRE (Java Runtime Environment) apenas
FROM eclipse-temurin:21-jre-jammy AS final

# O Spring Boot JAR precisa de uma variável de ambiente para funcionar
ENV JAVA_TOOL_OPTIONS="-Xms512m -Xmx1024m"

# O ID de usuário do Spring Boot (1000) é comumente usado em imagens JRE leves.
USER 1000

# Copia o JAR do estágio 'build' para o estágio 'final'
COPY --from=build /app/target/biblioteca-pessoal-0.0.1-SNAPSHOT.jar /app/app.jar

# Define o ponto de entrada para rodar a aplicação
ENTRYPOINT ["java", "-jar", "/app/app.jar"]