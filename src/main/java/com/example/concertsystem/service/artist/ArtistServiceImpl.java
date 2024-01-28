package com.example.concertsystem.service.artist;

import com.example.concertsystem.dto.ArtistResponse;
import com.example.concertsystem.entity.Artist;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.faunadb.client.query.Language.*;
import static com.faunadb.client.query.Language.Value;

@Service
public class ArtistServiceImpl implements ArtistService{
    private FaunaClient faunaClient;

    public ArtistServiceImpl(FaunaClient faunaClient) {
        this.faunaClient= faunaClient;
    }

    @Override
    public String addArtist(String name, String userName, String email, String govId, String profileImg) throws ExecutionException, InterruptedException {
        Value val = retrieveValue(userName);
        if(val != null) {
            return val.at("ref").to(Value.RefV.class).get().getId();
        }

        Value res = faunaClient.query(
                Create(
                        Collection("Artist"),
                        Obj(
                                "data",
                                Obj(
                                        "name", Value(name),
                                        "userName", Value(userName),
                                        "email", Value(email),
                                        "govId", Value(govId),
                                        "profileImg", Value(profileImg)
                                )
                        )
                )
        ).get();
        return res.at("ref").to(Value.RefV.class).get().getId();
    }

    @Override
    public List<String> addArtistList(List<Artist> artistList, List<String> profileImages) throws ExecutionException, InterruptedException {
        List<String> artistIds = new ArrayList<>();
        int i = 0;
        for (Artist artist : artistList) {
            String id = addArtist(artist.name(), artist.userName(), artist.email(), artist.govId(), profileImages.get(i));
            i++;
            artistIds.add(id);
        }
        return artistIds;
    }

    @Override
    public ArtistResponse getArtistById(String id) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(Get(Ref(Collection("Artist"), id))).get();

        return new ArtistResponse(
                res.at("ref").to(Value.RefV.class).get().getId(),
                res.at("data", "name").to(String.class).get(),
                res.at("data", "userName").to(String.class).get(),
                res.at("data", "email").to(String.class).get(),
                res.at("data", "govId").to(String.class).get(),
                res.at("data", "profileImg").to(String.class).get()
        );
    }

    @Override
    public String getArtistIdByName(String artist) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(
                Get(
                        Match(Index("artist_by_userName"), Value(artist))
                )
        ).get();
        return res.at("ref").to(Value.RefV.class).get().getId();
    }

    @Override
    public List<ArtistResponse> getAllArtist() throws ExecutionException, InterruptedException {
        List<Value> res = (List<Value>) faunaClient.query(
                Map(
                        Paginate(Documents(Collection("Artist"))),
                        Lambda("artistRef", Get(Var("artistRef")))
                )
        ).get();

        List<ArtistResponse> artistList = new ArrayList<>();
        for (Value val : res) {
            ArtistResponse artist = new ArtistResponse(
                    val.at("ref").to(Value.RefV.class).get().getId(),
                    val.at("data", "name").to(String.class).get(),
                    val.at("data", "userName").to(String.class).get(),
                    val.at("data", "email").to(String.class).get(),
                    val.at("data", "govId").to(String.class).get(),
                    val.at("data", "profileImg").to(String.class).get()
            );
            artistList.add(artist);
        }
        return artistList;
    }

    @Override
    public List<ArtistResponse> getArtistsByIds(List<String> artistIds) throws ExecutionException, InterruptedException {
        List<ArtistResponse> artistList = new ArrayList<>();
        for (String id : artistIds) {
            ArtistResponse artist = getArtistById(id);
            artistList.add(artist);
        }
        return artistList;
    }

    @Override
    public void updateArtist(String id, String name, String userName, String email, String govId, MultipartFile profileImg) throws ExecutionException, InterruptedException {

        faunaClient.query(
                Update(Ref(Collection("Artist"), id),
                        Obj(
                                "data", Obj(
                                        "name", Value(name),
                                        "userName", Value(userName),
                                        "email", Value(email),
                                        "govId", Value(govId),
                                        "profileImg", Value(profileImg)
                                )
                        )
                )
        ).get();
    }

    @Override
    public void deleteArtist(String id) {
        faunaClient.query(Delete(Ref(Collection("Artist"), id)));
    }

    public Value retrieveValue(String userName) {
        try {
            Value val = faunaClient.query(
                    Get(
                            Match(
                                    Index("artist_by_userName"),
                                    Value(userName)
                            )
                    )
            ).get();
            return val;
        } catch (Exception e) {
            return null;
        }
    }
}
