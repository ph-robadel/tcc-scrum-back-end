# Sistema Scrum back-end

## Descrição

Este repositório contém o trabalho de conclusão de curso desenvolvido para a Universidade Federal do Espírito Santo destinado ao curso Sistemas de Informação. O objetivo deste projeto é desenvolver um sistema back-end que disponibilize um conjunto API's para gerenciar projetos baseados na metodologia ágil Scrum, que permite uma gestão mais eficiente de projetos de software.

No diretório "diagrama-caso-de-uso" e "diagrama-de-classe", estará disponível a representação gráfica em PDF e o projeto editável dos diagramas de caso de uso e diagrama de classe desenvolvidos no software Astah UML. No diretório "documentacao-api", estará disponível a documentação das API's REST utilizando Swagger.

Além disso, o repositório contém o código-fonte completo do sistema, que foi desenvolvido utilizando a linguagem de programação Java na versão 11 e o Framework Spring.

## Cronograma

Intervalo em dias do semestre 2023/01 (11/03/2024 - 12/07/2024): 
Total dias do semestre letivo: 123 dias
Restantes: 123 dias (17 semanas)

| Atividades                         | Status da Atividade | Março | Abril | Maio | Junho | Julho |
| ---------------------------------- | ------------------- | ----- | ----- | ---- | ----- | ----- |
| Modelar diagrama de caso de uso    | Concluído           |       |       |      |       |       |
| Modelar diagrama de classe         | Concluído           |       |       |      |       |       |
| Escrever TCC 1                     | Concluído           |       |       |      |       |       |
| Montar slide da apresentação TCC 1 | Concluído           |       |       |      |       |       |
| Apresentar TCC 1                   | Concluído           |       |       |      |       |       |
|                                    |                     |       |       |      |       |       |
| Documentar API's com Swagger.      | Pendente            | x     |       |      |       |       |
| Implementar Software.              | Pendente            | x     | x     | x    | x     |       |
| Escrever TCC 2                     | Ativo               | x     | x     | x    | x     |       |
| Montar slide da apresentação TCC 2 | Pendente            |       |       |      | x     |       |
| Apresentar TCC 2                   | Pendente            |       |       |      |       | x     |

## Informações sobre o TCC

**Autor**: Pedro Henrique Robadel da Silva Camâra

**Orientador**: [Dr. Clayton Vieira Fraga Filho](http://buscatextual.cnpq.br/buscatextual/visualizacv.do?id=E4839043)

**Instituição de ensino**: Universidade Federal do Espiríto Santo (UFES)

**Disciplinas**: 

- [x] ~~COM11212 Trabalho de conclusão de curso I~~
- [ ] COM11261 Trabalho de conclusão de curso II

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
