package com.example.hexagonal.application.service;

import com.example.hexagonal.application.port.in.GetProductSummaryUseCase.ProductSummaryView;
import com.example.hexagonal.application.port.out.CachePort;
import com.example.hexagonal.application.port.out.LoadProductSummaryPort;
import java.time.Duration;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("GetProductSummaryService 테스트")
class GetProductSummaryServiceTest {

    private final CachePort cachePort = mock(CachePort.class);
    private final LoadProductSummaryPort loadProductSummaryPort = mock(LoadProductSummaryPort.class);
    private final GetProductSummaryService sut = new GetProductSummaryService(cachePort, loadProductSummaryPort);

    @Nested
    @DisplayName("get 메서드")
    class GetMethod {

        @Test
        @DisplayName("캐시 히트 시 DB를 조회하지 않아야 한다")
        void shouldNotLoadFromDbWhenCacheHit() {
            // given
            Long productId = 10L;
            when(cachePort.get("product:summary:10")).thenReturn(Optional.of("콜드브루|8|4900"));

            // when
            ProductSummaryView result = sut.get(productId);

            // then
            assertThat(result.name()).isEqualTo("콜드브루");
            assertThat(result.stock()).isEqualTo(8);
            assertThat(result.displayPrice()).isEqualTo("4900");
            verify(loadProductSummaryPort, never()).load(any());
        }

        @Test
        @DisplayName("캐시 미스 시 DB 조회 후 캐시에 저장해야 한다")
        void shouldLoadFromDbAndCacheWhenCacheMiss() {
            // given
            Long productId = 20L;
            ProductSummaryView dbValue = new ProductSummaryView(20L, "드립백", 12, "7900");
            when(cachePort.get("product:summary:20")).thenReturn(Optional.empty());
            when(loadProductSummaryPort.load(20L)).thenReturn(dbValue);

            // when
            ProductSummaryView result = sut.get(productId);

            // then
            assertThat(result).isEqualTo(dbValue);
            verify(loadProductSummaryPort).load(20L);
            verify(cachePort).set(eq("product:summary:20"), eq("드립백|12|7900"), any(Duration.class));
        }
    }
}
