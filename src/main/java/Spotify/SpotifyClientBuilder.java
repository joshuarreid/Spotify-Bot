package Spotify;

import Spotify.SpotifyCredentials;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@ComponentScan(basePackageClasses = SpotifyClientBuilder.class)
public class SpotifyClientBuilder {
    private SpotifyApi spotifyApi;
    private ClientCredentialsRequest clientCredentialsRequest;

    public SpotifyClientBuilder() {

        this.spotifyApi = spotifyApi = new SpotifyApi.Builder()
                .setClientId(SpotifyCredentials.clientId)
                .setClientSecret(SpotifyCredentials.clientSecret)
                .build();

        this.clientCredentialsRequest = spotifyApi.clientCredentials()
                .build();
    }

    private String getAccessToken() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
            return clientCredentials.getAccessToken();

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            return "Error: " + e.getMessage();
        }

    }

    private void setClientCredentials() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Set access token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public SpotifyApi build() {
        getAccessToken();
        setClientCredentials();
        return spotifyApi;
    }







}
