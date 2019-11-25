package my.auditor;

import lombok.NoArgsConstructor;
import my.utils.SnowFlake;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

@NoArgsConstructor
public class SnowflakeId implements IdentifierGenerator {

    @Override
    public Serializable generate(SessionImplementor s, Object obj) {
        return SnowFlake.getInstance().nextId();
    }
}