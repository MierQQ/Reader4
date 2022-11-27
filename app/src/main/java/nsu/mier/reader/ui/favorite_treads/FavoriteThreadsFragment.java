package nsu.mier.reader.ui.favorite_treads;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import nsu.mier.reader.R;
import nsu.mier.reader.databinding.FragmentFavoriteThreadsBinding;
import nsu.mier.reader.entity.Post;
import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.ui.board.ThreadsAdapter;

public class FavoriteThreadsFragment extends Fragment {

    private FavoriteThreadsViewModel mViewModel;
    private FragmentFavoriteThreadsBinding binding;
    private List<ThreadPosts> threadPostsList;
    FavoriteThreadsAdapter adapter;

    public static FavoriteThreadsFragment newInstance() {
        return new FavoriteThreadsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FavoriteThreadsViewModel.class);

        binding = FragmentFavoriteThreadsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Todo save threads
        threadPostsList = new ArrayList<>();
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            posts.add(new Post(10000 + i, "12/01/24", null, "hello " + i, i % 2 == 0? null:1, i, null, null));
        }
        for (int i = 0; i < 10; ++i) {
            threadPostsList.add(new ThreadPosts(new Post(1000 + i, "22/10/22", "kok" + i, "kokwadssadsaddsaddkjaskldj", i % 2 == 0? null:1, i, null, null), posts));
        }

        adapter = new FavoriteThreadsAdapter(threadPostsList, position -> {
            Bundle bundle = new Bundle();
            bundle.putInt("no", threadPostsList.get(position).getOpPost().getNo());
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_thread, bundle);
        }, position -> {
            threadPostsList.remove(position);
            adapter.setThreadList(threadPostsList);
        });
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}