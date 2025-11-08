# Stage 1: Build the application
# Usa uma imagem Maven com OpenJDK 21 para compilar o projeto
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copia o pom.xml e as dependências para otimizar o cache do Docker
COPY pom.xml .
# Copia o código fonte
COPY src ./src
# Adiciona comandos de diagnóstico para verificar a versão do Java e Maven
RUN java -version
RUN javac -version
RUN mvn -v
# Constrói o projeto Quarkus, pulando os testes
RUN mvn clean package -DskipTests
# Adiciona comando para listar o conteúdo de target/quarkus-app
RUN ls -R target/quarkus-app/

# Stage 2: Run the application
# Usa uma imagem OpenJDK 21 slim para a imagem final, que é menor
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copia o JAR executável e as bibliotecas geradas pelo Quarkus do estágio de build
COPY --from=build /app/target/quarkus-app/lib/ /app/lib/
COPY --from=build /app/target/quarkus-app/*.jar /app/
COPY --from=build /app/target/quarkus-app/app/ /app/app/
# Copia o diretório 'quarkus' que contém o quarkus-application.dat
COPY --from=build /app/target/quarkus-app/quarkus/ /app/quarkus/
# Expõe a porta que o Quarkus usa por padrão (8080)
EXPOSE 8080
# Comando para iniciar a aplicação
CMD ["java", "-Dquarkus.http.host=0.0.0.0", "-jar", "quarkus-run.jar"]