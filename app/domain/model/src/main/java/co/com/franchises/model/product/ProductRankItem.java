package co.com.franchises.model.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ProductRankItem {
    private Long id;
    private String name;
    private Integer stock;
    private String branchName;
}
