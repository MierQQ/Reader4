package nsu.mier.reader.ui.board;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nsu.mier.reader.entity.Board;
import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.repository.BoardsRepository;
import nsu.mier.reader.repository.ThreadRepository;

public class BoardViewModel extends ViewModel {
    MutableLiveData<List<ThreadPosts>> threads;
    public BoardViewModel() {
        threads = new MutableLiveData<>();
        threads.setValue(new ArrayList<>());
    }

    public void setThreadPosts(List<ThreadPosts> threads) {
        this.threads.setValue(threads);
    }

    public LiveData<List<ThreadPosts>> getThreadPosts() {
        return threads;
    }
    public void load(Context context, String board, int page) {
        ThreadRepository.getInstance().getThreads((List<ThreadPosts> threads) -> {
            this.threads.getValue().addAll(threads);
            this.threads.postValue(this.threads.getValue());
        }, context, page, board);
    }
}