package android.example.rickandmortyappapi.interfaces;

import android.example.rickandmortyappapi.models.Characters;
import android.example.rickandmortyappapi.models.Episodes;
import android.example.rickandmortyappapi.models.Locations;

import java.util.List;

public interface DataListElements {
    public void dataCharacters(List<Characters> characters);
    public void dataEpisodes(List<Episodes> episodes);
    public void dataLocations(List<Locations> locations);
}
