package com.moneycat.data.di;

import com.moneycat.data.repository.AiInsightRepositoryImpl;
import com.moneycat.domain.repository.AiInsightRepository;
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
public final class RepositoryModule_ProvideAiInsightRepositoryFactory implements Factory<AiInsightRepository> {
  private final Provider<AiInsightRepositoryImpl> implProvider;

  public RepositoryModule_ProvideAiInsightRepositoryFactory(
      Provider<AiInsightRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public AiInsightRepository get() {
    return provideAiInsightRepository(implProvider.get());
  }

  public static RepositoryModule_ProvideAiInsightRepositoryFactory create(
      Provider<AiInsightRepositoryImpl> implProvider) {
    return new RepositoryModule_ProvideAiInsightRepositoryFactory(implProvider);
  }

  public static AiInsightRepository provideAiInsightRepository(AiInsightRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideAiInsightRepository(impl));
  }
}
