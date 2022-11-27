package nsu.mier.reader.ui.boards;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import nsu.mier.reader.databinding.BoardCardBinding;
import nsu.mier.reader.entity.Board;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.BoardHolder>{

    private List<Board> boardList;
    private ClickListener listener;

    public BoardAdapter(List<Board> boardList, ClickListener listener) {
        setBoardList(boardList);
        this.listener = listener;
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
    public BoardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BoardCardBinding binding = BoardCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BoardHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardHolder holder, int position) {
        holder.binding.boardTag.setText("/" + boardList.get(position).getBoard() + "/");
        holder.binding.boardName.setText(boardList.get(position).getTitle());
        holder.binding.boardDescription.setText(boardList.get(position).getInfo());
    }

    @Override
    public int getItemCount() {
        return boardList.size();
    }

    public static class BoardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        BoardCardBinding binding;
        ClickListener listener;

        public BoardHolder(BoardCardBinding binding, ClickListener listener) {
            super(binding.getRoot());
            this.binding = binding;
            this.listener = listener;

            binding.boardCard.setOnClickListener(this);
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
