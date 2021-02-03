package my.model.base;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class BaseModel extends Model implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @WhenCreated
    private Date createDate;

    @WhenModified
    private Date updateDate;
}
