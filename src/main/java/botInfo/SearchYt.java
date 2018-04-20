package botInfo;// Sample Java code for user authorization
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.*;
import com.google.api.services.youtube.YouTube;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class SearchYt{

    /** Application name. */
    private static final String APPLICATION_NAME = "BotDiscord";

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/drive-java-quickstart
     */
    private static final Collection<String> SCOPES = Arrays.asList("YouTubeScopes.https://www.googleapis.com/auth/youtube.force-ssl YouTubeScopes.https://www.googleapis.com/auth/youtubepartner");

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * Build and return an authorized API client service, such as a YouTube
     * Data API client service.
     * @return an authorized API client service
     * @throws IOException
     */
    public static YouTube getYouTubeService() throws IOException {
        //Credential credential = authorize();
        return new YouTube.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            @Override
            public void initialize(HttpRequest httpRequest) throws IOException {

            }
        }).setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static String getResult(String query) throws IOException {
        YouTube youtube = getYouTubeService();
        StringBuilder stb=new StringBuilder();
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("part", "snippet");
            parameters.put("maxResults", "5");
            parameters.put("q", query);
            parameters.put("type", "video");

            YouTube.Search.List searchListByKeywordRequest = youtube.search().list(parameters.get("part").toString());
            searchListByKeywordRequest.setKey("AIzaSyDL5vdPmPIZDxdDqwZcRTvbcYMGluZ8_0U");
            if (parameters.containsKey("maxResults")) {
                searchListByKeywordRequest.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));
            }

            if (parameters.containsKey("q") && parameters.get("q") != "") {
                searchListByKeywordRequest.setQ(parameters.get("q").toString());
            }

            if (parameters.containsKey("type") && parameters.get("type") != "") {
                searchListByKeywordRequest.setType(parameters.get("type").toString());
            }

            SearchListResponse response = searchListByKeywordRequest.execute();
            List<SearchResult> searchResultList = response.getItems();
            stb.append("Resultats\n");
            for (SearchResult result :searchResultList){
                stb.append("Titre : "+result.getSnippet().getTitle()+"\n");
                stb.append("https://www.youtube.com/watch?v="+result.getId().getVideoId()+"\n");
                stb.append("\n\n");
            }
        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            stb.append("Erreur query\n");
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            stb.append("Erreur query\n");
            t.printStackTrace();
        }
        return (stb.toString());
    }

    public static String getUrl(String query) throws IOException {
        YouTube youtube = getYouTubeService();
        StringBuilder stb=new StringBuilder();
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("part", "snippet");
            parameters.put("maxResults", "1");
            parameters.put("q", query);
            parameters.put("type", "video");

            YouTube.Search.List searchListByKeywordRequest = youtube.search().list(parameters.get("part").toString());
            searchListByKeywordRequest.setKey("AIzaSyDL5vdPmPIZDxdDqwZcRTvbcYMGluZ8_0U");
            if (parameters.containsKey("maxResults")) {
                searchListByKeywordRequest.setMaxResults(Long.parseLong(parameters.get("maxResults").toString()));
            }

            if (parameters.containsKey("q") && parameters.get("q") != "") {
                searchListByKeywordRequest.setQ(parameters.get("q").toString());
            }

            if (parameters.containsKey("type") && parameters.get("type") != "") {
                searchListByKeywordRequest.setType(parameters.get("type").toString());
            }

            SearchListResponse response = searchListByKeywordRequest.execute();
            List<SearchResult> searchResultList = response.getItems();
            for (SearchResult result :searchResultList){
                stb.append("https://www.youtube.com/watch?v="+result.getId().getVideoId());
            }
        } catch (GoogleJsonResponseException e) {
            e.printStackTrace();
            stb.append("Erreur query\n");
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
        } catch (Throwable t) {
            stb.append("Erreur query\n");
            t.printStackTrace();
        }
        return (stb.toString());
    }
}