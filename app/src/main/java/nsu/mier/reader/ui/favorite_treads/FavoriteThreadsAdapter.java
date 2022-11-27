package nsu.mier.reader.ui.favorite_treads;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nsu.mier.reader.databinding.FavoriteThreadCardBinding;
import nsu.mier.reader.databinding.ThreadCardBinding;
import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.ui.board.PostAdapter;
import nsu.mier.reader.ui.board.ThreadsAdapter;

public class FavoriteThreadsAdapter extends RecyclerView.Adapter<FavoriteThreadsAdapter.FavoriteThreadsHolder>{
    private List<ThreadPosts> threadList;
    private ClickListener listener;
    private ClickListener deleteListener;

    public FavoriteThreadsAdapter(List<ThreadPosts> threadList, ClickListener listener, ClickListener deleteListener) {
        setThreadList(threadList);
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    public void setThreadList(List<ThreadPosts> boardList) {
        if (this.threadList == null) {
            this.threadList = boardList;
            notifyItemRangeInserted(0, boardList.size());
        } else {
            this.threadList = boardList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public FavoriteThreadsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FavoriteThreadCardBinding binding = FavoriteThreadCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FavoriteThreadsHolder(binding, listener, deleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteThreadsHolder holder, int position) {
        ThreadPosts threadPosts = threadList.get(position);
        if (threadPosts.getOpPost().getTim() != null) {
            holder.binding.imageView2.setVisibility(View.VISIBLE);
            //todo load image
        }
        holder.binding.dateAndNo.setText("Date: " + threadPosts.getOpPost().getDate() +
                " No." + threadPosts.getOpPost().getNo());
        holder.binding.ThreadTitle.setText(threadPosts.getOpPost().getSub());
        holder.binding.ThreadText.setText(threadPosts.getOpPost().getCom());
        //PostAdapter adapter = new PostAdapter(threadPosts.getPosts(), pos -> {} );
        //holder.binding.threadPosts.setAdapter(adapter);
        //holder.binding.threadPosts.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext()));
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    public static class FavoriteThreadsHolder extends RecyclerView.ViewHolder {
        FavoriteThreadCardBinding binding;

        public FavoriteThreadsHolder(FavoriteThreadCardBinding binding, ClickListener listener, ClickListener deleteListener) {
            super(binding.getRoot());
            this.binding = binding;

            binding.button2.setOnClickListener(view -> {listener.onItemClick(getAdapterPosition());});
            binding.button5.setOnClickListener(view -> {deleteListener.onItemClick(getAdapterPosition());});
        }

    }

    public interface ClickListener {
        void onItemClick(int position);
    }
}
