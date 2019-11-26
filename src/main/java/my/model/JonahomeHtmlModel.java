package my.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import my.model.base.BaseModel;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class JonahomeHtmlModel extends BaseModel {

    private static final long serialVersionUID = 1L;

    private Integer week;

    @Column(columnDefinition = "text")
    private String content;

}
