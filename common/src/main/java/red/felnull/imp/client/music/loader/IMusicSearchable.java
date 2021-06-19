package red.felnull.imp.client.music.loader;

import java.util.List;

public interface IMusicSearchable {

    List<IMusicLoader.SearchData> search(String identifier);

}
