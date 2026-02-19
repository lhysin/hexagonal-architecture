package com.example.hexagonal.adapter.out.persistence.product;

import com.example.hexagonal.application.port.in.GetProductSummaryUseCase.ProductSummaryView;
import com.example.hexagonal.application.port.out.LoadProductSummaryPort;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Component
public class ProductSummaryJdbcAdapter implements LoadProductSummaryPort {

    private final JdbcClient jdbcClient;

    public ProductSummaryJdbcAdapter(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public ProductSummaryView load(Long productId) {
        return jdbcClient.sql("""
                        select id, name, stock, display_price
                        from product
                        where id = :id
                        """)
                .param("id", productId)
                .query((rs, rowNum) -> new ProductSummaryView(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("stock"),
                        rs.getString("display_price")
                ))
                .single();
    }
}
