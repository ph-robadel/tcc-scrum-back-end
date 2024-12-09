# Sistema Scrum back-end

## Descrição

Este repositório contém o trabalho de conclusão de curso desenvolvido para a Universidade Federal do Espírito Santo destinado ao curso Sistemas de Informação. O objetivo deste projeto é desenvolver um sistema back-end que disponibilize um conjunto API's para gerenciar projetos baseados na metodologia ágil Scrum, que permite uma gestão mais eficiente de projetos de software.

No diretório [diagramas](https://github.com/ph-robadel/tcc-scrum-back-end/tree/main/diagramas) está disponível as imagens e o projeto editável dos diagramas desenvolvidos no software Astah UML. No diretório [documentacao-api](https://github.com/ph-robadel/tcc-scrum-back-end/tree/main/documentacao-api), está disponível o arquivo [api-docs.json](https://github.com/ph-robadel/tcc-scrum-back-end/blob/main/documentacao-api/api-docs.json) que contém a documentação das API's REST utilizando Swagger, e também o arquivo [Scrum Back-end.postman_collection.json](https://github.com/ph-robadel/tcc-scrum-back-end/blob/main/documentacao-api/Scrum%20Back-end.postman_collection.json) que é uma exportação da coleção de API para o Postman.

Além disso, o repositório contém o diretório [código-fonte](https://github.com/ph-robadel/tcc-scrum-back-end/tree/main/codigo-fonte) que contém o código-fonte da aplicação que foi desenvolvida utilizando a linguagem de programação Java na versão 21 e o Framework Spring.

## Informações sobre o TCC

**Autor**: Pedro Henrique Robadel da Silva Camâra

**Orientador**: [Dr. Clayton Vieira Fraga Filho](http://buscatextual.cnpq.br/buscatextual/visualizacv.do?id=E4839043)

**Instituição de ensino**: Universidade Federal do Espiríto Santo (UFES)

**Disciplinas**: 

- [x] ~~COM11212 Trabalho de conclusão de curso I~~
- [x] COM11261 Trabalho de conclusão de curso II

## Documentação OpenAPI

Ao executar o projeto, é possível acessar a documentação da API no padrão OpenAPI através do link:
~~~
http://localhost:8080/v3/api-docs
http://localhost:8080/swagger-ui/index.html
~~~

## Executando projeto via Docker

Para executar o projeto via docker, é possível construir uma nova imagem a partir do Dockerfile em ./codigo-fonte/scrum-back-end/Dockerfile ou baixar a imagem no Docker Hub.

Criando um novo container a partir da imagem disponível no [Docker Hub](https://hub.docker.com/r/phrobadel/scrum-rest-api):

~~~
docker run --name scrum-api -e USER_DB=postgres -e PASSWD_DB=123456 -e URL_DB=jdbc:postgresql://172.19.0.2:5432/scrumdb -e JWT_SECRET=d5fecba7-3a72-44fc-862a-260ddb241ebb -p 8080:8080 --network rede-scrum phrobadel/scrum-rest-api
~~~

- USER_DB: usuário do banco
- PASSWD_DB: senha do usuário do banco
- URL_DB: Url de conexão completa do banco de dados. Para executar localmente, verificar se estão na mesma rede (network).
- JWT_SECRET: Valor base para a geração da criptografia das senhas dos usuários.

## Lista de melhorias desejadas para o futuro

- [ ] Versionamento na rota da API. A API deve ter um sistema de controle de versão claramente definido para permitir que os usuários migrem para novas versões de maneira controlada e reversível.
- [ ] A API deve ter recursos de monitoramento e registro para rastrear e diagnosticar problemas. Isso deve incluir logs detalhados de todas as solicitações e respostas, bem como monitoramento em tempo real da saúde e desempenho da API.
- [ ] A API deve seguir os padrões de segurança como OAuth 2.0 para autenticação e autorização.
