package nsu.mier.reader.ui.favorite_treads;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import nsu.mier.reader.entity.ThreadPosts;
import nsu.mier.reader.repository.ThreadRepository;

public class FavoriteThreadsViewModel extends ViewModel {
    MutableLiveData<List<ThreadPosts>> threadPosts;
    public FavoriteThreadsViewModel() {
        threadPosts = new MutableLiveData<>();
        threadPosts.setValue(new ArrayList<>());
    }

    public void setThreads(List<ThreadPosts> threadPosts) {
        this.threadPosts.setValue(threadPosts);
    }

    public void addThreadPosts(ThreadPosts threadPost, Context context) {
        threadPosts.getValue().add(threadPost);
        threadPosts.setValue(threadPosts.getValue());
        saveThreads(threadPosts.getValue(), context);
    }

    public void deleteThreadPosts(Integer position, Context context) {
        Log.d("delete position: ", position + "");
        threadPosts.getValue().remove(position.intValue());
        threadPosts.setValue(threadPosts.getValue());
        saveThreads(threadPosts.getValue(), context);
    }

    public void saveThreads(List<ThreadPosts> threadPosts, Context context) {
        ThreadRepository.getInstance().saveThread(threadPosts, context);
    }

    public LiveData<List<ThreadPosts>> getThreads(Context context) {
        ThreadRepository.getInstance().loadThread((List<ThreadPosts> threadPosts) -> {
            this.threadPosts.postValue(threadPosts);
        }, context);
        return threadPosts;
    }
}