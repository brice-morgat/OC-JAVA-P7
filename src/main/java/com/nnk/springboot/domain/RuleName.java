package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@Table(name = "rulename")
public class RuleName {
    @Id
    @SequenceGenerator(name = "rulename_gen", sequenceName = "rulename_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rulename_gen")
    private Integer id;
    @NotBlank(message = "Name is mandatory")
    private String name;
    private String description;
    private String json;
    private String template;
    private String sqlPart;
    private String sqlStr;

    public RuleName(String name,
                    String description, String json,
                    String template, String sqlStr,
                    String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;

    }

}
