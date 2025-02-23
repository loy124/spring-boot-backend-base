package com.loy124.myapp.core.util.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import com.loy124.myapp.member.entity.Member;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;



@EntityListeners(AuditingEntityListener.class)
@Getter
@MappedSuperclass
public abstract class BaseEntity extends BaseTimeEntity {


    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "member_id")
    private Member createdBy;

    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", referencedColumnName = "member_id")
    private Member lastModifiedBy;

    public void setCreatedBy(Member createdBy) {
        this.createdBy = createdBy;
    }

    public void setLastModifiedBy(Member lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

}






