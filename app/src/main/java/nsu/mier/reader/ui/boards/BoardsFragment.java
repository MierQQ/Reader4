package nsu.mier.reader.ui.boards;


import androidx.lifecycle.LiveData;
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
    private LiveData<List<Board>> boards;
    private FragmentBoardsBinding binding;
    private BoardsViewModel mViewModel;

    public static BoardsFragment newInstance() {
        return new BoardsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(requireActivity()).get(BoardsViewModel.class);

        binding = FragmentBoardsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        boards = mViewModel.getBoards(requireActivity());

        BoardAdapter adapter = new BoardAdapter(boards.getValue(), this);
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

    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putString("board", boards.getValue().get(position).getBoard());
        bundle.putInt("position", position);
        bundle.putInt("pages", boards.getValue().get(position).getPages());
        bundle.putInt("perPages", boards.getValue().get(position).getPerPage());
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_board, bundle);
    }
}