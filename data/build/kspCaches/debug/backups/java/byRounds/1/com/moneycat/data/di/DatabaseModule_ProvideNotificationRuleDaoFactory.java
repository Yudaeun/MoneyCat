package com.moneycat.data.di;

import com.moneycat.data.local.db.MoneyCatDatabase;
import com.moneycat.data.local.db.dao.NotificationRuleDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class DatabaseModule_ProvideNotificationRuleDaoFactory implements Factory<NotificationRuleDao> {
  private final Provider<MoneyCatDatabase> dbProvider;

  public DatabaseModule_ProvideNotificationRuleDaoFactory(Provider<MoneyCatDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public NotificationRuleDao get() {
    return provideNotificationRuleDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideNotificationRuleDaoFactory create(
      Provider<MoneyCatDatabase> dbProvider) {
    return new DatabaseModule_ProvideNotificationRuleDaoFactory(dbProvider);
  }

  public static NotificationRuleDao provideNotificationRuleDao(MoneyCatDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideNotificationRuleDao(db));
  }
}
