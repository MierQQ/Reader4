package nsu.mier.reader.ui.favorite_boards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nsu.mier.reader.databinding.BoardCardBinding;
import nsu.mier.reader.databinding.FavoriteBoardCartBinding;
import nsu.mier.reader.entity.Board;

public class FavoriteBoardsAdapter extends RecyclerView.Adapter<FavoriteBoardsAdapter.FavoriteBoardHolder>{

    private List<Board> boardList;
    private ClickListener listener;
    private ClickListener deleteListener;

    public FavoriteBoardsAdapter(List<Board> boardList, ClickListener listener, ClickListener deleteListener) {
        setBoardList(boardList);
        this.listener = listener;
        this.deleteListener = deleteListener;
    }

    public void setBoardList(List<Board> boardList) {
        if (this.boardList == null) {
            this.boardList = boardList;
            notifyItemRangeInserted(0, boardList.size());
        } else {
            this.boardList = boardList;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public FavoriteBoardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FavoriteBoardCartBinding binding = FavoriteBoardCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FavoriteBoardHolder(binding, listener, deleteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteBoardHolder holder, int position) {
        holder.binding.boardTag.setText("/" + boardList.get(position).getBoard() + "/");
        holder.binding.boardName.setText(boardList.get(position).getTitle());
        holder.binding.boardDescription.setText(boardList.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public static class FavoriteBoardHolder extends RecyclerView.ViewHolder {
        FavoriteBoardCartBinding binding;
        ClickListener listener;

        public FavoriteBoardHolder(FavoriteBoardCartBinding binding, ClickListener listener, ClickListener deleteListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;

            binding.boardCard.setOnClickListener(view -> {
                listener.onItemClick(getAdapterPosition());
            });

            binding.button.setOnClickListener(view -> {
                deleteListener.onItemClick(getAdapterPosition());
            });
        }
    }

    public interface ClickListener {
        void onItemClick(int position);
    }
}
