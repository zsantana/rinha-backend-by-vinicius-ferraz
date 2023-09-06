# Rinha de Backend - By Vinícius Ferraz

Melhoria no código do projeto rinha-de-backend originário do repositório:

https://github.com/viniciusfcf/rinha-de-backend-2023-q3/tree/main/quarkus-app-revanche



## 💻 Getting started

```bash
# Build 
$ mvn clean package

# Local execution
$ mvn quarkus:dev -Ddebug=false
```


## Getting started Docker
```bash

# Started and attaches to containers for a service
$ docker-compose up --build
```


## Getting started Docker (Native Image)
```bash
# Install image from file build_docker_push.sh 
$ ./build_docker_native.sh 

# Started and attaches to containers for a service
$ docker-compose -f docker-compose-native.yml --env-file ./.env up
```


## ✔️ Required
* Maven: 3.8.4
* Java version: 17
* Docker version: 20.10.17
* Docker-compose version: v2.2.2



## Ajustes realizados:

* Ajuste no Script DDL com obrigatoriedade somente no campo apelido
* Ajuste no cache local para 100k (Total de registros do teste de stress)
* Busca de dados no cache, caso contrário, faz busca no bando de dados
* Aumento de sessões do Postgres para 400 e cada POD com 100 sessões simultâneas
* Alteração do método post usando chamada nativa (Panache.withTransaction) ao invés da notação CDI



## Resultado obtidos

* Perfil da máquina:



* Resultado e performance:
