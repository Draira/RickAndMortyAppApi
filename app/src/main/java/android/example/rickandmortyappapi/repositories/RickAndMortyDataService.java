package android.example.rickandmortyappapi.repositories;

import android.content.Context;
import android.example.rickandmortyappapi.interfaces.DataListElements;
import android.example.rickandmortyappapi.interfaces.SpecificEpisode;
import android.example.rickandmortyappapi.models.Characters;
import android.example.rickandmortyappapi.models.Episodes;
import android.example.rickandmortyappapi.models.Locations;
import android.example.rickandmortyappapi.singleton.MySingleton;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RickAndMortyDataService {

    String TAG = "RickAndMortyDataService";

    Context context;

    //TODO order listener and structure of this class
    //Listeners
    DataListElements dataListElements;
    SpecificEpisode specificEpisodeInterface;

    //Empty constructor
    public RickAndMortyDataService() {
    }

    //Constructor
    public RickAndMortyDataService(Context context, DataListElements dataListElements) {
        this.context = context;
        this.dataListElements = dataListElements;
    }

    //Setter of listener
    public void setNameEpisodeInterface(SpecificEpisode specificEpisodeInterface) {
        this.specificEpisodeInterface = specificEpisodeInterface;
    }

    //METHODS

    public void RequestCharacters(){

        Log.v(TAG, "RequestCharacters " );
        String JSON_URL = "https://rickandmortyapi.com/api/character/?page=1";
        List<Characters> charactersList = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray characters = response.getJSONArray("results");

                            for(int i = 0; i < characters.length(); i++){

                                Characters modelCharacter = new Characters();

                                JSONObject singleCharacter = characters.getJSONObject(i); //Get every character
                                modelCharacter.setId(singleCharacter.getInt("id"));
                                modelCharacter.setName(singleCharacter.getString("name"));
                                modelCharacter.setStatus(singleCharacter.getString("status"));
                                modelCharacter.setSpecies(singleCharacter.getString("species"));
                                modelCharacter.setType(singleCharacter.getString("type"));
                                modelCharacter.setGender(singleCharacter.getString("gender"));
                                modelCharacter.setImage(singleCharacter.getString("image"));
                                modelCharacter.setUrl(singleCharacter.getString("url"));
                                modelCharacter.setCreated(singleCharacter.getString("created"));

                                ArrayList<String> dumi_origin = new ArrayList<>();
                                JSONObject origin = singleCharacter.getJSONObject("origin");
                                dumi_origin.add( origin.getString("name"));
                                dumi_origin.add( origin.getString("url"));
                                modelCharacter.setOrigin(dumi_origin);

                                ArrayList<String> dumi_location = new ArrayList<>();
                                JSONObject location = singleCharacter.getJSONObject("location");
                                dumi_location.add( location.getString("name"));
                                dumi_location.add( location.getString("url"));
                                //Toast.makeText(context, "Nombre del lugar: " + dumi_location.get(0), Toast.LENGTH_SHORT).show();
                                modelCharacter.setLocation(dumi_location);

                                ArrayList<String> dumi_episode = new ArrayList<>();
                                JSONArray episode = singleCharacter.getJSONArray("episode");
                                for(int j = 0; j < episode.length(); j++){
                                    //Toast.makeText(MainActivity.this, "" + episode.get(j), Toast.LENGTH_SHORT).show();

                                    dumi_episode.add(episode.getString(j));
                                }
                                modelCharacter.setEpisodes(dumi_episode);

                                charactersList.add(modelCharacter);
                            }

                            dataListElements.dataCharacters(charactersList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public void RequestEpisodes(){
        Log.v(TAG, "RequestEpisodes " );
        String JSON_URL = "https://rickandmortyapi.com/api/episode?page=1";
        List<Episodes> episodesList = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray episodes = response.getJSONArray("results");

                            for(int i = 0; i < episodes.length(); i++){

                                Episodes modelEpisode = new Episodes();

                                JSONObject singleEpisode = episodes.getJSONObject(i); //Get every character
                                modelEpisode.setId(singleEpisode.getInt("id"));
                                modelEpisode.setName(singleEpisode.getString("name"));

                                episodesList.add(modelEpisode);
                            }

                            dataListElements.dataEpisodes(episodesList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public void RequesLocations(){
        Log.v(TAG, "RequestEpisodes" );
        String JSON_URL = "https://rickandmortyapi.com/api/location?page=1";
        List<Locations> locationsList = new ArrayList<>();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray episodes = response.getJSONArray("results");

                            for(int i = 0; i < episodes.length(); i++){

                                Locations modelLocation = new Locations();

                                JSONObject singleEpisode = episodes.getJSONObject(i); //Get every character
                                modelLocation.setId(singleEpisode.getInt("id"));
                                modelLocation.setName(singleEpisode.getString("name"));

                                locationsList.add(modelLocation);
                            }

                            dataListElements.dataLocations(locationsList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(request);

    }

    public void RequestForAnEpisode(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Episodes modelEpisode = new Episodes();
                            String name = response.getString("name");
                            int id = response.getInt("id");
                            modelEpisode.setId(id);
                            modelEpisode.setName(name);

                            specificEpisodeInterface.getModelEpisode(modelEpisode);

                            if(specificEpisodeInterface != null){
                                specificEpisodeInterface.getNameEpisode(name);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(request);
    }


}
