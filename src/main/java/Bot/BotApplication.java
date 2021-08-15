package Bot;

import Spotify.SpotifyCredentials;
import Spotify.SpotifyService;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.specification.*;
import com.wrapper.spotify.requests.data.browse.GetRecommendationsRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfUsersPlaylistsRequest;
import org.apache.hc.core5.http.ParseException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.io.IOException;


@SpringBootApplication
@EnableConfigurationProperties
public class BotApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(BotApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		SpotifyApi spotifyApi = SpotifyService.getSpotifyApi();
		GetListOfUsersPlaylistsRequest getListOfUsersPlaylistsRequest = spotifyApi
				.getListOfUsersPlaylists(SpotifyCredentials.username).build();
		Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfUsersPlaylistsRequest.execute();
		Object tracks = playlistSimplifiedPaging.getItems()[0].getId();
	}



	public void createRecommendedPlaylist(SpotifyApi spotifyApi, String user) throws ParseException, SpotifyWebApiException, IOException {
		Paging<Artist> artistList = getTopArtists(spotifyApi);
		String topArtistId = artistList.getItems()[0].getId();
		GetRecommendationsRequest getRecommendationsRequest = spotifyApi.getRecommendations()
				.limit(10)
				.seed_artists(topArtistId)
				.build();

		Recommendations recommendationsList = getRecommendationsRequest.execute();

		//Create Playlist and Name it
			// newPlaylist = spotifyApi.createPlaylist("Week of " + Date.getDate() + " Playlist")

		//Add tracks to playlists from recommendationsList
			// spotifyApi.addTracksToPlaylist(newPlaylist.getId(), recommendationsList.getTracks())



	}

	public void getTopTracks(SpotifyApi spotifyApi, String user) {

	}

	public Paging<Artist> getTopArtists(SpotifyApi spotifyApi) {
		GetUsersTopArtistsRequest request = spotifyApi.getUsersTopArtists()
			.limit(10)
            .time_range("short_term")
			.build();

		try {
			Paging<Artist> artistList = request.execute();
			System.out.println("Total: " + artistList.getTotal());
			return artistList;
		} catch (IOException | SpotifyWebApiException | ParseException e) {
			System.out.println("Error: " + e.getMessage());
			return null;
		}
	}




}
