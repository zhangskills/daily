package my.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import my.model.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Integer week;

    @Column(length = 1024)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

}
