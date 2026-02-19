package com.example.case4.adapter.out;

import com.example.case4.application.port.out.LoadCouponPort;
import com.example.case4.application.port.out.LoadOrderPort;
import com.example.case4.application.port.out.LoadProfilePort;
import org.springframework.stereotype.Component;

@Component
class ProfileAdapter implements LoadProfilePort { @Override public String loadName(Long userId) { return "user-" + userId; } }

@Component
class OrderAdapter implements LoadOrderPort { @Override public long loadCount(Long userId) { return 3L; } }

@Component
class CouponAdapter implements LoadCouponPort { @Override public int loadCount(Long userId) { return 2; } }
