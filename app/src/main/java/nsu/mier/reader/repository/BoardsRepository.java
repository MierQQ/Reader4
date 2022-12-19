package nsu.mier.reader.repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import nsu.mier.reader.entity.Board;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BoardsRepository {
    private static BoardsRepository instance;
    private Request request;
    private String url = "https://a.4cdn.org/boards.json";
    private OkHttpClient client = SingletonOkHttpClient.getClient();
    private boolean isSFW = true;

    public static BoardsRepository getInstance() {
        if (instance == null) {
            instance = new BoardsRepository();
        }
        return instance;
    }

    private BoardsRepository() {
        request = new Request.Builder()
                .url(url)
                .build();
    }

    public void loadBoards(Callback<List<Board>> callback, Context context) {
        List<Board> boards;
        try (ObjectInputStream file = new ObjectInputStream(new FileInputStream(new File(context.getExternalFilesDir(null), "FavoriteBoards.obj")))) {
            boards = (List<Board>) file.readObject();
        } catch (IOException | ClassNotFoundException e) {
            boards = new ArrayList<>();
            Toast.makeText(context, "Failed to load favorite boards", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Log.d("Boards loaded len ", boards.size() + "");
        callback.Invoke(boards);
    }

    public void saveBoards(List<Board> boards, Context context) {
        try (ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(new File(context.getExternalFilesDir(null), "FavoriteBoards.obj")))) {
            file.writeObject(boards);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save boards", Toast.LENGTH_SHORT).show();
        }
    }

    public void getBoards(Callback<List<Board>> callback, Context context) {
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(context, "Failed to get boards", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        List<Board> boards = new ArrayList<>();
                        JSONArray array = json.getJSONArray("boards");
                        for (int i = 0; i < array.length(); ++i) {
                            if (isSFW && array.getJSONObject(i).getInt("ws_board") != 1) {
                                continue;
                            }
                            String board = array.getJSONObject(i).getString("board");
                            String title = array.getJSONObject(i).getString("title");
                            String info = array.getJSONObject(i).getString("meta_description");
                            info = StringEscapeUtils.unescapeHtml4(info);
                            Integer pages = array.getJSONObject(i).getInt("pages");
                            Integer perPage = array.getJSONObject(i).getInt("per_page");
                            boards.add(new Board(board, title, info, pages, perPage));
                        }
                        callback.Invoke(boards);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Failed to get boards", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
