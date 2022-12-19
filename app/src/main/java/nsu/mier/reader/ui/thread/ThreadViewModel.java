package nsu.mier.reader.ui.thread;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.repository.ThreadRepository;

public class ThreadViewModel extends ViewModel {
    MutableLiveData<ThreadPosts> thread;
    public ThreadViewModel() {
        thread = new MutableLiveData<>();
        thread.setValue(null);
    }

    public void setThreadPosts(ThreadPosts thread) {
        this.thread.setValue(thread);
    }

    public LiveData<ThreadPosts> getThreadPosts(Context context, String board, int no) {
        ThreadRepository.getInstance().getThread((ThreadPosts thread) -> {
            this.thread.postValue(thread);
        }, context, board, no);
        return thread;
    }
}