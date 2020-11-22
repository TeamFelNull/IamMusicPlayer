package red.felnull.imp.client.util;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import red.felnull.imp.client.data.YoutubeData;

import java.util.List;

public class YoutubeUtils {
    public static List<SearchResult> getVideoSearchResults(String searchText) {
        try {
            YouTube.Search.List search = YoutubeData.getYoutube().search().list("id,snippet");
            search.setKey(YoutubeData.getYoutubeAPIKey());
            search.setQ(searchText);
            search.setType("video");
            search.setMaxResults(25l);
            SearchListResponse searchResponse = search.execute();
            return searchResponse.getItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
