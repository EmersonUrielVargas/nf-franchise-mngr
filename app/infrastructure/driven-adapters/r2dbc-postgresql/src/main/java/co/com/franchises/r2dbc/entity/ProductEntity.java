package co.com.franchises.r2dbc.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "franchises_app.products")
public class ProductEntity {
    @Id
    private Long id;
    private String name;
    @Column("branch_id")
    private Long branchId;
    private Integer stock;
}
