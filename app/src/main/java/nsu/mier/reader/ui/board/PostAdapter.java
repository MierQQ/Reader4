package nsu.mier.reader.ui.board;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nsu.mier.reader.MainActivity;
import nsu.mier.reader.databinding.BoardCardBinding;
import nsu.mier.reader.databinding.PostCardBinding;
import nsu.mier.reader.entity.Board;
import nsu.mier.reader.entity.Post;
import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.repository.ThreadRepository;
import nsu.mier.reader.ui.boards.BoardAdapter;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private List<Post> postList;
    private ClickListener listener;
    private String board;
    private Activity activity;

    public PostAdapter(List<Post> postList, ClickListener listener, String board, Activity activity) {
        setPostList(postList);
        this.listener = listener;
        this.board = board;
        this.activity = activity;
    }

    public void setPostList(List<Post> postList) {
        if (this.postList == null) {
            this.postList = postList;
            notifyItemRangeInserted(0, postList.size());
        } else {
            this.postList = postList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostCardBinding binding = PostCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PostHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Post post = postList.get(position);
        if (post.getTim() != null) {
            holder.binding.imageView2.setVisibility(View.VISIBLE);
            holder.binding.imageView2.setOnClickListener(view -> {
                MainActivity.binding.imageView.setVisibility(View.VISIBLE);
                ThreadRepository.getInstance().getImage(bitmap -> {
                    activity.runOnUiThread(() -> {
                        MainActivity.binding.imageView.setImageBitmap(bitmap);
                    });
                }, board, post.getTim(), post.getExt());
            });

            ThreadRepository.getInstance().getImage(bitmap -> {
                activity.runOnUiThread(() -> {
                    holder.binding.imageView2.setImageBitmap(bitmap);
                });
            }, board, post.getTim(), post.getExt());
        }
        holder.binding.ThreadTitle.setText("Date: " + post.getDate() +
                " No." + post.getNo());
        holder.binding.ThreadText.setText(post.getCom() != null ? post.getCom() : null);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PostCardBinding binding;
        ClickListener listener;

        public PostHolder(PostCardBinding binding, ClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;


        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(getAdapterPosition());
        }
    }

    public interface ClickListener {
        void onItemClick(int position);
    }
}
