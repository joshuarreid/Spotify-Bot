package Spotify;


import com.wrapper.spotify.SpotifyApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("spotifyService")
public class SpotifyService {

    @Autowired
    private static SpotifyClientBuilder clientBuilder;
    private static SpotifyApi spotifyApi;

    public static void startService() {
        clientBuilder = new SpotifyClientBuilder();
        spotifyApi = clientBuilder.build();
    }

    public static void refreshService() {
        if (spotifyApi != null) {
            spotifyApi.setRefreshToken(spotifyApi.getAccessToken());
        }
    }

    public static SpotifyApi getSpotifyApi() {
        if (spotifyApi != null) {
            return spotifyApi;
        } else {
            startService();
            return spotifyApi;
        }
    }


}
