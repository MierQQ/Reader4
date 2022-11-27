package nsu.mier.reader.ui.boards;


import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.List;

import nsu.mier.reader.R;
import nsu.mier.reader.databinding.FragmentBoardsBinding;
import nsu.mier.reader.entity.Board;

public class BoardsFragment extends Fragment implements BoardAdapter.ClickListener {
    private List<Board> boards;
    private FragmentBoardsBinding binding;
    private BoardsViewModel mViewModel;

    public static BoardsFragment newInstance() {
        return new BoardsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(BoardsViewModel.class);

        binding = FragmentBoardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //todo load boards
        boards = new LinkedList<Board>();
        for (Integer i = 0; i < 10; ++i) {
            boards.add(new Board(i.toString(), i.toString(), i.toString(), i, i));
        }

        BoardAdapter adapter = new BoardAdapter(boards, this);
        binding.boards.setAdapter(adapter);
        binding.boards.setLayoutManager(new LinearLayoutManager(container.getContext()));
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
        bundle.putString("board", boards.get(position).getBoard());
        bundle.putInt("pages", boards.get(position).getPages());
        bundle.putInt("perPages", boards.get(position).getPerPage());
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_board, bundle);
    }
}