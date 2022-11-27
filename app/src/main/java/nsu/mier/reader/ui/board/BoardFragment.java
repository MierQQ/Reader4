package nsu.mier.reader.ui.board;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import nsu.mier.reader.R;
import nsu.mier.reader.databinding.FragmentBoardBinding;
import nsu.mier.reader.entity.Post;
import nsu.mier.reader.entity.ThreadPosts;

public class BoardFragment extends Fragment implements ThreadsAdapter.ClickListener{

    private FragmentBoardBinding binding;
    private BoardViewModel mViewModel;
    private String board;
    private Integer pages;
    private Integer perPages;
    private List<ThreadPosts> threadPostsList;

    public static BoardFragment newInstance() {
        return new BoardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(BoardViewModel.class);

        binding = FragmentBoardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        board = getArguments().getString("board");
        pages = getArguments().getInt("pages");
        perPages = getArguments().getInt("perPages");

        //Todo load threads
        threadPostsList = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            posts.add(new Post(10000 + i, "12/01/24", null, "hello " + i, i % 2 == 0? null:1, i, null, null));
        }
        for (int i = 0; i < 10; ++i) {
            threadPostsList.add(new ThreadPosts(new Post(1000 + i, "22/10/22", "kok" + i, "kokwadssadsaddsaddkjaskldj", i % 2 == 0? null:1, i, null, null), posts));
        }

        binding.textView3.setText("/" + board + "/");
        ThreadsAdapter adapter = new ThreadsAdapter(threadPostsList, this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("no", threadPostsList.get(position).getOpPost().getNo());
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_thread, bundle);
    }
}