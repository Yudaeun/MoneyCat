package com.day.moneycat.notification

import com.moneycat.data.local.db.entity.NotificationRuleEntity

object DefaultNotificationRules {
    val rules = listOf(
        NotificationRuleEntity(
            bankName = "신한카드",
            packageName = "com.shinhan.smartcarmanager",
            titlePattern = "신한카드",
            bodyPattern = "([\\d,]+)원.*?([가-힣a-zA-Z0-9 ]+?)에서",
            amountGroup = 1,
            merchantGroup = 2,
        ),
        NotificationRuleEntity(
            bankName = "삼성카드",
            packageName = "com.samsung.android.spay",
            titlePattern = "삼성카드",
            bodyPattern = "([\\d,]+)원.*?([가-힣a-zA-Z0-9 ]+?)에서",
            amountGroup = 1,
            merchantGroup = 2,
        ),
        NotificationRuleEntity(
            bankName = "KB국민카드",
            packageName = "com.kbcard.kbkookmincard",
            titlePattern = "KB국민카드",
            bodyPattern = "([\\d,]+)원 이용",
            amountGroup = 1,
            merchantGroup = null,
        ),
        NotificationRuleEntity(
            bankName = "현대카드",
            packageName = "com.hyundaicard.appcard",
            titlePattern = "현대카드",
            bodyPattern = "([\\d,]+)원.*?([가-힣a-zA-Z0-9 ]+?)에서",
            amountGroup = 1,
            merchantGroup = 2,
        ),
        NotificationRuleEntity(
            bankName = "롯데카드",
            packageName = "com.lottemembers.android",
            titlePattern = "롯데카드",
            bodyPattern = "([\\d,]+)원.*?([가-힣a-zA-Z0-9 ]+?)에서",
            amountGroup = 1,
            merchantGroup = 2,
        ),
        NotificationRuleEntity(
            bankName = "하나카드",
            packageName = "com.hanafinancial.hanapay",
            titlePattern = "하나카드",
            bodyPattern = "([\\d,]+)원.*?([가-힣a-zA-Z0-9 ]+?)에서",
            amountGroup = 1,
            merchantGroup = 2,
        ),
        NotificationRuleEntity(
            bankName = "NH농협카드",
            packageName = "nh.smart",
            titlePattern = "NH농협카드",
            bodyPattern = "([\\d,]+)원.*?([가-힣a-zA-Z0-9 ]+?)에서",
            amountGroup = 1,
            merchantGroup = 2,
        ),
        NotificationRuleEntity(
            bankName = "우리카드",
            packageName = "com.wooricard.smartwallet",
            titlePattern = "우리카드",
            bodyPattern = "([\\d,]+)원.*?([가-힣a-zA-Z0-9 ]+?)에서",
            amountGroup = 1,
            merchantGroup = 2,
        ),
    )
}
