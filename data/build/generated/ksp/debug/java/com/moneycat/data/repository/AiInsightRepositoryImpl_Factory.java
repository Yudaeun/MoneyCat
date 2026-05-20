package com.moneycat.data.repository;

import com.moneycat.data.local.db.dao.AiInsightDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class AiInsightRepositoryImpl_Factory implements Factory<AiInsightRepositoryImpl> {
  private final Provider<AiInsightDao> daoProvider;

  public AiInsightRepositoryImpl_Factory(Provider<AiInsightDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public AiInsightRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static AiInsightRepositoryImpl_Factory create(Provider<AiInsightDao> daoProvider) {
    return new AiInsightRepositoryImpl_Factory(daoProvider);
  }

  public static AiInsightRepositoryImpl newInstance(AiInsightDao dao) {
    return new AiInsightRepositoryImpl(dao);
  }
}
