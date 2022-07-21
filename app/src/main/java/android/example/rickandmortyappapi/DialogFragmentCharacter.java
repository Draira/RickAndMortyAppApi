package android.example.rickandmortyappapi;

import android.example.rickandmortyappapi.adapters.Adapter;
import android.example.rickandmortyappapi.interfaces.SpecificEpisode;
import android.example.rickandmortyappapi.models.Episodes;
import android.example.rickandmortyappapi.repositories.RickAndMortyDataService;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DialogFragmentCharacter extends DialogFragment {
    //Elements of view
    ShapeableImageView imageCharacter;
    ImageView imageViewPoint;
    TextView nameCharacter;
    TextView statusCharacter;
    TextView speciesCharacter;
    TextView subspaciesCharacter;
    TextView genderCharacter;
    TextView originCharacter;
    TextView lastLocationCharacter;
    TextView firstSeenCharacter;

    //Load data in the screen
    RecyclerView recyclerView;
    Adapter adapter;
    LinearLayoutManager layoutManager;

    //Variables to load elements of the view
    private String name;
    private String status;
    private String species;
    private String type;
    private String gender;
    private ArrayList<String> origin;
    private ArrayList<String> location;
    private String image;
    private ArrayList<String> episodes;
    private String fistSeen;

    //Empty constructor
    public DialogFragmentCharacter() {
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dialog_character, container, false);

        setViewVariables(view);
        layoutManager = new LinearLayoutManager(getContext());

        return view;
    }

    private void setViewVariables(View view) {
        imageCharacter = view.findViewById(R.id.image_character);

        imageViewPoint = view.findViewById(R.id.status_point_character);
        nameCharacter = view.findViewById(R.id.name_character);
        statusCharacter = view.findViewById(R.id.status_character);
        speciesCharacter = view.findViewById(R.id.species_character);
        lastLocationCharacter = view.findViewById(R.id.last_location_character);
        firstSeenCharacter = view.findViewById(R.id.first_seen_character);
        genderCharacter = view.findViewById(R.id.gender_character);
        subspaciesCharacter = view.findViewById(R.id.subspecies_character);
        originCharacter = view.findViewById(R.id.origin_character);

        recyclerView = view.findViewById(R.id.recycler_dialog);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.get().load(image).into(imageCharacter);
        nameCharacter.setText(name);
        statusCharacter.setText(status);
        speciesCharacter.setText(species);
        lastLocationCharacter.setText(location.get(0));
        firstSeenCharacter.setText(fistSeen);
        genderCharacter.setText(gender);
        subspaciesCharacter.setText(type);
        originCharacter.setText(origin.get(0));

        //Load episodes
        if(!episodes.isEmpty()){
            ArrayList<Episodes> episodesArrayList = new ArrayList<>();
            ArrayList<String> nameEpisodes = new ArrayList<>();
            for(int i = 0; i < episodes.size(); i++){
                RickAndMortyDataService rickAndMortyDataService = new RickAndMortyDataService();
                rickAndMortyDataService.RequestForAnEpisode(episodes.get(i));
                rickAndMortyDataService.setNameEpisodeInterface(new SpecificEpisode() {
                    @Override
                    public void getNameEpisode(String name) {
                        nameEpisodes.add(name);
                    }

                    @Override
                    public void getModelEpisode(Episodes episodes) {
                        episodesArrayList.add(episodes);
                    }
                });
            }

            recyclerView.setLayoutManager(layoutManager);
            adapter = new Adapter(getContext());
            adapter.setTypeAdapter(1);
            adapter.setEpisodesList(episodesArrayList);
            recyclerView.setAdapter(adapter);
        }

        //Change color of imageview according to status
        if(status.equals("Alive")){
            imageViewPoint.setColorFilter(Color.GREEN);
        }else if(status.equals("Dead")){
            imageViewPoint.setColorFilter(Color.RED);
        }else if(status.equals("unknown")){
            imageViewPoint.setColorFilter(Color.LTGRAY);
        }
    }

    
    //Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setOrigin(ArrayList<String> origin) {
        this.origin = origin;
    }

    public void setLocation(ArrayList<String> location) {
        this.location = location;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setEpisodes(ArrayList<String> episodes) {
        this.episodes = episodes;
    }

    public void setFistSeen(String fistSeen) {
        this.fistSeen = fistSeen;
    }
}