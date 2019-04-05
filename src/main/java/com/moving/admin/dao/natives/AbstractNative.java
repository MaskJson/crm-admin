package com.moving.admin.dao.natives;

import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;

public abstract class AbstractNative<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    public abstract void appendSort(Pageable pageable);

    public BigInteger objectToBigInteger(Object object) {
        BigInteger count = new BigInteger("0");
        try {
            // 页码超出查不到结果时报错
            count = (BigInteger) object;
        }catch (NoResultException e) {
        }
        return count;
    }

    public void simpleAppendSort(Pageable pageable, StringBuilder sortStr) {
        String[] orderBy = getSorts(pageable);
        int pageSize = pageable.getPageSize();
        int start = pageable.getPageNumber()*pageSize;
        sortStr.append(" a.").append(orderBy[0]).append(" ").append(orderBy[1]).append(" LIMIT ").append(start).append(",").append(pageSize);
    }

    public String[] getSorts(Pageable pageable) {
        String sort = pageable.getSort().toString();
        return sort.split(":");
    }

}
