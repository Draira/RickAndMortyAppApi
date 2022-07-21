package android.example.rickandmortyappapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.example.rickandmortyappapi.adapters.Adapter;
import android.example.rickandmortyappapi.interfaces.DataListElements;
import android.example.rickandmortyappapi.models.Characters;
import android.example.rickandmortyappapi.models.Episodes;
import android.example.rickandmortyappapi.models.Locations;
import android.example.rickandmortyappapi.repositories.RickAndMortyDataService;
import android.example.rickandmortyappapi.singleton.MySingleton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements DataListElements {
    String TAG = "Pagination";
    String TAG2 = "Click_charcter";

    //List of data elements
    List<Characters> charactersList;
    List<Episodes> episodesList;
    List<Locations> locationsList;

    //Holds data
    RecyclerView recyclerView;
    Adapter adapter;
    LinearLayoutManager layoutManager;

    //Buttons for search data
    Button getCharactersButton;
    Button getEpisodesButton;
    Button getLocationsButton;

    //Variables to indicate what button is clicked
    int button_character_is_clicked = 0;
    int button_episode_is_clicked = 0;
    int button_location_is_clicked = 0;

    //Pagination Variables of scroll listener
    private int totalItemCount;
    private int firstVisibleItemCount;
    private int visibleItemCount;
    //global pagination variables
    private int page = 1;
    private int previousTotal;
    private boolean load = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViewVariables();
        layoutManager = new LinearLayoutManager(MainActivity.this);
        clickingButtons();
    }

    //Link the variables with the view elements
    private void setViewVariables() {
        recyclerView = findViewById(R.id.recycler_view);
        getCharactersButton = findViewById(R.id.button_characters);
        getEpisodesButton = findViewById(R.id.button_episodes);
        getLocationsButton = findViewById(R.id.button_locations);
    }

    //Listener for each button
    private void clickingButtons() {
        getCharactersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset variables of pagination
                page = 1;
                previousTotal = 0;
                load = true;
                //Set and Reset variables of button activation
                button_character_is_clicked = 1;
                button_episode_is_clicked = 0;
                button_location_is_clicked = 0;
                //Cleaning List
                charactersList = new ArrayList<>();
                episodesList = new ArrayList<>();
                locationsList = new ArrayList<>();
                //Change color of buttons
                getCharactersButton.setBackgroundColor(getResources().getColor(R.color.almost_white));
                getEpisodesButton.setBackgroundColor(getResources().getColor(R.color.light_grey));
                getLocationsButton.setBackgroundColor(getResources().getColor(R.color.light_grey));
                extractDataCharacter();
            }
        });

        getEpisodesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Reset variables of pagination
                page = 1;
                previousTotal = 0;
                load = true;
                //Set and Reset variables of button activation
                button_character_is_clicked = 0;
                button_episode_is_clicked = 1;
                button_location_is_clicked = 0;
                //Cleaning List
                charactersList = new ArrayList<>();
                episodesList = new ArrayList<>();
                locationsList = new ArrayList<>();
                //Change color of buttons
                getCharactersButton.setBackgroundColor(getResources().getColor(R.color.light_grey));
                getEpisodesButton.setBackgroundColor(getResources().getColor(R.color.almost_white));
                getLocationsButton.setBackgroundColor(getResources().getColor(R.color.light_grey));

                extractDataEpisode();
            }
        });

        getLocationsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Reset variables of pagination
                page = 1;
                previousTotal = 0;
                load = true;
                //Set and Reset variables of button activation
                button_character_is_clicked = 0;
                button_episode_is_clicked = 0;
                button_location_is_clicked = 1;
                //Cleaning List
                charactersList = new ArrayList<>();
                episodesList = new ArrayList<>();
                locationsList = new ArrayList<>();
                //Change color of buttons
                getCharactersButton.setBackgroundColor(getResources().getColor(R.color.light_grey));
                getEpisodesButton.setBackgroundColor(getResources().getColor(R.color.light_grey));
                getLocationsButton.setBackgroundColor(getResources().getColor(R.color.almost_white));

                extractDataLocation();
            }
        });
    }

    //Extract Data Characters from the API
    private void extractDataCharacter() {
        RickAndMortyDataService rickAndMortyDataService = new RickAndMortyDataService(this, this);
        rickAndMortyDataService.RequestCharacters();
    }

    //Extract Data Episodes from the API
    private void extractDataEpisode(){
        RickAndMortyDataService rickAndMortyDataService = new RickAndMortyDataService(this, this);
        rickAndMortyDataService.RequestEpisodes();
    }

    //Extract Data Locations from the API
    private void extractDataLocation(){
        RickAndMortyDataService rickAndMortyDataService = new RickAndMortyDataService(this, this);
        rickAndMortyDataService.RequesLocations();
    }


    //TODO make a pagination class
    private void pagination(){

        //Listener of the recycler to know when load more data
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            //Listener if you scroll
            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                firstVisibleItemCount = layoutManager.findFirstVisibleItemPosition();//firs element in the screen view
                visibleItemCount = layoutManager.getChildCount();//visible elements in the screen view
                totalItemCount = layoutManager.getItemCount(); //all elements in view


                Log.d(TAG, "----------first if-----------");
                Log.d(TAG, "load: " + load);

                if(load){//if load is true then prepare next element to load (without loading)

                    Log.d(TAG2, "INSIDE LOAD TRUE: totalItemCount: " + totalItemCount + " > " + "previousTotal: " + previousTotal );

                    if(totalItemCount > previousTotal){

                        Log.d(TAG2, "Changed page and load, totalItemCount = "+totalItemCount+ " previousTotal " + previousTotal);

                        previousTotal = totalItemCount;
                        page++;
                        load = false;

                        Log.d(TAG2, "Changed page and load, page = "+page+ " load " + load);
                    }
                }
                Log.d(TAG, "----------second if-----------" );
                Log.d(TAG, "firstVisibleItemCount: " + firstVisibleItemCount + " - visibleItemCount : " + visibleItemCount + " - totalItemCount: " + totalItemCount) ;

                //If load is false the new data is waiting to be loaded and...
                //...if you are at the bottom of the currently loaded data scroll
                if(!load && (firstVisibleItemCount + visibleItemCount) >= totalItemCount){
                    getNext(); //load new data
                    load = true; //data was loaded

                    Log.d(TAG2, "INSIDE LOAD FALSE: Load new data, page = "+page+ " load " + load);
                }
            }
        });
    }

    //TODO make a pagination class and add this method
    private void getNext() {

        String JSON_URL_CHARACTER = "https://rickandmortyapi.com/api/character/?page=";
        String JSON_URL_EPISODES = "https://rickandmortyapi.com/api/episode?page=";
        String JSON_URL_LOCATION = "https://rickandmortyapi.com/api/location?page=";

        String JSON_URL = "";

        if(button_character_is_clicked == 1){
            JSON_URL = JSON_URL_CHARACTER + page;
            Log.d(TAG2, "1URL");
        }else if(button_episode_is_clicked == 1){
            JSON_URL = JSON_URL_EPISODES + page;
            Log.d(TAG2, "2URL");
        }else if(button_location_is_clicked == 1){
            JSON_URL = JSON_URL_LOCATION + page;
            Log.d(TAG2, "3URL");
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, JSON_URL,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray results = response.getJSONArray("results");

                            for(int i = 0; i < results.length(); i++){

                                if(button_character_is_clicked == 1){

                                    Characters modelCharacter = new Characters();

                                    JSONObject singleCharacter = results.getJSONObject(i); //Get every character

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
                                    modelCharacter.setLocation(dumi_location);

                                    ArrayList<String> dumi_episode = new ArrayList<>();
                                    JSONArray episode = singleCharacter.getJSONArray("episode");
                                    for(int j = 0; j < episode.length(); j++){
                                        dumi_episode.add(episode.getString(j));
                                    }
                                    modelCharacter.setEpisodes(dumi_episode);

                                    charactersList.add(modelCharacter);

                                }else if(button_episode_is_clicked == 1){

                                    Episodes modelEpisode = new Episodes();

                                    JSONObject singleEpisode = results.getJSONObject(i); //Get every episode
                                    Log.d(TAG2,""+ singleEpisode);
                                    modelEpisode.setId(singleEpisode.getInt("id"));
                                    modelEpisode.setName(singleEpisode.getString("name"));
                                    modelEpisode.setAir_date(singleEpisode.getString("air_date"));
                                    modelEpisode.setEpisode(singleEpisode.getString("episode"));
                                    modelEpisode.setUrl(singleEpisode.getString("url"));
                                    modelEpisode.setCreated(singleEpisode.getString("created"));


                                    ArrayList<String> dumi_character = new ArrayList<>();
                                    JSONArray characters = singleEpisode.getJSONArray("characters");
                                    for(int j = 0; j < characters.length(); j++){

                                        dumi_character.add(characters.getString(j));
                                    }
                                    modelEpisode.setCharacters(dumi_character);

                                    episodesList.add(modelEpisode);


                                }else if(button_location_is_clicked == 1){
                                    Locations modelLocation = new Locations();

                                    JSONObject singleLocation = results.getJSONObject(i); //Get every location

                                    modelLocation.setId(singleLocation.getInt("id"));
                                    modelLocation.setName(singleLocation.getString("name"));
                                    modelLocation.setType(singleLocation.getString("type"));
                                    modelLocation.setDimension(singleLocation.getString("dimension"));
                                    modelLocation.setUrl(singleLocation.getString("url"));
                                    modelLocation.setCreated(singleLocation.getString("created"));

                                    ArrayList<String> dumi_residents = new ArrayList<>();
                                    JSONArray residents = singleLocation.getJSONArray("residents");
                                    for(int j = 0; j < residents.length(); j++){
                                        dumi_residents.add(residents.getString(j));
                                    }
                                    modelLocation.setResidents(dumi_residents);

                                    locationsList.add(modelLocation);
                                }
                            }
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "error: " + error, Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(request);
    }


    //Load character data into view
    @Override
    public void dataCharacters(List<Characters> characters) {
        this.charactersList = characters;

        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(MainActivity.this); //Adapter is created is each new click
        adapter.setTypeAdapter(0);
        adapter.setCharactersList(characters);
        recyclerView.setAdapter(adapter);

        pagination();
    }

    //Load episode data into view
    @Override
    public void dataEpisodes(List<Episodes> episodes) {
        this.episodesList = episodes;

        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(MainActivity.this); //Adapter is created is each new click
        adapter.setTypeAdapter(1);
        adapter.setEpisodesList(episodes);
        recyclerView.setAdapter(adapter);

        pagination();
    }

    //Load location data into view
    @Override
    public void dataLocations(List<Locations> locations) {
        this.locationsList = locations;

        recyclerView.setLayoutManager(layoutManager);
        adapter = new Adapter(MainActivity.this); //Adapter is created is each new click
        adapter.setTypeAdapter(2);
        adapter.setLocationsList(locations);
        recyclerView.setAdapter(adapter);

        pagination();
    }
}