package nsu.mier.reader.ui.favorite_treads;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
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
    private LiveData<List<ThreadPosts>> threadPostsList;
    FavoriteThreadsAdapter adapter;

    public static FavoriteThreadsFragment newInstance() {
        return new FavoriteThreadsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(FavoriteThreadsViewModel.class);

        binding = FragmentFavoriteThreadsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        threadPostsList = mViewModel.getThreads(requireActivity());

        adapter = new FavoriteThreadsAdapter(threadPostsList.getValue(), position -> {
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putString("board", threadPostsList.getValue().get(position).getBoard());
            bundle.putInt("no", threadPostsList.getValue().get(position).getOpPost().getNo());
            bundle.putBoolean("favorite", true);
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_thread, bundle);
        }, position -> {
            mViewModel.deleteThreadPosts(position, requireActivity());
        }, requireActivity());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        threadPostsList.observe(getViewLifecycleOwner(), threadPosts -> {
            adapter.setThreadList(threadPosts);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}