package com.moneycat.data.di;

import com.moneycat.data.local.db.MoneyCatDatabase;
import com.moneycat.data.local.db.dao.CardBenefitDao;
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
public final class DatabaseModule_ProvideCardBenefitDaoFactory implements Factory<CardBenefitDao> {
  private final Provider<MoneyCatDatabase> dbProvider;

  public DatabaseModule_ProvideCardBenefitDaoFactory(Provider<MoneyCatDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CardBenefitDao get() {
    return provideCardBenefitDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideCardBenefitDaoFactory create(
      Provider<MoneyCatDatabase> dbProvider) {
    return new DatabaseModule_ProvideCardBenefitDaoFactory(dbProvider);
  }

  public static CardBenefitDao provideCardBenefitDao(MoneyCatDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideCardBenefitDao(db));
  }
}
