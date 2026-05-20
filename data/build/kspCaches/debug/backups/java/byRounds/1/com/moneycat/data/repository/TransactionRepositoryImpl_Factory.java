package com.moneycat.data.repository;

import com.moneycat.data.local.db.dao.TransactionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class TransactionRepositoryImpl_Factory implements Factory<TransactionRepositoryImpl> {
  private final Provider<TransactionDao> daoProvider;

  public TransactionRepositoryImpl_Factory(Provider<TransactionDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public TransactionRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static TransactionRepositoryImpl_Factory create(Provider<TransactionDao> daoProvider) {
    return new TransactionRepositoryImpl_Factory(daoProvider);
  }

  public static TransactionRepositoryImpl newInstance(TransactionDao dao) {
    return new TransactionRepositoryImpl(dao);
  }
}
