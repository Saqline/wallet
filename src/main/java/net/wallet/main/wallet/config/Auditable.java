package net.wallet.main.wallet.config;

import java.util.Date;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import static jakarta.persistence.TemporalType.TIMESTAMP;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable {
    
    @CreatedBy
    protected String createby;

    @CreatedDate
    @Temporal(TIMESTAMP)
    protected Date createat;

    @LastModifiedBy
    protected String updateby;

    @LastModifiedDate
    @Temporal(TIMESTAMP)
    protected Date updateat;
    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createdby) {
        this.createby = createdby;
    }

    public Date getCreateat() {
        return createat;
    }

    public void setCreateat(Date createdDate) {
        this.createat = createdDate;
    }

    public String getUpdateby() {
        return updateby;
    }

    public void setUpdateby(String lastModifiedBy) {
        this.updateby = lastModifiedBy;
    }

    public Date getUpdateat() {
        return updateat;
    }

    public void setUpdateat(Date lastModifiedDate) {
        this.updateat = lastModifiedDate;
    }
}
