package nsu.mier.reader.ui.favorite_boards;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

import nsu.mier.reader.R;
import nsu.mier.reader.databinding.FragmentFavoriteBoardsBinding;
import nsu.mier.reader.entity.Board;
import nsu.mier.reader.ui.boards.BoardAdapter;

public class FavoriteBoardsFragment extends Fragment {
    private LiveData<List<Board>> boards;
    private FragmentFavoriteBoardsBinding binding;
    private FavoriteBoardsViewModel mViewModel;
    private FavoriteBoardsAdapter adapter;

    public static FavoriteBoardsFragment newInstance() {
        return new FavoriteBoardsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(FavoriteBoardsViewModel.class);

        binding = FragmentFavoriteBoardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        boards = mViewModel.getBoards(requireActivity());


        adapter = new FavoriteBoardsAdapter(boards.getValue(), position -> {
            Bundle bundle = new Bundle();
            bundle.putString("board", boards.getValue().get(position).getBoard());
            bundle.putInt("pages", boards.getValue().get(position).getPages());
            bundle.putInt("perPages", boards.getValue().get(position).getPerPage());
            bundle.putBoolean("favorite", true);
            Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_board, bundle);
        }, position -> {
            mViewModel.deleteBoard(position, requireActivity());
        });
        binding.boards.setAdapter(adapter);
        binding.boards.setLayoutManager(new LinearLayoutManager(container.getContext()));

        boards.observe(getViewLifecycleOwner(), adapter::setBoardList);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}