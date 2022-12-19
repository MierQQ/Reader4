package nsu.mier.reader.ui.favorite_boards;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nsu.mier.reader.entity.Board;
import nsu.mier.reader.repository.BoardsRepository;

public class FavoriteBoardsViewModel extends ViewModel {
    MutableLiveData<List<Board>> boards;
    public FavoriteBoardsViewModel() {
        boards = new MutableLiveData<>();
        boards.setValue(new ArrayList<>());
    }

    public void setBoards(List<Board> boards) {
        this.boards.setValue(boards);
    }

    public void addBoard(Board board, Context context) {
        boards.getValue().add(board);
        boards.setValue(boards.getValue());
        saveBoards(boards.getValue(), context);
    }

    public void deleteBoard(Integer position, Context context) {
        Log.d("delete position: ", position + "");
        boards.getValue().remove(position.intValue());
        boards.setValue(boards.getValue());
        saveBoards(boards.getValue(), context);
    }

    public void saveBoards(List<Board> boards, Context context) {
        BoardsRepository.getInstance().saveBoards(boards, context);
    }

    public LiveData<List<Board>> getBoards(Context context) {
        BoardsRepository.getInstance().loadBoards((List<Board> boards) -> {
            this.boards.postValue(boards);
        }, context);
        return boards;
    }
}