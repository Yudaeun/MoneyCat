package com.moneycat.domain.model

enum class AssetType { CASH, DEPOSIT, SAVINGS, STOCK, FOREIGN_CURRENCY, OTHER }

enum class Currency { KRW, USD, JPY, EUR }

enum class TransactionType { INCOME, EXPENSE }

enum class PaymentMethod { CASH, CARD, TRANSFER }

enum class CardType { CREDIT, DEBIT, PREPAID }

enum class BenefitType { DISCOUNT, CASHBACK, POINT }

enum class InputSource { MANUAL, NOTIFICATION, OCR, CSV }

enum class InsightType { SPENDING_ALERT, SAVING_TIP, CARD_SUGGESTION, ANOMALY, WEEKLY_SUMMARY, GEMINI_ANALYSIS }

enum class CatExpression { DEFAULT, HAPPY, ALERT, THINKING, SLEEPING }
