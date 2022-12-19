package nsu.mier.reader.ui.board;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nsu.mier.reader.MainActivity;
import nsu.mier.reader.databinding.ThreadCardBinding;
import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.repository.ThreadRepository;

public class ThreadsAdapter extends RecyclerView.Adapter<ThreadsAdapter.ThreadHolder> {
    private List<ThreadPosts> threadList;
    private ClickListener listener;
    private Activity activity;

    public ThreadsAdapter(List<ThreadPosts> threadList, ClickListener listener, Activity activity) {
        setThreadList(threadList);
        this.listener = listener;
        this.activity = activity;
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
    public ThreadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ThreadCardBinding binding = ThreadCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ThreadHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ThreadHolder holder, int position) {
        ThreadPosts threadPosts = threadList.get(position);
        if (threadPosts.getOpPost().getTim() != null) {
            holder.binding.imageView2.setVisibility(View.VISIBLE);
            holder.binding.imageView2.setOnClickListener(view -> {
                MainActivity.binding.imageView.setVisibility(View.VISIBLE);
                ThreadRepository.getInstance().getImage(bitmap -> {
                    activity.runOnUiThread(() -> {
                        MainActivity.binding.imageView.setImageBitmap(bitmap);
                    });
                }, threadPosts.getBoard(), threadPosts.getOpPost().getTim(), threadPosts.getOpPost().getExt());
            });

            ThreadRepository.getInstance().getImage(bitmap -> {
                activity.runOnUiThread(() -> {
                    holder.binding.imageView2.setImageBitmap(bitmap);
                });
            }, threadPosts.getBoard(), threadPosts.getOpPost().getTim(), threadPosts.getOpPost().getExt());
        }
        holder.binding.dateAndNo.setText("Date: " + threadPosts.getOpPost().getDate() +
                " No." + threadPosts.getOpPost().getNo());
        holder.binding.ThreadTitle.setText(threadPosts.getOpPost().getSub());
        holder.binding.ThreadText.setText(threadPosts.getOpPost().getCom());
        PostAdapter adapter = new PostAdapter(threadPosts.getPosts(), pos -> {}, threadPosts.getBoard(), activity);
        holder.binding.threadPosts.setAdapter(adapter);
        holder.binding.threadPosts.setLayoutManager(new LinearLayoutManager(holder.binding.getRoot().getContext()));
    }

    @Override
    public int getItemCount() {
        return threadList.size();
    }

    public static class ThreadHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ThreadCardBinding binding;
        ClickListener listener;

        public ThreadHolder(ThreadCardBinding binding, ClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;

            binding.button2.setOnClickListener(this);
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
