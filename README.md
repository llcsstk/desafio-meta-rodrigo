# Desafio Meta - Rodrigo

A aplicação consulta a URL de um repositório GitHub (PUBLICO) e consulta os arquivos via link direto. Se na listagem aparecer um diretório, busca o diretório recursivamente buscando seus arquivos.

Para arquivos que o GitHub não apresenta número de linhas (ex. PNG), é calculado o tamanho do arquivo.


A primeira consulta do repositório é mais lenta, pois é indexado na database in memory os dados do arquivo.

Caso na segunda consulta não haja nenhum commit novo, ele recupera o cache da database e retorna no endpoint.

Caso na segunda consulta haja um novo commit, é refeita a indexação causando uma lentidão a mais.

# Utilização - <url>/scrap/github?url=<repo url>

Exemplo: http://localhost:8080/scrap/github?url=https://github.com/public-apis/public-apis

# Swagger

Acesse via /swagger-ui.html

# Heroku

https://desafio-meta.herokuapp.com/

[IMPORTANTE] O cache dos dados é feito via database in memory, o que não é permitido nos planos do Heroku, por isso a aplicação apresenta erro acessando via link acima.

Foi feito o deploy somente para demonstrar o deploy integrado via GitHub -> Heroku
