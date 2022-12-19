package nsu.mier.reader.ui.boards;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nsu.mier.reader.entity.Board;
import nsu.mier.reader.repository.BoardsRepository;

public class BoardsViewModel extends ViewModel {
    MutableLiveData<List<Board>> boards;
    public BoardsViewModel() {
        boards = new MutableLiveData<>();
        boards.setValue(new ArrayList<>());
    }

    public void setBoards(List<Board> boards) {
        this.boards.setValue(boards);
    }

    public LiveData<List<Board>> getBoards(Context context) {
        BoardsRepository.getInstance().getBoards((List<Board> boards) -> {
            this.boards.postValue(boards);
        }, context);
        return boards;
    }
}