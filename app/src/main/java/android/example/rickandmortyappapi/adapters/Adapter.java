package android.example.rickandmortyappapi.adapters;

import android.content.Context;
import android.example.rickandmortyappapi.DialogFragmentCharacter;
import android.example.rickandmortyappapi.R;
import android.example.rickandmortyappapi.interfaces.SpecificEpisode;
import android.example.rickandmortyappapi.models.Characters;
import android.example.rickandmortyappapi.models.Episodes;
import android.example.rickandmortyappapi.models.Locations;
import android.example.rickandmortyappapi.repositories.RickAndMortyDataService;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    String TAG = "Adapter";

    private List<Characters> charactersList = new ArrayList<>();
    private List<Episodes> episodesList = new ArrayList<>();
    private List<Locations> locationsList = new ArrayList<>();

    private Context mContext;
    private int typeAdapter;



    //Constructor
    public Adapter(Context mContext) {
        this.mContext = mContext;
    }



    //Setters
    public void setCharactersList(List<Characters> charactersList) {
        this.charactersList = charactersList;
    }

    public void setEpisodesList(List<Episodes> episodesList) {
        this.episodesList = episodesList;
    }

    public void setLocationsList(List<Locations> locationsList) {
        this.locationsList = locationsList;
    }

    public void setTypeAdapter(int typeAdapter) {
        this.typeAdapter = typeAdapter;
    }







    //Set the layout on the view
    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        Log.d(TAG,  "typeAdapter onCreateViewHolder: " + typeAdapter);
        View view;

        if(typeAdapter == 0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_character, parent, false);
            CharacterViewHolder characterViewHolder = new CharacterViewHolder(view);
            return characterViewHolder;
        }else if(typeAdapter == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_episode, parent, false);
            EpisodeViewHolder episodeViewHolder = new EpisodeViewHolder(view);
            return episodeViewHolder;
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_location, parent, false);
            LocationViewHolder locationViewHolder = new LocationViewHolder(view);
            return locationViewHolder;
        }
    }


    //Load the data in the layout
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG,  "typeAdapter onBindViewHolder: " + typeAdapter);
        if (holder instanceof CharacterViewHolder && typeAdapter == 0){
            ((CharacterViewHolder) holder).nameCharacter.setText(charactersList.get(position).getName());
            Picasso.get().load(charactersList.get(position).getImage()).into(((CharacterViewHolder) holder).imageCharacter);

            ((CharacterViewHolder) holder).lastLocationCharacter.setText(charactersList.get(position).getLocation().get(0));
            ((CharacterViewHolder) holder).statusCharacter.setText(charactersList.get(position).getStatus());
            ((CharacterViewHolder) holder).speciesCharacter.setText(charactersList.get(position).getSpecies());

            String status = charactersList.get(position).getStatus();
            Log.d(TAG, "status: " + status);
            if(status.equals("Alive")){
                Log.d(TAG, "alive");
                ((CharacterViewHolder) holder).imageViewPoint.setColorFilter(Color.GREEN);
            }else if(status.equals("Dead")){
                ((CharacterViewHolder) holder).imageViewPoint.setColorFilter(Color.RED);
            }else if(status.equals("unknown")){
                ((CharacterViewHolder) holder).imageViewPoint.setColorFilter(Color.LTGRAY);
            }

            //First episode of each character
            String episode = charactersList.get(position).getEpisodes().get(0);//I have the first episode
            final String[] nameEpisode = {""};
            RickAndMortyDataService rickAndMortyDataService = new RickAndMortyDataService();
            rickAndMortyDataService.RequestForAnEpisode(episode);
            rickAndMortyDataService.setNameEpisodeInterface(new SpecificEpisode() {
                @Override
                public void getNameEpisode(String name) {
                    ((CharacterViewHolder) holder).firstSeenCharacter.setText(name);
                    nameEpisode[0] = name;
                    Log.d(TAG, name);
                }

                @Override
                public void getModelEpisode(Episodes episodes) {

                }
            });

            ((CharacterViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Create a new dialog fragment
                    FragmentTransaction ft = ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction();
                    Fragment prev = ((AppCompatActivity) mContext).getSupportFragmentManager().findFragmentByTag("dialog");
                    if (prev != null) {
                        ft.remove(prev);
                    }
                    ft.addToBackStack(null);

                    //And load the data of the character in the dialog fragment
                    DialogFragmentCharacter dialogFragment = new DialogFragmentCharacter();
                    dialogFragment.setImage(charactersList.get(position).getImage());
                    dialogFragment.setName(charactersList.get(position).getName());
                    dialogFragment.setStatus(charactersList.get(position).getStatus());
                    dialogFragment.setSpecies(charactersList.get(position).getSpecies());
                    dialogFragment.setLocation(charactersList.get(position).getLocation());
                    dialogFragment.setEpisodes(charactersList.get(position).getEpisodes());
                    dialogFragment.setFistSeen(nameEpisode[0]);
                    dialogFragment.setGender(charactersList.get(position).getGender());
                    dialogFragment.setType(charactersList.get(position).getType());
                    dialogFragment.setOrigin(charactersList.get(position).getOrigin());
                    dialogFragment.show(ft, "dialog");
                }
            });


        }
        else if(holder instanceof EpisodeViewHolder && typeAdapter == 1){
            ((EpisodeViewHolder) holder).idEpisode.setText(String.valueOf(episodesList.get(position).getId()));
            ((EpisodeViewHolder) holder).nameEpisode.setText(episodesList.get(position).getName());

            //click the model
            ((EpisodeViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Coming soon...", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(holder instanceof LocationViewHolder && typeAdapter == 2){
            ((LocationViewHolder) holder).idLocation.setText(String.valueOf(locationsList.get(position).getId()));
            ((LocationViewHolder) holder).nameLocation.setText(locationsList.get(position).getName());

            //click the model
            ((LocationViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Coming soon...", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        Log.d(TAG,  "typeAdapter getItemCount: " + typeAdapter);
        int size = 0;
        if(typeAdapter == 0){
            size = charactersList.size();
        }else if(typeAdapter == 1){
            size = episodesList.size();
        }else if(typeAdapter == 2){
            size = locationsList.size();
        }
        return size;
    }


    //Viewholders
    private static class CharacterViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView imageCharacter;
        ImageView imageViewPoint;
        TextView nameCharacter;
        TextView statusCharacter;
        TextView speciesCharacter;
        TextView lastLocationCharacter;
        TextView firstSeenCharacter;

        public CharacterViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageCharacter = itemView.findViewById(R.id.image_character);
            imageViewPoint = itemView.findViewById(R.id.status_point_character);
            nameCharacter = itemView.findViewById(R.id.name_character);
            statusCharacter = itemView.findViewById(R.id.status_character);
            speciesCharacter = itemView.findViewById(R.id.species_character);
            lastLocationCharacter = itemView.findViewById(R.id.last_location_character);
            firstSeenCharacter = itemView.findViewById(R.id.first_seen_character);
        }
    }

    private static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        TextView idEpisode;
        TextView nameEpisode;

        public EpisodeViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            idEpisode = itemView.findViewById(R.id.id_episode);
            nameEpisode = itemView.findViewById(R.id.name_episode);
        }
    }

    private static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView idLocation;
        TextView nameLocation;

        public LocationViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            nameLocation = itemView.findViewById(R.id.name_location);
            idLocation = itemView.findViewById(R.id.id_location);
        }
    }
}
