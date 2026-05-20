package com.moneycat.data.di;

import com.moneycat.data.repository.AssetRepositoryImpl;
import com.moneycat.domain.repository.AssetRepository;
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
public final class RepositoryModule_ProvideAssetRepositoryFactory implements Factory<AssetRepository> {
  private final Provider<AssetRepositoryImpl> implProvider;

  public RepositoryModule_ProvideAssetRepositoryFactory(
      Provider<AssetRepositoryImpl> implProvider) {
    this.implProvider = implProvider;
  }

  @Override
  public AssetRepository get() {
    return provideAssetRepository(implProvider.get());
  }

  public static RepositoryModule_ProvideAssetRepositoryFactory create(
      Provider<AssetRepositoryImpl> implProvider) {
    return new RepositoryModule_ProvideAssetRepositoryFactory(implProvider);
  }

  public static AssetRepository provideAssetRepository(AssetRepositoryImpl impl) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideAssetRepository(impl));
  }
}
