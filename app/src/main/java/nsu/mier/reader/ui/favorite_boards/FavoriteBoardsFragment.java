package nsu.mier.reader.ui.favorite_boards;

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
    private List<Board> boards;
    private FragmentFavoriteBoardsBinding binding;
    private FavoriteBoardsViewModel mViewModel;
    private FavoriteBoardsAdapter adapter;

    public static FavoriteBoardsFragment newInstance() {
        return new FavoriteBoardsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(FavoriteBoardsViewModel.class);

        binding = FragmentFavoriteBoardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //todo get saved boards
        boards = new LinkedList<Board>();
        for (Integer i = 0; i < 10; ++i) {
            boards.add(new Board(i.toString(), i.toString(), i.toString(), i, i));
        }

        adapter = new FavoriteBoardsAdapter(boards, position -> {
            Bundle bundle = new Bundle();
            bundle.putString("board", boards.get(position).getBoard());
            bundle.putInt("pages", boards.get(position).getPages());
            bundle.putInt("perPages", boards.get(position).getPerPage());
            Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_board, bundle);
        }, position -> {
            boards.remove(position);
            adapter.setBoardList(boards);
        });
        binding.boards.setAdapter(adapter);
        binding.boards.setLayoutManager(new LinearLayoutManager(container.getContext()));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}