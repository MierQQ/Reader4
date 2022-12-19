package nsu.mier.reader.ui.thread;

import androidx.lifecycle.LiveData;
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

import nsu.mier.reader.MainActivity;
import nsu.mier.reader.R;
import nsu.mier.reader.databinding.FragmentFavoriteThreadsBinding;
import nsu.mier.reader.databinding.FragmentThreadBinding;
import nsu.mier.reader.entity.Post;
import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.repository.ThreadRepository;
import nsu.mier.reader.ui.board.BoardViewModel;
import nsu.mier.reader.ui.board.PostAdapter;
import nsu.mier.reader.ui.favorite_treads.FavoriteThreadsViewModel;

public class ThreadFragment extends Fragment {

    private ThreadViewModel mViewModel;
    private FragmentThreadBinding binding;
    private LiveData<ThreadPosts> threadPosts;
    private Integer no;
    private Integer position;
    private Integer replies;
    private Integer page;
    private String board;
    private Boolean isFavorite;

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
        position = getArguments().getInt("position");
        replies = getArguments().getInt("replies");
        board = getArguments().getString("board");
        isFavorite = getArguments().getBoolean("favorite", false);
        Log.d("sss", "onCreateView: " + no);

        threadPosts = mViewModel.getThreadPosts(requireActivity(), board, no);

        PostAdapter adapter = new PostAdapter(new ArrayList<>(), pos -> {} , board, requireActivity());
        binding.threadPosts.setAdapter(adapter);
        binding.threadPosts.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.button4.setVisibility(isFavorite ? View.GONE : View.VISIBLE);
        binding.button4.setOnClickListener(view -> {
            if (threadPosts.getValue() != null) {
                new ViewModelProvider(this).get(FavoriteThreadsViewModel.class).addThreadPosts(threadPosts.getValue(), requireActivity());
            }
        });
        threadPosts.observe(getViewLifecycleOwner(), threadPosts1 -> {
            Log.d("sss", "onCreateView: " + threadPosts1);
            if (threadPosts1 != null) {
                if (threadPosts.getValue().getOpPost().getTim() != null) {
                    binding.imageView2.setVisibility(View.VISIBLE);
                    binding.imageView2.setOnClickListener(view -> {
                        MainActivity.binding.imageView.setVisibility(View.VISIBLE);
                        ThreadRepository.getInstance().getImage(bitmap -> {
                            requireActivity().runOnUiThread(() -> {
                                MainActivity.binding.imageView.setImageBitmap(bitmap);
                            });
                        }, board, threadPosts1.getOpPost().getTim(), threadPosts1.getOpPost().getExt());
                    });

                    ThreadRepository.getInstance().getImage(bitmap -> {
                        if (getActivity() == null) {
                            return;
                        }
                        requireActivity().runOnUiThread(() -> {
                            binding.imageView2.setImageBitmap(bitmap);
                        });
                    }, board, threadPosts1.getOpPost().getTim(), threadPosts1.getOpPost().getExt());
                }

                binding.dateAndNo.setText("Date: " + threadPosts.getValue().getOpPost().getDate() +
                        " No." + threadPosts.getValue().getOpPost().getNo());
                binding.ThreadTitle.setText(threadPosts.getValue().getOpPost().getSub());
                binding.ThreadText.setText(threadPosts.getValue().getOpPost().getCom());

                adapter.setPostList(threadPosts1.getPosts());
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}