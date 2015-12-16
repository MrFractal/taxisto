package ru.trendtech.domain.courier;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by petr on 24.09.2015.
 */
@Entity
@Table(name = "test_entity")
public class TestEntityWithCustomGenerateId {
    @Id
    @GeneratedValue(generator="LICENSE_TABLE_SEQ",strategy=GenerationType.TABLE)
    @TableGenerator(
    name="LICENSE_TABLE_SEQ",
    table="sequences",
    initialValue=-1,
    pkColumnName="seq_name", // Specify the name of the column of the primary key
    valueColumnName="seq_num", // Specify the name of the column that stores the last value generated
    pkColumnValue="id", // Specify the primary key column value that would be considered as a primary key generator
    allocationSize=100000000)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
