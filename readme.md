# Spotify API Client com OpenFeign

## 📌 Introdução
Este projeto foi desenvolvido com o objetivo de praticar a utilização do **OpenFeign** para consumir serviços REST de forma declarativa no ecossistema Spring.

A aplicação realiza autenticação na API do **Spotify** e, em seguida, consome o endpoint de **novos lançamentos de álbuns**.

O projeto mostra como:
- Configurar **Feign Clients** para comunicação com serviços externos.
- Realizar autenticação via **Client Credentials Flow** do Spotify.
- Mapear as respostas JSON para classes Java utilizando **Jackson**.
- Utilizar anotações do Spring para organizar e expor endpoints REST.

---

## 🔧 Estrutura do Código e Explicação

### 1. **Feign Clients**

#### AuthSpotifyClient
```java
@FeignClient(
        name = "AuthSpotifyClient",
        url = "https://accounts.spotify.com"
)
public interface AuthSpotifyClient {

    @PostMapping(path = "/api/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    LoginResponse login(@RequestBody LoginRequest loginRequest);
}
```


@FeignClient: cria automaticamente um cliente HTTP para comunicação com o serviço externo.

name: identificador do client.

url: endereço base do serviço (neste caso, autenticação do Spotify).

@PostMapping: indica que será feita uma requisição POST.

path: caminho do endpoint.

consumes: define o tipo de conteúdo enviado (formulário codificado).

@RequestBody: envia o objeto LoginRequest como corpo da requisição.

### AlbumSpotifyClient
```java
@FeignClient(
name = "AlbumSpotifyClient",
url = "https://api.spotify.com"
)
public interface AlbumSpotifyClient {

    @GetMapping(path = "/v1/browse/new-releases")
    AlbumResponse getReleases(@RequestHeader("Authorization") String authorization);
}
```

@FeignClient: define outro client Feign, dessa vez para o serviço de álbuns do Spotify.

@GetMapping: especifica requisição GET para buscar lançamentos.

@RequestHeader: permite enviar o token de autenticação no cabeçalho da requisição.

2. Modelos de Requisição e Resposta
   LoginRequest
   public class LoginRequest {
   @FormProperty("grant_type")
   private String grantType;

   @FormProperty("client_id")
   private String clientId;

   @FormProperty("client_secret")
   private String clientSecret;
   }


@FormProperty: usado para mapear os campos esperados no corpo da requisição do tipo application/x-www-form-urlencoded.

### LoginResponse
```java
public class LoginResponse {
@JsonProperty("access_token")
private String acessToken;
}
```

@JsonProperty: mapeia o campo JSON access_token da resposta para a variável acessToken.


### AlbumResponse / AlbumWrapper / Album
```java
public class AlbumWrapper {
private List<Album> items;
}

public class AlbumResponse {
private AlbumWrapper albums;
}

public class Album {
private String id;
private String name;

    @JsonProperty("release_date")
    private String releaseDate;
}
```

Estrutura criada para refletir o formato JSON retornado pela API do Spotify.

@JsonProperty("release_date"): garante o mapeamento correto do campo JSON para releaseDate.

3. 

### Controller
```java
   @RestController
   @RequestMapping(path = "/spotify/api")
   public class AlbumController {

   private final AuthSpotifyClient authSpotifyClient;
   private final AlbumSpotifyClient albumSpotifyClient;

   @Value("${client_id}")
   private String client_id;

   @Value("${client_secret}")
   private String client_secret;

   @Value("${client_credentials}")
   private String client_credentials;

   public AlbumController(AuthSpotifyClient authSpotifyClient, AlbumSpotifyClient albumSpotifyClient) {
   this.authSpotifyClient = authSpotifyClient;
   this.albumSpotifyClient = albumSpotifyClient;
   }

   @GetMapping(path = "/albums")
   public ResponseEntity<AlbumResponse> loginResponse() {
   var request = new LoginRequest(client_credentials, client_id, client_secret);

        var token = authSpotifyClient.login(request).getAcessToken();
        var response = albumSpotifyClient.getReleases("Bearer " + token);

        return ResponseEntity.ok(response);
   }
}
```

@RestController: define a classe como um controlador REST.

@RequestMapping: define o caminho base para os endpoints.

@Value: injeta valores de variáveis de ambiente ou propriedades do application.properties.

@GetMapping: expõe o endpoint /spotify/api/albums que retorna os novos álbuns.

ResponseEntity.ok(response): retorna a resposta no formato JSON com status 200.

🚀 Fluxo da Aplicação

O usuário acessa o endpoint GET /spotify/api/albums.

O sistema cria um LoginRequest com as credenciais do Spotify.

O AuthSpotifyClient faz a requisição de autenticação e obtém o access_token.

O AlbumSpotifyClient usa esse token para buscar os álbuns recém-lançados.

A resposta é retornada ao usuário no formato JSON.

📚 Tecnologias Utilizadas

Java 21+

Spring Boot

Spring Cloud OpenFeign

Spotify API

Jackson para serialização/deserialização JSON