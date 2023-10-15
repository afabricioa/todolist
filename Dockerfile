FROM ubuntu:latest AS build

# roda um update
RUN apt-get update
# roda um install no java 17 colocando yes em todas as respostas
RUN apt-get install openjdk-17-jdk -y

# copia tudo da pasta local para pasta onde o projeto vai ser instalado
COPY . .

# instala o maven colocando yes em tudo
RUN apt-get install maven -y

# roda o mvn onde gera o .JAR na pasta target
RUN mvn clean install

EXPOSE 8080

# copia o build do jar da pasta target para o app na nuvem
COPY --from=build /target/todolist-0.0.1.jar app.jar

# comando para executar a aplicação java gerada no .jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]