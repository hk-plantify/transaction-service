package com.plantify.transaction.global.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LockProvider {

    private final RedissonClient redissonClient;

    public RLock getUserLock(Long userId) {
        return redissonClient.getLock("transaction:" + userId);
    }
}