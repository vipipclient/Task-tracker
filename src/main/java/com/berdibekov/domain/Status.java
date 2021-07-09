package com.berdibekov.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Max;

@Entity
@Getter
public enum Status {

    Open(0),
    Closed(1);
    @Column(name = "status_id")
    private int status;

    @Id
    @Setter
    private int id;

    private Status(int status){
        this.status = status;
    }

    @Column(name = "value")
    public String getValue(){
        return this.toString();
    }
}
