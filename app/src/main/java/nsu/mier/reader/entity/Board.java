package nsu.mier.reader.entity;

import java.io.Serializable;
import java.util.Objects;

public class Board implements Serializable {
    private String board;
    private String title;
    private String info;
    private Integer pages;
    private Integer perPage;

    public Board(String board, String title, String info, Integer pages, Integer perPage) {
        this.board = board;
        this.title = title;
        this.info = info;
        this.pages = pages;
        this.perPage = perPage;
    }

    public String getBoard() {
        return board;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public Integer getPages() {
        return pages;
    }

    public Integer getPerPage() {
        return perPage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board1 = (Board) o;
        return Objects.equals(board, board1.board) && Objects.equals(title, board1.title) && Objects.equals(info, board1.info) && Objects.equals(pages, board1.pages) && Objects.equals(perPage, board1.perPage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, title, info, pages, perPage);
    }
}
