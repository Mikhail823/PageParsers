package model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@Entity
@Table(name = "page")
public class SitePage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String path;

    private int code;

    @Type(type = "text")
    private String content;

    public SitePage(){}

    public SitePage(String path, int code, String content){
        this.path = path;
        this.code = code;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SitePage)) return false;
        SitePage sitePage = (SitePage) o;
        return getId() == sitePage.getId() && getCode() == sitePage.getCode() && Objects.equals(getPath(), sitePage.getPath()) && Objects.equals(getContent(), sitePage.getContent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPath(), getCode(), getContent());
    }

    @Override
    public String toString() {
        return "SitePage{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", code=" + code +
                ", content='" + content + '\'' +
                '}';
    }


}
