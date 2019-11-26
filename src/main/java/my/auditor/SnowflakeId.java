package my.auditor;

import lombok.NoArgsConstructor;
import my.utils.SnowFlake;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

@NoArgsConstructor
public class SnowflakeId implements IdentifierGenerator {
    
    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return SnowFlake.getInstance().nextId();
    }
}