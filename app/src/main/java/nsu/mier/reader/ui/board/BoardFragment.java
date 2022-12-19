package nsu.mier.reader.ui.board;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import nsu.mier.reader.R;
import nsu.mier.reader.databinding.FragmentBoardBinding;
import nsu.mier.reader.entity.Board;
import nsu.mier.reader.entity.Post;
import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.ui.boards.BoardsViewModel;
import nsu.mier.reader.ui.favorite_boards.FavoriteBoardsViewModel;

public class BoardFragment extends Fragment implements ThreadsAdapter.ClickListener{

    private FragmentBoardBinding binding;
    private BoardViewModel mViewModel;
    private String board;
    private Integer pages;
    private Integer page;
    private Integer perPages;
    private Integer position;
    private Boolean isFavorite;
    private LiveData<List<ThreadPosts>> threadPostsList;

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
        position = getArguments().getInt("position");
        pages = getArguments().getInt("pages");
        perPages = getArguments().getInt("perPages");
        isFavorite = getArguments().getBoolean("favorite", false);
        page = 1;

        threadPostsList = mViewModel.getThreadPosts();
        mViewModel.load(requireActivity(), board, 1);

        binding.textView3.setText("/" + board + "/");
        ThreadsAdapter adapter = new ThreadsAdapter(threadPostsList.getValue(), this, requireActivity());
        binding.recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(binding.getRoot().getContext());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisiblePosition = layoutManager.findLastVisibleItemPosition();
                if (!Objects.equals(page, pages) && lastVisiblePosition == threadPostsList.getValue().size() - 1) {
                    mViewModel.load(requireActivity(),  board, ++page);
                }
            }
        });
        binding.button3.setVisibility(isFavorite ? View.GONE : View.VISIBLE);
        binding.button3.setOnClickListener(view -> {
            Board board = new ViewModelProvider(requireActivity()).get(BoardsViewModel.class).getBoards(requireActivity()).getValue().get(position);
            new ViewModelProvider(requireActivity()).get(FavoriteBoardsViewModel.class).addBoard(board, requireActivity());
        });
        threadPostsList.observe(getViewLifecycleOwner(), adapter::setThreadList);
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
        bundle.putInt("no", threadPostsList.getValue().get(position).getOpPost().getNo());
        bundle.putInt("position", position);
        bundle.putString("board", board);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_thread, bundle);
    }
}