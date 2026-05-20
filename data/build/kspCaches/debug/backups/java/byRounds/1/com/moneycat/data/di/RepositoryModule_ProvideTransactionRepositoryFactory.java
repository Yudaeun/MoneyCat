package com.moneycat.data.di;

import com.moneycat.data.repository.TransactionRepositoryImpl;
import com.moneycat.domain.repository.TransactionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class RepositoryModule_ProvideTransactionRepositoryFactory implements Factory<TransactionRepository> {
  private final Provider<TransactionRepositoryImpl> implProvider;

  public RepositoryModule_ProvideTransactionRepositoryFactory(
      Provider<TransactionRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public TransactionRepository get() {
    return provideTransactionRepository(implProvider.get());
  }

  public static RepositoryModule_ProvideTransactionRepositoryFactory create(
      Provider<TransactionRepositoryImpl> implProvider) {
    return new RepositoryModule_ProvideTransactionRepositoryFactory(implProvider);
  }

  public static TransactionRepository provideTransactionRepository(TransactionRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideTransactionRepository(impl));
  }
}
