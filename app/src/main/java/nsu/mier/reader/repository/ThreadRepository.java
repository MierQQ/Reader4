package nsu.mier.reader.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nsu.mier.reader.entity.Post;
import nsu.mier.reader.entity.ThreadPosts;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ThreadRepository {
    private static ThreadRepository instance;
    private String url = "https://a.4cdn.org";
    private OkHttpClient client = SingletonOkHttpClient.getClient();
    private boolean isSFW = true;

    public static ThreadRepository getInstance() {
        if (instance == null) {
            instance = new ThreadRepository();
        }
        return instance;
    }

    private ThreadRepository() {

    }

    public void loadThread(Callback<List<ThreadPosts>> callback, Context context) {
        List<ThreadPosts> thread;
        try (ObjectInputStream file = new ObjectInputStream(new FileInputStream(new File(context.getExternalFilesDir(null), "FavoriteThreads.obj")))) {
            thread = (List<ThreadPosts>) file.readObject();
        } catch (IOException | ClassNotFoundException e) {
            thread = new ArrayList<>();
            Toast.makeText(context, "Failed to load favorite thread", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        Log.d("Thread loaded len ", thread.size() + "");
        callback.Invoke(thread);
    }

    public void saveThread(List<ThreadPosts> thread, Context context) {
        try (ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(new File(context.getExternalFilesDir(null), "FavoriteThreads.obj")))) {
            file.writeObject(thread);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save thread", Toast.LENGTH_SHORT).show();
        }
    }

    public void getThreads(Callback<List<ThreadPosts>> callback, Context context, int page, String board) {
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        builder.addPathSegment(board);
        builder.addPathSegment(page + ".json");
        Request request = new Request.Builder()
                .url(builder.build())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(context, "Failed to get thread", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                        List<ThreadPosts> threads = new ArrayList<>();
                        JSONArray threadsJSON = json.getJSONArray("threads");
                        for (int i = 0; i < threadsJSON.length(); ++i) {
                            JSONArray postsJSON = threadsJSON.getJSONObject(i).getJSONArray("posts");
                            List<Post> posts = new ArrayList<>();
                            for (int j = 0; j < postsJSON.length(); ++j) {
                                Integer no = postsJSON.getJSONObject(j).getInt("no");
                                String date = postsJSON.getJSONObject(j).getString("now");
                                String sub = null;
                                if (postsJSON.getJSONObject(j).has("sub")){
                                    sub = postsJSON.getJSONObject(j).getString("sub");
                                    sub = StringEscapeUtils.unescapeHtml4(sub);
                                }
                                String com = null;
                                if (postsJSON.getJSONObject(j).has("com")){
                                    com = postsJSON.getJSONObject(j).getString("com");
                                    com = StringEscapeUtils.unescapeHtml4(com);
                                    com = Jsoup.parse(com).text();
                                }
                                Long tim = null;
                                if (postsJSON.getJSONObject(j).has("tim")){
                                    tim = postsJSON.getJSONObject(j).getLong("tim");
                                }
                                Integer id = null;
                                if (postsJSON.getJSONObject(j).has("id")){
                                    id = postsJSON.getJSONObject(j).getInt("id");
                                }
                                String filename = null;
                                if (postsJSON.getJSONObject(j).has("filename")){
                                    filename = postsJSON.getJSONObject(j).getString("filename");
                                    filename = StringEscapeUtils.unescapeHtml4(filename);
                                }
                                String ext = null;
                                if (postsJSON.getJSONObject(j).has("ext")){
                                    ext = postsJSON.getJSONObject(j).getString("ext");
                                    ext = StringEscapeUtils.unescapeHtml4(ext);
                                }
                                date = StringEscapeUtils.unescapeHtml4(date);
                                posts.add(new Post(no, date, sub, com, tim, id, filename, ext));
                            }
                            Post op = posts.get(0);
                            posts.remove(0);
                            threads.add(new ThreadPosts(op, posts, board));
                        }
                        Log.d("Thread reposiory: ", "loaded page " + page + "-------------------------------------------");
                        callback.Invoke(threads);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Looper.prepare();
                        Toast.makeText(context, "Failed to get threads", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void getImage(Callback<Bitmap> callback, String board, Long picId, String ext) {
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse("https://i.4cdn.org/")).newBuilder();
        builder.addPathSegment(board);
        builder.addPathSegment(picId + ext);
        Log.d("picture url ", builder.build().toString());
        Request request = new Request.Builder()
                .url(builder.build())
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.Invoke(BitmapFactory.decodeStream(Objects.requireNonNull(response.body()).byteStream()));
                    return;
                }
            }
        });
    }

    public void getThread(Callback<ThreadPosts> callback, Context context, String board, Integer no) {
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder();
        builder.addPathSegment(board);
        builder.addPathSegment("thread");
        builder.addPathSegment(no + ".json");
        Request request = new Request.Builder()
                .url(builder.build())
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toast.makeText(context, "Failed to get thread", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                        JSONArray postsJSON = json.getJSONArray("posts");
                        List<Post> posts = new ArrayList<>();
                        for (int j = 0; j < postsJSON.length(); ++j) {
                            Integer no = postsJSON.getJSONObject(j).getInt("no");
                            String date = postsJSON.getJSONObject(j).getString("now");
                            String sub = null;
                            if (postsJSON.getJSONObject(j).has("sub")){
                                sub = postsJSON.getJSONObject(j).getString("sub");
                                sub = StringEscapeUtils.unescapeHtml4(sub);
                            }
                            String com = null;
                            if (postsJSON.getJSONObject(j).has("com")){
                                com = postsJSON.getJSONObject(j).getString("com");
                                com = StringEscapeUtils.unescapeHtml4(com);
                                com = Jsoup.parse(com).text();
                            }
                            Long tim = null;
                            if (postsJSON.getJSONObject(j).has("tim")){
                                tim = postsJSON.getJSONObject(j).getLong("tim");
                            }
                            Integer id = null;
                            if (postsJSON.getJSONObject(j).has("id")){
                                id = postsJSON.getJSONObject(j).getInt("id");
                            }
                            String filename = null;
                            if (postsJSON.getJSONObject(j).has("filename")){
                                filename = postsJSON.getJSONObject(j).getString("filename");
                                filename = StringEscapeUtils.unescapeHtml4(filename);
                            }
                            String ext = null;
                            if (postsJSON.getJSONObject(j).has("ext")){
                                ext = postsJSON.getJSONObject(j).getString("ext");
                                ext = StringEscapeUtils.unescapeHtml4(ext);
                            }
                            date = StringEscapeUtils.unescapeHtml4(date);
                            posts.add(new Post(no, date, sub, com, tim, id, filename, ext));
                        }
                        Post op = posts.get(0);
                        posts.remove(0);
                        callback.Invoke(new ThreadPosts(op, posts, board));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Looper.prepare();
                        Toast.makeText(context, "Failed to get thread", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
