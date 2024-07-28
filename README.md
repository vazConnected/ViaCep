# Consulta de CEPs via ViaCep
Esse projeto tem como objetivo estudar a utilização de APIs externas em Java e aplicar conhecimentos do framework Spring Boot.

## O que é ViaCep?
O [ViaCep](https://viacep.com.br/) é um webservice gratuito para consultar Códigos de Endereçamento Postais (CEPs).

## Como Utilizar?

### Pesquisa por Cep:
- **URL**: http://localhost:8080/cep/{cep}
- **Melhod**: <code>POST</code>
- **Parâmetro de entrada**: por *path variable*. Basta substituir o <code>{cep}</code> pelo Cep desejado.
  - O formato aceito é <code>00000-000</code>
  - Exemplo de solicitação: http://localhost:8080/cep/30480-000
- **Resposta**: um JSON com dois campos: cep e fromCache.
  - <code>cep</code>: as informações relacionadas ao cep pesquisado;
  - <code>fromCache</code>: indicador origem do resultado. Se *true*, indica que as informações foram obtidas da cache local. Se *false*, indica que o Cep foi obtido por uma chamada à API do ViaCep.
    - Exemplo de resposta para o cep <code>30480-000</code>:
        ```json
        {
            "cep": {
                "cep": "30480-000",
                "logradouro": "Avenida Amazonas",
                "complemento": "de 3681 a 4199 - lado ímpar",
                "unidade": "",
                "bairro": "Alto Barroca",
                "localidade": "Belo Horizonte",
                "uf": "MG",
                "ibge": "3106200",
                "gia": "",
                "ddd": "31",
                "siafi": "4123"
            },
            "fromCache": false
        }
        ```

### Pesquisa por Rua:
- **URL**: http://localhost:8080/rua
- **Melhod**: <code>POST</code>
- **Parâmetro de entrada**: por *request body*. Devem ser informados estado, cidade e rua.
    ```json
    {
        "rua": "Minas Gerais",
        "estado": "MG",
        "cidade": "Belo Horizonte"
    }
    ```
- **Resposta**: um JSON com a lista de Ceps referente à pesquisa
  - Exemplo de resposta:
    ```json
    {
        "ceps": [
            {
                "cep": "30455-520",
                "logradouro": "Beco Minas Gerais",
                "complemento": "",
                "unidade": "",
                "bairro": "Ventosa",
                "localidade": "Belo Horizonte",
                "uf": "MG",
                "ibge": "3106200",
                "gia": "",
                "ddd": "31",
                "siafi": "4123"
            },
            {
                "cep": "30421-637",
                "logradouro": "Beco Minas Gerais",
                "complemento": "",
                "unidade": "",
                "bairro": "Ventosa",
                "localidade": "Belo Horizonte",
                "uf": "MG",
                "ibge": "3106200",
                "gia": "",
                "ddd": "31",
                "siafi": "4123"
            },
            {
                "cep": "31255-030",
                "logradouro": "Avenida Minas Gerais",
                "complemento": "",
                "unidade": "",
                "bairro": "São Francisco",
                "localidade": "Belo Horizonte",
                "uf": "MG",
                "ibge": "3106200",
                "gia": "",
                "ddd": "31",
                "siafi": "4123"
            }
        ]
    }
    ```

### Pesquisas com Erro:
Para todas as pesquisas que, por qualquer motivo, resultem em erro, será retornado um objeto informando o tipo de erro e uma curta descrição. Abaixo, um exemplo de como seria reportada uma pesquisa em que as iniciais do estado estão incorretas:
```json
{
	"errorType": "java.security.InvalidParameterException",
	"errorMessage": "O estado informado não existe."
}
```

## Bibliotecas externas utilizadas
- [Gson](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.10.1);
- [Lombok](https://projectlombok.org/);
- [Spring Boot Devtools](https://docs.spring.io/spring-boot/docs/1.5.16.RELEASE/reference/html/using-boot-devtools.html).
