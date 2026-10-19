package com.carl.job;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.carl.domain.TccFenceLog;
import com.carl.service.TccFenceLogService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.seata.core.model.GlobalStatus;
import org.apache.seata.tm.api.GlobalTransaction;
import org.apache.seata.tm.api.GlobalTransactionContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: Seata TCC要求try和confirm要一起成功，如果发生了try成功但是confirm失败的话，不会rollback,就需要人工介入/补偿
 * @author: carl
 * @date: 2026.10.19
 * @Since: 1.0
 */
@Component
@Slf4j
public class TCCJob {
    @Resource
    private TccFenceLogService tccFenceLogService;

    // 伪代码示意
    @Scheduled(fixedDelay = 60000) // 每分钟执行一次
    public void markFailedTransactionsForManualHandling() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thirtyMinutes = now.minusMinutes(30);

        // 1. 扫描可能已超时的、处于 CONFIRMING 状态的本地事务记录
        List<TccFenceLog> tccFenceLogs = tccFenceLogService.list(Wrappers.lambdaQuery(TccFenceLog.class)
                .eq(TccFenceLog::getStatus, 3)
                .ge(TccFenceLog::getGmtCreate, thirtyMinutes)
                .le(TccFenceLog::getGmtModified, now));
        if (CollUtil.isNotEmpty(tccFenceLogs)) {
            for (TccFenceLog tccFenceLog : tccFenceLogs) {
                try {
                    // 2. 通过 Seata API 重新加载全局事务并查询状态
                    GlobalTransaction tx = GlobalTransactionContext.reload(tccFenceLog.getXid());
                    GlobalStatus status = tx.getStatus();

                    // 3. 根据状态决定是否标记
                    if (status == GlobalStatus.CommitRetryTimeout ||
                        status == GlobalStatus.CommitFailed) {
                        // 持久性失败，标记为待人工处理
//                        txRecordDao.updateStatus(record.getId(), "CONFIRM_FAIL_NEED_MANUAL");
                        log.error("全局事务 {} 的Confirm阶段已确认失败，状态标记为待人工处理。", tccFenceLog.getXid());
                        // 此处可触发告警（邮件、钉钉、Prometheus等）
                    }
                } catch (Exception e) {
                    log.error("查询全局事务 {} 状态时发生异常", tccFenceLog.getXid(), e);
                }
            }
        }


    }
}
