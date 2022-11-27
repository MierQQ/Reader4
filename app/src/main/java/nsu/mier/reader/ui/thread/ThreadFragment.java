package nsu.mier.reader.ui.thread;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nsu.mier.reader.R;
import nsu.mier.reader.databinding.FragmentFavoriteThreadsBinding;
import nsu.mier.reader.databinding.FragmentThreadBinding;
import nsu.mier.reader.entity.Post;
import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.ui.board.PostAdapter;
import nsu.mier.reader.ui.favorite_treads.FavoriteThreadsViewModel;

public class ThreadFragment extends Fragment {

    private ThreadViewModel mViewModel;
    private FragmentThreadBinding binding;
    private ThreadPosts threadPosts;
    private Integer no;

    public static ThreadFragment newInstance() {
        return new ThreadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(ThreadViewModel.class);

        binding = FragmentThreadBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        no = getArguments().getInt("no");
        Log.d("sss", "onCreateView: " + no);

        //todo load thread
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 50; ++i) {
            posts.add(new Post(10000 + i, "12/01/24", null, "hello " + i, i % 2 == 0? null:1, i, null, null));
        }
        threadPosts = new ThreadPosts(new Post(no, "22/10/22", "kok " + no, "kokwadssadsaddsaddkjaskldj", no % 2 == 0? null:1, no, null, null), posts);

        if (threadPosts.getOpPost().getTim() != null) {
            binding.imageView2.setVisibility(View.VISIBLE);
            //todo load image
        }


        binding.dateAndNo.setText("Date: " + threadPosts.getOpPost().getDate() +
                " No." + threadPosts.getOpPost().getNo());
        binding.ThreadTitle.setText(threadPosts.getOpPost().getSub());
        binding.ThreadText.setText(threadPosts.getOpPost().getCom());
        PostAdapter adapter = new PostAdapter(threadPosts.getPosts(), pos -> {} );
        binding.threadPosts.setAdapter(adapter);
        binding.threadPosts.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}