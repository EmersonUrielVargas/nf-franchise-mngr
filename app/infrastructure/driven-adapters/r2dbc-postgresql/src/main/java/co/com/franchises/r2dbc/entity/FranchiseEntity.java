package co.com.franchises.r2dbc.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "franchises_app.franchises")
public class FranchiseEntity {
    @Id
    private String id;
    private String name;
}
