package nsu.mier.reader.ui.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nsu.mier.reader.databinding.BoardCardBinding;
import nsu.mier.reader.databinding.PostCardBinding;
import nsu.mier.reader.entity.Board;
import nsu.mier.reader.entity.Post;
import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.ui.boards.BoardAdapter;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostHolder> {
    private List<Post> postList;
    private ClickListener listener;

    public PostAdapter(List<Post> postList, ClickListener listener) {
        setPostList(postList);
        this.listener = listener;
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
            //todo load image
        }
        holder.binding.ThreadTitle.setText("Date: " + post.getDate() +
                " No." + post.getNo());
        holder.binding.ThreadText.setText(post.getCom());
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
