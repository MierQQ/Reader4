package nsu.mier.reader;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import nsu.mier.reader.databinding.ActivityMainBinding;
import nsu.mier.reader.entity.Board;
import nsu.mier.reader.entity.ThreadPosts;

public class MainActivity extends AppCompatActivity {

    static public ActivityMainBinding binding;
    public NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageView.setOnClickListener(view -> {
            binding.imageView.setVisibility(View.GONE);
        });

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_boards, R.id.navigation_favorite_boards, R.id.navigation_favorite_treads)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        File file = new File(getApplicationContext().getExternalFilesDir(null), "FavoriteBoards.obj");
        if (!file.exists()) {
            try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(new File(this.getExternalFilesDir(null), "FavoriteBoards.obj")))) {
                fos.writeObject(new ArrayList<Board>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        file = new File(getApplicationContext().getExternalFilesDir(null), "FavoriteThreads.obj");
        if (!file.exists()) {
            try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(new File(this.getExternalFilesDir(null), "FavoriteThreads.obj")))) {
                fos.writeObject(new ArrayList<ThreadPosts>());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}