package nsu.mier.reader.entity;

import java.util.List;
import java.util.Objects;

public class ThreadPosts {
    private Post opPost;
    private List<Post> posts;

    public ThreadPosts(Post opPost, List<Post> posts) {
        this.opPost = opPost;
        this.posts = posts;
    }

    public Post getOpPost() {
        return opPost;
    }

    public List<Post> getPosts() {
        return posts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ThreadPosts tread = (ThreadPosts) o;
        return Objects.equals(opPost, tread.opPost) && Objects.equals(posts, tread.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(opPost, posts);
    }
}
