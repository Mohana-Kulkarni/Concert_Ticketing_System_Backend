package com.example.concertsystem.service.artist;

import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.entity.Organiser;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.stereotype.Service;

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
    public void addArtist(String name, String userName, String email, String govId, String profileImg) {
        faunaClient.query(
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
        );
    }

    @Override
    public Artist getArtistById(String id) throws ExecutionException, InterruptedException {
        Value res = faunaClient.query(Get(Ref(Collection("Artist"), id))).get();

        return new Artist(
                res.at("ref").to(Value.RefV.class).get().getId(),
                res.at("data", "name").to(String.class).get(),
                res.at("data", "userName").to(String.class).get(),
                res.at("data", "email").to(String.class).get(),
                res.at("data", "govId").to(String.class).get(),
                res.at("data", "profileImg").to(String.class).get()
        );
    }

    @Override
    public void updateArtist(String id, String name, String userName, String email, String govId, String profileImg) throws ExecutionException, InterruptedException {
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
}
