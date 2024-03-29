package com.example.concertsystem.service.artist;

import com.example.concertsystem.constants.GlobalConstants;
import com.example.concertsystem.dto.ArtistResponse;
import com.example.concertsystem.entity.Artist;
import com.example.concertsystem.exception.ResourceNotFoundException;
import com.example.concertsystem.exception_handling.classes.ArtistNotFoundException;
import com.faunadb.client.FaunaClient;
import com.faunadb.client.types.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
    public Map<String, String> addArtist(String name, String userName, String email, String govId, String profileImg) {
        Map<String, String> map = new HashMap<>();
        try {
            Value val = retrieveValue(userName);
            if (val != null) {
                map.put("val", "exists");
                map.put("id", val.at("ref").to(Value.RefV.class).get().getId());
                return map;
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
            map.put("val", "true");
            map.put("id", res.at("ref").to(Value.RefV.class).get().getId());
            return map;
        }catch(Exception e){
            throw new RuntimeException(GlobalConstants.MESSAGE_417_POST);
        }
    }

    @Override
    public List<String> addArtistList(List<Artist> artistList, List<String> profileImages){
        try {
            List<String> artistIds = new ArrayList<>();
            int i = 0;
            for (Artist artist : artistList) {
                String id = addArtist(artist.name(), artist.userName(), artist.email(), artist.govId(), profileImages.get(i)).get("id");
                i++;
                artistIds.add(id);
            }
            return artistIds;
        }catch (Exception e){
            throw new RuntimeException(GlobalConstants.MESSAGE_500);
        }
    }

    @Override
    public ArtistResponse getArtistById(String id) throws ArtistNotFoundException {
        try {
            Value res = faunaClient.query(Get(Ref(Collection("Artist"), id))).get();

            return new ArtistResponse(
                    res.at("ref").to(Value.RefV.class).get().getId(),
                    res.at("data", "name").to(String.class).get(),
                    res.at("data", "userName").to(String.class).get(),
                    res.at("data", "email").to(String.class).get(),
                    res.at("data", "govId").to(String.class).get(),
                    res.at("data", "profileImg").to(String.class).get()
            );
        } catch (Exception e) {
            throw new ResourceNotFoundException("Artist","Id",id);
        }
    }

    @Override
    public String getArtistIdByName(String artist){
        try {
            Value res = faunaClient.query(
                    Get(
                            Match(Index("artist_by_userName"), Value(artist))
                    )
            ).get();
            return res.at("ref").to(Value.RefV.class).get().getId();
        }catch (Exception e){
            throw new ResourceNotFoundException("Artist","ArtistName",artist);
        }
    }

    @Override
    public List<ArtistResponse> getAllArtist(){
        try {
            CompletableFuture<Value> res = faunaClient.query(
                    Map(
                            Paginate(Documents(Collection("Artist"))),
                            Lambda("artistRef", Get(Var("artistRef")))
                    )
            );

            List<Value> value = (List<Value>) res.join().at("data").to(List.class).get();

            List<ArtistResponse> artistList = new ArrayList<>();
            for (Value val : value) {
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
        } catch (Exception e) {
            throw new RuntimeException(GlobalConstants.MESSAGE_500);
        }
    }

    @Override
    public List<ArtistResponse> getArtistsByIds(List<String> artistIds){
        int i=0;
        try {
            List<ArtistResponse> artistList = new ArrayList<>();
            for (String id : artistIds) {
                i++;
                ArtistResponse artist = getArtistById(id);
                artistList.add(artist);
            }
            return artistList;
        }catch (Exception e){
            throw new ResourceNotFoundException("Artist","ArtistId",artistIds.get(i));
        }
    }

    @Override
    public boolean updateArtist(String id, String name, String userName, String email, String govId, String profileImg){
        try {
            getArtistById(id);
            try {
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
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Artist","Id",id);
        }

    }

    @Override
    public boolean deleteArtist(String id) {
        try {
            getArtistById(id);
            try {
                Value val = faunaClient.query(Delete(Ref(Collection("Artist"), id))).get();
                if(val==null)
                    return false;
                return true;
            } catch (Exception e) {
                return false;
            }
        }catch (Exception e){
            throw new ResourceNotFoundException("Artist","Id",id);
        }
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
