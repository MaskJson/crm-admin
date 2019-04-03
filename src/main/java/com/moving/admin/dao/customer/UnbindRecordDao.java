package com.moving.admin.dao.customer;

import com.moving.admin.entity.customer.UnbindRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

public interface UnbindRecordDao extends JpaRepository<UnbindRecord, Long>, JpaSpecificationExecutor<UnbindRecord> {

    void deleteAllByCustomerIdAndUserId(Long customerId, Long userId);

    UnbindRecord findUnbindRecordByCustomerIdAndUserIdAndCreateTimeAfter(Long customerId, Long userId, Date createTime);

}
