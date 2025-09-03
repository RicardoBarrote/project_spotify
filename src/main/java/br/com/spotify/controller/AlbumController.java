package br.com.spotify.controller;

import br.com.spotify.client.AlbumResponse;
import br.com.spotify.client.AlbumSpotifyClient;
import br.com.spotify.client.AuthSpotifyClient;
import br.com.spotify.client.LoginRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
