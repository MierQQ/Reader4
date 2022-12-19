package nsu.mier.reader.entity;

import java.io.Serializable;
import java.util.Objects;

public class Post implements Serializable {
    private Integer no;
    private String date;
    private String sub;
    private String com;
    private Long tim;
    private Integer id;
    private String filename;
    private String ext;

    public Post(Integer no, String date, String sub, String com, Long tim, Integer id, String filename, String ext) {
        this.no = no;
        this.date = date;
        this.sub = sub;
        this.com = com;
        this.tim = tim;
        this.id = id;
        this.filename = filename;
        this.ext = ext;
    }

    public Integer getNo() {
        return no;
    }

    public String getDate() {
        return date;
    }

    public String getSub() {
        return sub;
    }

    public String getCom() {
        return com;
    }

    public Long getTim() {
        return tim;
    }

    public Integer getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getExt() {
        return ext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(no, post.no) && Objects.equals(date, post.date) && Objects.equals(sub, post.sub) && Objects.equals(com, post.com) && Objects.equals(tim, post.tim) && Objects.equals(id, post.id) && Objects.equals(filename, post.filename) && Objects.equals(ext, post.ext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(no, date, sub, com, tim, id, filename, ext);
    }
}
