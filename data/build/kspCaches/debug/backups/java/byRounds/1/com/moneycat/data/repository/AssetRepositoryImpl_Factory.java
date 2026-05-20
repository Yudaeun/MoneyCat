package com.moneycat.data.repository;

import com.moneycat.data.local.db.dao.AssetDao;
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
public final class AssetRepositoryImpl_Factory implements Factory<AssetRepositoryImpl> {
  private final Provider<AssetDao> daoProvider;

  public AssetRepositoryImpl_Factory(Provider<AssetDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public AssetRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static AssetRepositoryImpl_Factory create(Provider<AssetDao> daoProvider) {
    return new AssetRepositoryImpl_Factory(daoProvider);
  }

  public static AssetRepositoryImpl newInstance(AssetDao dao) {
    return new AssetRepositoryImpl(dao);
  }
}
