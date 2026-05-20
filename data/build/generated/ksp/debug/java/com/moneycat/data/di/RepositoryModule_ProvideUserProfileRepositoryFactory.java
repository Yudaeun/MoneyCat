package com.moneycat.data.di;

import com.moneycat.data.repository.UserProfileRepositoryImpl;
import com.moneycat.domain.repository.UserProfileRepository;
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
public final class RepositoryModule_ProvideUserProfileRepositoryFactory implements Factory<UserProfileRepository> {
  private final Provider<UserProfileRepositoryImpl> implProvider;

  public RepositoryModule_ProvideUserProfileRepositoryFactory(
      Provider<UserProfileRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public UserProfileRepository get() {
    return provideUserProfileRepository(implProvider.get());
  }

  public static RepositoryModule_ProvideUserProfileRepositoryFactory create(
      Provider<UserProfileRepositoryImpl> implProvider) {
    return new RepositoryModule_ProvideUserProfileRepositoryFactory(implProvider);
  }

  public static UserProfileRepository provideUserProfileRepository(UserProfileRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideUserProfileRepository(impl));
  }
}
