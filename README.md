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

![image](https://github.com/zsantana/rinha-backend-by-vinicius-ferraz/assets/17239827/ad699558-476d-4e02-b1a2-03b943f49062)


* Resultado e performance:

![image](https://github.com/zsantana/rinha-backend-by-vinicius-ferraz/assets/17239827/e770a4b8-bfce-47e5-9a53-a5584ca2d15b)


![image](https://github.com/zsantana/rinha-backend-by-vinicius-ferraz/assets/17239827/bd8a6b56-49e4-432e-a0e5-c6db87b520e6)

* Erros:
* ![image](https://github.com/zsantana/rinha-backend-by-vinicius-ferraz/assets/17239827/521b2ad2-7786-4ebc-8916-6d8b44a273c1)

* Requisições por segundos:
![image](https://github.com/zsantana/rinha-backend-by-vinicius-ferraz/assets/17239827/add7fb2f-b364-48b9-be3f-4e9d0cb049aa)


