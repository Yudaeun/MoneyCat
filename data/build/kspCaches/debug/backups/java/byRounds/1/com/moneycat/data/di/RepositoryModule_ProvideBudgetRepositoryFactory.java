package com.moneycat.data.di;

import com.moneycat.data.repository.BudgetRepositoryImpl;
import com.moneycat.domain.repository.BudgetRepository;
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
public final class RepositoryModule_ProvideBudgetRepositoryFactory implements Factory<BudgetRepository> {
  private final Provider<BudgetRepositoryImpl> implProvider;

  public RepositoryModule_ProvideBudgetRepositoryFactory(
      Provider<BudgetRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public BudgetRepository get() {
    return provideBudgetRepository(implProvider.get());
  }

  public static RepositoryModule_ProvideBudgetRepositoryFactory create(
      Provider<BudgetRepositoryImpl> implProvider) {
    return new RepositoryModule_ProvideBudgetRepositoryFactory(implProvider);
  }

  public static BudgetRepository provideBudgetRepository(BudgetRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideBudgetRepository(impl));
  }
}
